package de.diemex.inventorylib.inventory;


import de.diemex.inventorylib.events.ViewEvent;
import de.diemex.inventorylib.inventory.service.ClickKind;
import de.diemex.inventorylib.inventory.service.IView;
import de.diemex.inventorylib.inventory.views.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A ViewManager handles all the clicking and editing of an Inventory. You have to create one for the OnClickListeners to work, or better said for anything to work at all.
 *
 * @author Diemex
 */
public class ViewManager implements Listener
{
    /** Instance of the API to which this ViewManager is bound */
    private final Plugin mPlugin;

    /** References to open inventories */
    private Map<String, IView> mOpenViews = new HashMap<String, IView>();


    /**
     * Creates the ViewManager and most importantly registers the events to the plugin
     *
     * @param plugin owning plugin
     */
    public ViewManager(Plugin plugin)
    {
        mPlugin = plugin;
        mPlugin.getServer().getPluginManager().registerEvents(this, mPlugin);
    }


    /** Called for normal clicks like left/right click and shiftclick */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryClick(InventoryClickEvent event)
    {
        final Player player = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;

        if (player != null && mOpenViews.containsKey(player.getName()))
        {
            final IView view = mOpenViews.get(player.getName());
            final int slot = event.getRawSlot();

            switch (event.getAction())
            {
                case MOVE_TO_OTHER_INVENTORY:
                    event.setCancelled(true);
                    break;
            }

            //Only cancel Events in the actual custom inventory. Let players still utilize their normal inventory.
            if (view.getSlots() > slot && slot >= 0)
            {
                event.setCancelled(true);
                Button btn = view.getButton(slot);
                if (btn != null)
                    btn.onClick(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT ? ClickKind.RIGHT : ClickKind.LEFT);
            }
        }
    }


    /**
     * When a player "drags" items in his inventory
     *
     * @param event Event that occurred
     */
    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event)
    {
        final Player player = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;

        if (player != null && mOpenViews.containsKey(player.getName()))
        {
            IView view = mOpenViews.get(player.getName());
            Set<Integer> affectedSlots = event.getRawSlots();
            for (Integer slot : affectedSlots)
            {
                if (slot >= 0 && slot < view.getSlots())
                {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }


    /** Called when a View is closed by a player */
    @EventHandler
    private void onViewEvent(ViewEvent event)
    {
        final String playerName = event.getPlayer().getName();
        switch (event.getAction())
        {
            case CLOSE:
                mOpenViews.remove(playerName);
                break;
            case OPEN:
                mOpenViews.put(playerName, event.getView());
                break;
        }
    }


    /** Called when an Inventory is closed by a Player */
    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event)
    {
        if (event.getPlayer() instanceof Player)
        {
            final Player player = (Player) event.getPlayer();
            if (mOpenViews.containsKey(player.getName()))
            {
                mOpenViews.get(player.getName()).close();
                mOpenViews.remove(player.getName());
            }
        }
    }
}
