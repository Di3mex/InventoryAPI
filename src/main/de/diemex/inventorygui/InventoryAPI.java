package de.diemex.inventorygui;

import de.diemex.inventorygui.command.Commander;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A simple yet powerful API allowing you to utilize inventories like a normal GUI system.
 *
 * @author Diemex
 */
public class InventoryAPI extends JavaPlugin
{
    @Override
    public void onEnable() {
        super.onEnable();
        getCommand("gui").setExecutor(new Commander(this));
    }

    public static String getTag()
    {
        return "[GuiAPI]";
    }
}
