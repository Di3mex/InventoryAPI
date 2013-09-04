package de.diemex.inventorygui.inventory.service;


/**
 * What happens when exiting this IView
 *
 * @author Diemex
 */
public enum ViewExitBehaviour
{
    /** Close the window, default when no parent is present */
    CLOSE,
    /** Return to the previous Menu (the parent) */
    RETURN_TO_PARENT,
    /** DISABLED (Doesn't work) : Return the user to his inventory */
    RETURN_TO_INV,
    /** Deny the user the ability to exit the menu */
    DENY
}
