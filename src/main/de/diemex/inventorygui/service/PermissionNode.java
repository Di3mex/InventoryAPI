package de.diemex.inventorygui.service;


/**
 * All known permission nodes.
 */
public enum PermissionNode
{
    /**
     * Admin, reloading etc
     */
    ADMIN("admin"),;

    /**
     * Prefix for all permission nodes.
     */
    private static final String PREFIX = "InventoryAPI.";

    /**
     * Resulting permission node path.
     */
    private final String node;


    /**
     * Constructor.
     *
     * @param subperm
     *         - specific permission path.
     */
    private PermissionNode(String subperm)
    {
        this.node = PREFIX + subperm;
    }


    /**
     * Get the full permission node path.
     *
     * @return Permission node path.
     */
    public String getNode()
    {
        return node;
    }
}