package de.diemex.inventorylib.inventory.service;


import de.diemex.inventorylib.inventory.views.Button;
import org.bukkit.inventory.Inventory;

/** @author Diemex */
public interface IView
{
    /** Shows the View. */ /*IMPORTANT!!!! A View has to call a ViewEvent with Action.OPEN for the onClickListeners to work */
    public void show();

    /** Closes the View. */ /* IMPORTANT!!!! A View has to call a ViewEvent with Action.CLOSE for the onClickListeners to work */
    public void close();

    /** Sets a Button at a specific position */
    public void setButton(Button btn, int index);

    /**
     * Get the Button at a specific Position in the View
     *
     * @param index position in the View
     *
     * @return button
     */
    public Button getButton(int index);

    /**
     * Redraw the UI, recreates the Inventory
     *
     * @return the new UI
     */
    public Inventory redraw();

    /**
     * Get the size of an inventory in lines
     *
     * @return size in lines
     */
    public int getSize();

    /**
     * Set the size of this IView in lines
     *
     * @param lines lines
     */
    public void setSize(int lines);

    /**
     * Get the amount of slots in this View
     *
     * @return slots as in lines * 9
     */
    public int getSlots();

    /**
     * Open another View when clicking on a button with a certain id
     *
     * @param buttonId id of the button
     * @param other    other View to open
     */
    public void setOnClick(final int buttonId, final IView other);

    /**
     * Close this View and open another
     *
     * @param other other View to open
     */
    public void openOtherView(final IView other);

    /**
     * Set the parent. If set and this View the View will return to the parent on close
     *
     * @param parent parent View to set and return to
     */
    public void setParent(IView parent);
}
