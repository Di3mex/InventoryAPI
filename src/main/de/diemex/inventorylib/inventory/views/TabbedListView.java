package de.diemex.inventorylib.inventory.views;


import de.diemex.inventorylib.events.ViewEvent;
import de.diemex.inventorylib.inventory.service.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction of a tabbed UI, with back and forward buttons allowing you to navigate through the UI easily. A TabbedListView always has the same title. It is meant to be used
 * for Lists of Items which do not require a special formatting and just need to be displayed in the form of a List
 *
 * @author Diemex
 */
public class TabbedListView implements IView
{
    /** Player viewing this TabbedListView */
    private final Player mPlayer;

    /** A reference to the plugin owning this TabbedListView, we need it for the tasks to switch between the UI */
    private final Plugin mPlugin;

    /** Inventory in which to display this TabbedListView */
    private Inventory mInventory;

    /** Title of this TabbedListView */
    private final String mTitle;

    /** The size of the inventories in this TabbedListView (all the same) */
    private int mSize;

    /** The view which is currently being shown, -1 = no View */
    private int mViewShowing = -1;

    /** The status in which this view is currently in, like showing, hidden */
    private ViewStatus mStatus;

    /** Parent to go back to when exiting this TabbedListView */
    private IView mParent;

    /** One dimensional Stack of Popups, user can move back and forth */
    private List<IView> mViewStack;

    /** Icon for next, back buttons */
    private Material BACK = Material.GOLD_PLATE, NEXT = Material.IRON_PLATE;

    /** What to do when this View gets closed */
    private ViewExitBehaviour mOnExit = ViewExitBehaviour.CLOSE;


    /**
     * Very basic constructor.
     * <p/>
     * Will assume that it has no parent and therefore exit the Menu when clicking back.
     *
     * @param plugin owning Plugin
     */
    public TabbedListView(String title, Player player, Plugin plugin)
    {
        mTitle = title;
        mPlayer = player;
        mPlugin = plugin;
        mParent = null;
        mStatus = ViewStatus.CLOSED;
        mViewStack = new ArrayList<IView>();
    }


    /** @deprecated until I know what to do here */
    public void addView(IView menu)
    {
        mViewStack.add(menu);
        //TODO add buttons
    }


    /**
     * Create a IView from the list of items.
     * <p/>
     * Use this if you only want to display stuff lists of items with no particular functionality. It ensures that there is enough space for back and next (if needed) buttons
     *
     * @param items list of items from which to create the view
     */
    public void createView(List<Button> items)
    {
        mSize = (int) Math.ceil((items.size() + 2) / 9.0); //2 extra navigation buttons, 9 columns per row
        BasicView basicView = new BasicView(mTitle, mSize, mPlayer, mPlugin);

        int i = 0;
        for (Button item : items)
            basicView.setButton(item, i++);

        //Make sure all inventories have the same size so we can switch by just changing the contents
        for (IView v : mViewStack)
        {
            if (v.getSize() > mSize)
                mSize = v.getSize();
            v.setSize(mSize);
        }

        final int position = mViewStack.size() != 0 ? mViewStack.size() : 0; //currentPos
        if (position > 0)
        {
            Button backButton = new Button("Back", BACK);
            backButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public boolean onClick(ClickKind type)
                {
                    swapView(position - 1);
                    return true;
                }
            });
            backButton.getLayoutParams().addGravity(LayoutGravity.BOTTOM);
            backButton.getLayoutParams().setFixedColumn(8);
            basicView.setButton(backButton, mSize * 9 - 2); // bottom right corner, to the right we will put the next button
        }

        //Add a the next button to the previous View pointing to this View
        if (mViewStack.size() > 0)
        {
            Button nextButton = new Button("Next", NEXT);
            nextButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public boolean onClick(ClickKind type)
                {
                    swapView(position);
                    return true;
                }
            });
            nextButton.getLayoutParams().addGravity(LayoutGravity.BOTTOM);
            nextButton.getLayoutParams().addGravity(LayoutGravity.RIGHT);
            mViewStack.get(position - 1).setSize(mSize);
            mViewStack.get(position - 1).setButton(nextButton, mSize * 9 - 1); //bottom right corner
        }

        mViewStack.add(basicView);
    }


    /**
     * Switches the View to the next one
     *
     * @param which which View to switch to
     */
    public void swapView(final int which)
    {
        Validate.isTrue(mViewStack.size() > which && which >= 0, "Tried to show View number " + which + " but there are only " + mViewStack.size() + "(-1) Views to show");
        Validate.isTrue(mStatus == ViewStatus.OPENED, "Tried to swap a View when no view is currently being displayed");

        mViewShowing = which;

        Bukkit.getScheduler().runTask(mPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                final IView nextView = mViewStack.get(which);
                //items from the last inventory won't be cleared if the slot doesn't get used
                mInventory.clear();
                mInventory.setContents(nextView.redraw().getContents());
            }
        });
    }


    /**
     * Show the View with the given number.
     *
     * @param which number of view to show
     */
    public void showView(final int which)
    {
        Validate.isTrue(mViewStack.size() > which && which >= 0, "Tried to show View number " + which + " but there are only " + mViewStack.size() + "(-1) Views to show");

        switch (mStatus)
        {
            case OPENED:
                close();
                break;
            case CLOSED:
                mStatus = ViewStatus.OPENED;
                mViewShowing = which;

                ViewEvent openEvent = new ViewEvent(mPlayer, this, ViewEvent.ViewAction.OPEN);
                mPlugin.getServer().getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled())
                    Bukkit.getScheduler().runTask(mPlugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mInventory = mViewStack.get(which).redraw();
                            mPlayer.openInventory(mInventory);
                        }
                    });
                break;
            default:
                throw new UnsupportedOperationException("Status " + mStatus.name() + " not implemented!");
        }
    }


    /** Shows the first View in this TabbedListView */
    @Override
    public void show()
    {
        if (mViewStack.isEmpty())
            throw new IllegalStateException("No Views have been added to the TabbedListView!");

        showView(0);
    }


    /**
     * Closes the View currently being shown.
     *
     * @throws IllegalStateException if no View is being shown
     */
    @Override
    public void close()
    {
        switch (mStatus)
        {
            case CLOSED:
            {
                throw new IllegalStateException("There is no View being shown atm");
            }
            case OPENED:
            {
                switch (mOnExit)
                {
                    case CLOSE:
                    case RETURN_TO_PARENT:
                    case RETURN_TO_INV:
                    {
                        ViewEvent closeEvent = new ViewEvent(mPlayer, this, ViewEvent.ViewAction.CLOSE);
                        mPlugin.getServer().getPluginManager().callEvent(closeEvent);
                        if (!closeEvent.isCancelled())
                        {
                            mPlayer.closeInventory();
                            mViewShowing = -1; //= not showing
                            mStatus = ViewStatus.CLOSED;
                        }
                        break;
                    }
                }
                switch (mOnExit)
                {
                    case RETURN_TO_INV:
                    {
                        //Open the players "normal" inventory
                        //Doesn't work: I just want to open the Players normal inventory
                        break;
                    }
                    case RETURN_TO_PARENT:
                    {
                        if (mParent != null)
                            mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mParent.show();
                                }
                            });
                        break;
                    }
                    case DENY:
                    {
                        show();
                        return;
                    }
                }
                break;
            }
            default:
                throw new UnsupportedOperationException(mStatus.name() + " hasn't been implemented yet!");
        }
    }


    /**
     * Set the Item for the currently displayed View or throws an error
     *
     * @param btn   btn to set
     * @param index position to set
     */
    @Override
    public void setButton(Button btn, int index)
    {
        Validate.isTrue(mViewShowing >= 0, "TabbedListView isn't showing any View at the moment");
        Validate.notNull(btn);
        Validate.isTrue(index >= 0 && index < getSlots(), "Index " + index + " is out of bounds size: " + getSlots());
        mViewStack.get(mViewShowing).setButton(btn, index);
    }


    @Override
    public Button getButton(int index)
    {
        return mViewShowing >= 0 ? mViewStack.get(mViewShowing).getButton(index) : null;
    }


    @Override
    public Inventory redraw()
    {
        return mViewShowing >= 0 ? mViewStack.get(mViewShowing).redraw() : null;
    }


    @Override
    public int getSize()
    {
        return mSize;
    }


    @Override
    public void setSize(int lines)
    {
        throw new UnsupportedOperationException("A TabbedListView has the size of it's largest View");
    }


    @Override
    public int getSlots()
    {
        return mSize * 9;
    }


    @Override
    public void setOnClick(int buttonId, final IView other)
    {
        //TODO bool return
        //TODO set default button
        Validate.notNull(getButton(buttonId), "Button With id " + buttonId + " doesn't exist!");

        Button btn = getButton(buttonId);
        btn.setOnClickListener(new OnClickListener()
        {
            @Override
            public boolean onClick(ClickKind type)
            {
                mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        openOtherView(other);
                    }
                });
                return false;
            }
        });
        //Return to this View when child is closed
        other.setParent(this);
    }


    @Override
    public void openOtherView(final IView other)
    {
        close();
        mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                other.show();
            }
        });
    }


    @Override
    public void setParent(IView parent)
    {
        mParent = parent;
        mOnExit = ViewExitBehaviour.RETURN_TO_PARENT;
    }
}
