package de.diemex.inventorylib.inventory.views;


import de.diemex.inventorylib.events.ViewEvent;
import de.diemex.inventorylib.inventory.service.*;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A BasicView is basically a standard container with no special functionality and probably what you will use most of the time.
 *
 * @author Diemex
 */
public class BasicView implements IView
{
    /** Plugin instance */
    private final Plugin mPlugin;

    /** Inventory is bound to this Player */
    private final Player mPlayer;

    /** Inventory representing our GUI */
    private Inventory mInventory;

    /** Is this View viewed by the Player? */
    private ViewStatus mStatus;

    /** Title of this inventory */
    private String mTitle;

    /** The lines that this inventory currently has */
    private int mLines;

    /** Contents of this inventory */
    private Map<Integer, Button> mContents = new HashMap<Integer, Button>();

    /** If this exists pressing back will return you to the parent IView */
    private IView mParent;

    /** What to do when this View gets closed */
    private ViewExitBehaviour mOnExit = ViewExitBehaviour.CLOSE;


    /**
     * Create a BasicView, doesn't open it yet
     *
     * @param title  title text to display on the top
     * @param lines  how many rows do we need, one row holds 9 {@link de.diemex.inventorylib.inventory.views.Button}s
     * @param player main player for whom this inventory is meant
     * @param plugin owning plugin for running tasks and opening the inventory
     */
    public BasicView(String title, int lines, final Player player, Plugin plugin)
    {
        Validate.notNull(plugin);
        Validate.notNull(player);
        Validate.isTrue(lines <= 6 && lines >= 0, "Inventories bigger than 6 lines get glitched out. Input: " + lines);
        Validate.notNull(title);

        mPlugin = plugin;
        mPlayer = player;
        mLines = lines;
        mTitle = title;

        mStatus = ViewStatus.CLOSED;
        newInventory();
    }


    @Override
    public void setButton(final Button btn, final int index)
    {
        //Validate.isTrue(mLines * 9 > index, "Inserted index is out of bounds: " + index + "/" + mLines * 9 + " in view " + mTitle + ". Contents: " + mContents);
        //Increase size if OutOfBounds
        if (mLines * 9 < index)
            setSize((int) Math.ceil(index / 9.0));
        Validate.notNull(btn);
        Validate.isTrue(!mContents.containsKey(index), "Use replace to replace an existing Item");

        mContents.put(index, btn);
        switch (mStatus)
        {
            case CLOSED:
                mInventory.setItem(index, btn.asItemStack());
                break;
            case OPENED: //schedule the task to run one tick later to prevent glitchyness
                mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mInventory.setItem(index, btn.asItemStack());
                    }
                });
                break;
            default:
                throw new UnsupportedOperationException("Status " + mStatus.name() + " is unknown!");
        }

    }


    public void removeButton(final int index)
    {
        Validate.isTrue(mContents.containsKey(index), "No Button at the given index " + index + " found!");
        switch (mStatus)
        {
            case CLOSED:
                mInventory.setItem(index, null);
                break;
            case OPENED: //schedule the task to run one tick later to prevent glitchyness
                mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mInventory.setItem(index, null);
                    }
                });
                break;
            default:
                throw new UnsupportedOperationException("Status " + mStatus.name() + " is unknown!");
        }
    }


    @Override
    public Button getButton(int index)
    {
        Validate.isTrue(index < getSlots(), "Item at index " + index + " doesn't exist. Contents: " + Arrays.toString(mInventory.getContents()));
        return mContents.get(index);
    }


    @Override
    public void setSize(int newSize)
    {
        //TODO Validate that no items will get lost by the new size
        if (mLines != newSize)
        {
            mLines = newSize;
            newInventory();
            redraw();

            if (mStatus == ViewStatus.OPENED)
            {
                close();
                show();
            }
        }
    }


    @Override
    public int getSize()
    {
        return mLines;
    }


    @Override
    public int getSlots()
    {
        return mLines * 9;
    }


    @Override
    public void show()
    {
        ViewEvent openEvent = new ViewEvent(mPlayer, this, ViewEvent.ViewAction.OPEN);
        mPlugin.getServer().getPluginManager().callEvent(openEvent);
        if (!openEvent.isCancelled())
        {
            mStatus = ViewStatus.OPENED;
            mPlayer.openInventory(mInventory);
        }
    }


    @Override
    public void close()
    {
        switch (mStatus)
        {
            case OPENED:
                //Close this View
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
                            mStatus = ViewStatus.CLOSED;
                            mPlayer.closeInventory();
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
            case CLOSED:
                throw new IllegalStateException("Input is already closed");
        }
    }


    @Override
    public Inventory redraw()
    {
        mContents = LayoutParams.applyLayoutParams(mContents, mLines);
        mInventory.clear();
        for (Map.Entry<Integer, Button> btn : mContents.entrySet())
            mInventory.setItem(btn.getKey(), btn.getValue().asItemStack());
        return mInventory;
    }


    private void newInventory()
    {
        mInventory = mPlugin.getServer().createInventory(mPlayer, mLines * 9, mTitle);
    }


    @Override
    public void openOtherView(final IView other)
    {
        //TODO bool return
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
    public void setOnClick(final int buttonId, final IView other)
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
    public void setParent(IView parent)
    {
        mParent = parent;
        mOnExit = ViewExitBehaviour.RETURN_TO_PARENT;
    }
}