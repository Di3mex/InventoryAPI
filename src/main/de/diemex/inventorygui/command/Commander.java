/*
 * This file is part of
 * ExtraHardMode Server Plugin for Minecraft
 *
 * Copyright (C) 2012 Ryan Hamshire
 * Copyright (C) 2013 Diemex
 *
 * ExtraHardMode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExtraHardMode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero Public License
 * along with ExtraHardMode.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.diemex.inventorygui.command;

import de.diemex.inventorygui.InventoryAPI;
import de.diemex.inventorygui.service.CommandHandler;
import de.diemex.inventorygui.service.ICommand;
import de.diemex.inventorygui.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commander extends CommandHandler
{

    public Commander(InventoryAPI plugin)
    {
        super(plugin, "ehm");
        HelpCommand help = new HelpCommand();
        registerCommand("help", help);
        registerCommand("?", help);
        registerCommand("open", new OpenInvCommand());
    }

    @Override
    public boolean noArgs(CommandSender sender, Command command, String label)
    {
        sender.sendMessage(ChatColor.GRAY + "========= " + ChatColor.RED + plugin.getName() + ChatColor.GRAY + " =========");
        sender.sendMessage(" /ehm");
        sender.sendMessage("    help" + ChatColor.YELLOW + " - Show the help menu");
        sender.sendMessage("    version" + ChatColor.YELLOW + " - Show version info");
        if (sender.hasPermission(PermissionNode.ADMIN.getNode()))
        {
            sender.sendMessage("    reload " + ChatColor.YELLOW + "- Reload the plugin");
            sender.sendMessage("    open   " + ChatColor.YELLOW + "- Open a test menu");
        }
        return true;
    }

    @Override
    public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args)
    {
        sender.sendMessage(ChatColor.YELLOW + plugin.getTag() + " Unknown command: " + ChatColor.WHITE + args[0]);
        return true;
    }

    private class HelpCommand implements ICommand
    {

        @Override
        public boolean execute(InventoryAPI plugin, CommandSender sender, Command command, String label, String[] args)
        {
            return noArgs(sender, command, label);
        }

    }
}
