package de.diemex.inventorygui;


import de.diemex.inventorygui.command.Commander;
import de.diemex.inventorygui.inventory.ViewManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A simple yet powerful API allowing you to utilize inventories like a normal GUI system.
 *
 * @author Diemex
 */
public class InventoryAPI extends JavaPlugin
{
    private ViewManager manager;


    @Override
    public void onEnable()
    {
        super.onEnable();
        getCommand("gui").setExecutor(new Commander(this));
        getViewManager();
    }


    /**
     * Get the ViewManager associated with the API
     *
     * @return view manager
     */
    public ViewManager getViewManager()
    {
        if (manager == null)
            manager = new ViewManager(this);
        return manager;
    }


    /**
     * Use this method when you create new Views
     *
     * @return instance of an InventoryAPI
     */
    public InventoryAPI getInstance()
    {
        return this;
    }


    public static String getTag()
    {
        return "[GuiAPI]";
    }
}
