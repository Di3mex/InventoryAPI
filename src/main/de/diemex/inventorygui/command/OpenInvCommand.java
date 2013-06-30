package de.diemex.inventorygui.command;


import de.diemex.inventorygui.InventoryAPI;
import de.diemex.inventorygui.inventory.views.BasicView;
import de.diemex.inventorygui.inventory.views.Button;
import de.diemex.inventorygui.inventory.views.TabbedListView;
import de.diemex.inventorygui.service.ICommand;
import de.diemex.inventorygui.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Diemex
 */
public class OpenInvCommand implements ICommand
{
    @Override
    public boolean execute(InventoryAPI plugin, CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player && sender.hasPermission(PermissionNode.ADMIN.getNode()))
        {
            final BasicView basicView = new BasicView("derp", 1, (Player) sender, plugin);
            final Player player = (Player) sender;

            basicView.setButton(new Button("hi", Material.BRICK), 0);
            basicView.setButton(new Button("derp", Material.ACTIVATOR_RAIL), 1);
            final BasicView parent = new BasicView("PARENT", 2, (Player) sender, plugin);
            Button btn = new Button("SubView", Material.APPLE);
                btn.addDescLine(ChatColor.BOLD+"xoxo "+ChatColor.DARK_RED+" YOLO "+ChatColor.BLUE+" xD");
                parent.setButton(btn, 0);
                parent.setOnClick(0, basicView);
            basicView.setParent(parent);

            TabbedListView listView = new TabbedListView("Heyo ListView", (Player)sender, plugin);
                List<Button> buttons = new ArrayList<Button>();
                buttons.add(new Button("Item 1", Material.ANVIL));
                listView.createView(buttons);
            listView.setParent(parent);
            Button listBtn = new Button("Achievements", Material.BONE);
                listBtn.addDescLine("Click to view all achievements");
                parent.setButton(listBtn, 1);
                parent.setOnClick(1, listView);

            parent.show();

            /*plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Button btn = new Button("ahhahaha", Material.ARROW);
                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public boolean onClick() {
                            player.sendMessage("You clicked on Item 2");
                            return true;
                        }
                    });
                    basicView.setButton(btn, 2);
                }
            }, 10);

            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    basicView.removeButton(1);
                }
            }, 50);

            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    basicView.setSize(2);
                }
            }, 100);

            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    basicView.setButton(new Button("xD", Material.BEDROCK), 17);
                }
            }, 100);*/
        } else
        {
            /*TabbedListView group = new TabbedListView("Hey ViewGroup", (Player) sender, plugin);

            List<Button> buttons = new ArrayList<Button>();
            buttons.add(new Button("Hey", Material.EMPTY_MAP));
            buttons.add(new Button("derp", Material.MAGMA_CREAM));
            group.createView(buttons);

            List<Button> buttons2 = new ArrayList<Button>();
            buttons2.add(new Button("Hey", Material.BED));
            buttons2.add(new Button("qlimax", Material.BOW));
            buttons2.add(new Button("max", Material.APPLE));
            buttons2.add(new Button("howdy", Material.ANVIL));
            group.createView(buttons2);

            group.showView(0);*/

            /*ContainerView container = new ContainerView((Player) sender, plugin, "HEYO");
                BasicView view = new BasicView(plugin, (Player)sender, 4, "YOYO");
                view.setButton(new Button("Test", Material.ENCHANTED_BOOK), 0);
                container.setRootView(view);
                int id = container.addView(view);
                container.linkView(id, 0);
            container.show();*/
        }
        return true;
    }
}
