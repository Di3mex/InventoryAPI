package de.diemex.inventorylib.events;


import de.diemex.inventorylib.inventory.service.IView;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** @author Diemex */
public class ViewEvent extends Event implements Cancellable
{
    private boolean mCancelled;
    /** The Player viewing this View */
    private final Player mPlayer;
    /** The open View */
    private final IView mView;

    /** What action has been taken */
    private final ViewAction mAction;


    /**
     * Create a new ViewEvent
     *
     * @param player the player
     * @param view   view being closed
     */
    public ViewEvent(Player player, IView view, ViewAction action)
    {
        mPlayer = player;
        mView = view;
        mAction = action;
    }


    /** @return player */
    public Player getPlayer()
    {
        return mPlayer;
    }


    /** @return view */
    public IView getView()
    {
        return mView;
    }


    /**
     * Get the Action of this Event
     *
     * @return action
     */
    public ViewAction getAction()
    {
        return mAction;
    }


    /** @return if Event got cancelled */
    @Override
    public boolean isCancelled()
    {
        return mCancelled;
    }


    /** @param cancelled set if the closing of the View got cancelled */
    @Override
    public void setCancelled(boolean cancelled)
    {
        this.mCancelled = cancelled;
    }


    public enum ViewAction
    {
        /** When a View is closed by the Player */
        CLOSE,
        /** When a View is opened by the Player */
        OPEN
    }


    //Every Event needs this
    private static final HandlerList HANDLERS = new HandlerList();


    public HandlerList getHandlers()
    {
        return HANDLERS;
    }


    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }
}
