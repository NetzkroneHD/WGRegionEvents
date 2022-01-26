package de.netzkronehd.wgregionevents.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * Thrown when a {@link Player} has already left a {@link ProtectedRegion}
 */
public class RegionLeftEvent extends RegionEvent {

    public RegionLeftEvent(ProtectedRegion region, Player player, MovementWay movement, PlayerEvent parent, Location from, Location to) {
        super(region, player, movement, parent, from, to);
    }

}
