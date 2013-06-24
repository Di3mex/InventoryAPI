package de.diemex.inventorygui.command;

import de.diemex.inventorygui.InventoryAPI;
import de.diemex.inventorygui.View;
import de.diemex.inventorygui.service.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Diemex
 */
public class OpenInvCommand implements ICommand
{
    @Override
    public boolean execute(InventoryAPI plugin, CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
            new View(plugin, (Player) sender, 9, "derp").show();
        return true;
    }
}
