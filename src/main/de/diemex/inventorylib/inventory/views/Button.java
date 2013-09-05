package de.diemex.inventorylib.inventory.views;


import de.diemex.inventorylib.inventory.service.ClickKind;
import de.diemex.inventorylib.inventory.service.LayoutParams;
import de.diemex.inventorylib.inventory.service.OnClickListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a clickable Button in an inventory
 *
 * @author Diemex
 */
public class Button
{
    private ItemStack mItem;

    /** Title of the Button */
    private String mTitle;

    /** Count to be displayed */
    private int mCount = 1;

    /** Description which will be displayed as lore on the Item */
    private List<String> mDescription = new ArrayList<String>();

    /** Parameters to lay this Button out */
    private LayoutParams mLayoutParams = new LayoutParams();

    /** Material to use as an icon */
    private MaterialData mIcon;

    /** Will be called if this Item is to be clicked */
    private OnClickListener mOnClickListener;

    /** If set and this is a right click this listener will be called instead */
    private OnClickListener mOnRightClickListener;


    /**
     * Your trusty constructor
     *
     * @param title the name of this icon, can be null or "" if you don't want a special title
     * @param icon  Material to use as an "icon", can't be null
     */
    public Button(String title, Material icon)
    {
        Validate.notNull(icon);
        this.mTitle = title;
        this.mIcon = new MaterialData(icon);
    }


    /**
     * Your trusty constructor
     *
     * @param title the name of this icon, can be null or "" if you don't want a special title
     * @param icon  MaterialData to use as an "icon", can include meta, TODO fix not working  meta
     */
    public Button(String title, MaterialData icon)
    {
        Validate.notNull(icon);
        this.mTitle = title;
        this.mIcon = icon;
    }


    /**
     * Get the title to be displayed
     *
     * @return title
     */
    public String getTitle()
    {
        return mTitle;
    }


    /**
     * Set the title to be displayed, supply null or "" to clear the title
     *
     * @param mTitle new title to set
     */
    public void setTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }


    /**
     * Get the icon including its MaterialData
     *
     * @return MaterialData which includes damageValues
     */
    public MaterialData getIcon()
    {
        return mIcon;
    }


    public void setIcon(Material icon)
    {
        mIcon = new MaterialData(icon);
    }


    /**
     * Set the Icon to MaterialData
     *
     * @param mIcon icon to set
     */
    public void setIcon(MaterialData mIcon)
    {
        this.mIcon = mIcon;
    }


    /**
     * Adds a line of text to the lore
     *
     * @param line line to add
     */
    public void addDescLine(String line)
    {
        //TODO Validation
        mDescription.add(line);
        //Invalidate the ItemStack
        mItem = null;
    }


    /**
     * Set the lore to the list of lines, overwrites existing lines
     *
     * @param lines lines to set
     */
    public void setDescription(List<String> lines)
    {
        //TODO Validation
        mDescription = lines;
        //Force redraw of Item
        mItem = null;
    }


    public List<String> getDescription()
    {
        return mDescription;
    }


    /** Get count */
    public int getCount()
    {
        return mCount;
    }


    /** Set the number to display on this Button, meaning the item count */
    public void setCount(int mCount)
    {
        this.mCount = mCount;
    }


    /**
     * Get the ItemStack representing this item
     *
     * @return representation of this icon
     */
    public ItemStack asItemStack()
    {
        if (mItem == null)
            mItem = createItemStack(mTitle, mIcon);
        return mItem;
    }


    /**
     * Create an ItemStack for this Button
     *
     * @param title as in the name of the icon
     * @param data  icon to use
     *
     * @return an ItemStack
     */
    private ItemStack createItemStack(String title, MaterialData data)
    {
        ItemStack item = new ItemStack(data.getItemType(), mCount);
        ItemMeta meat = item.getItemMeta();
        if (!title.isEmpty())
            meat.setDisplayName(title);
        if (!mDescription.isEmpty())
            meat.setLore(mDescription);
        item.setItemMeta(meat);
        return item;
    }


    /**
     * Called when a Player clicks on this Button
     *
     * @param type click type left/right
     *
     * @return if successful
     */
    public boolean onClick(ClickKind type)
    {
        switch (type)
        {
            case LEFT:
                if (isListenerSet(type))
                    return mOnClickListener.onClick(type);
                break;
            case RIGHT: //allow for a separate right click listener
                if (isListenerSet(type))
                    return mOnRightClickListener.onClick(type);
                else if (isListenerSet(ClickKind.LEFT))
                    return mOnClickListener.onClick(type);
        }
        return false;
    }


    public boolean hasLayoutParams()
    {
        return !mLayoutParams.isEmpty();
    }


    public void setLayoutParams(LayoutParams layoutParams)
    {
        mLayoutParams = layoutParams;
    }


    public LayoutParams getLayoutParams()
    {
        return mLayoutParams;
    }


    /**
     * Set the listener which will be called when a user clicks this item
     *
     * @param listener listener to set
     */
    public void setOnClickListener(OnClickListener listener)
    {
        mOnClickListener = listener;
    }


    /**
     * Use this method if you want to do something special on RightClick.
     *
     * @param listener listener to set
     */
    public void setOnRightClickListener(OnClickListener listener)
    {
        mOnRightClickListener = listener;
    }


    /**
     * Is an onClickListener set?
     *
     * @return if onClickListener != null
     */
    private boolean isListenerSet(ClickKind type)
    {
        switch (type)
        {
            case LEFT:
                return mOnClickListener != null;
            case RIGHT:
                return mOnRightClickListener != null;
            default:
                throw new UnsupportedOperationException("Not a known clicktype " + type.name());
        }
    }


    @Override
    public String toString()
    {
        return mTitle;
    }
}
