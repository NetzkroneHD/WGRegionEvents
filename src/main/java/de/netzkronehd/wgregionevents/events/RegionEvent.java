package de.netzkronehd.wgregionevents.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class RegionEvent extends PlayerEvent {


    private static final HandlerList handlerList = new HandlerList();
    private final ProtectedRegion region;
    private final MovementWay movement;
    public final PlayerEvent parentEvent;
    private final Location from, to;

    public RegionEvent(ProtectedRegion region, Player player, MovementWay movement, PlayerEvent parent, Location from, Location to) {
        super(player);
        this.region = region;
        this.movement = movement;
        this.parentEvent = parent;
        this.to = to;
        this.from = from;
    }

    public HandlerList getHandlers() {
        return RegionEvent.handlerList;
    }

    public ProtectedRegion getRegion() {
        return this.region;
    }

    public static HandlerList getHandlerList() {
        return RegionEvent.handlerList;
    }

    public MovementWay getMovementWay() {
        return this.movement;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public PlayerEvent getParentEvent() {
        return this.parentEvent;
    }

}
