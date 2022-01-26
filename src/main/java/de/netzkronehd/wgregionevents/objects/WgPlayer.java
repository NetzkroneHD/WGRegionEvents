package de.netzkronehd.wgregionevents.objects;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.netzkronehd.wgregionevents.WgRegionEvents;
import de.netzkronehd.wgregionevents.events.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import java.util.*;

public class WgPlayer {

    private final Player player;
    private final List<ProtectedRegion> regions;

    public WgPlayer(Player player) {
        this.player = player;
        regions = new ArrayList<>();
    }

    public boolean updateRegions(MovementWay way, Location to, Location from, PlayerEvent parent) {
        final ApplicableRegionSet toRegions = WgRegionEvents.getInstance().getSimpleWorldGuardAPI().getRegions(to);
        final ApplicableRegionSet fromRegions = WgRegionEvents.getInstance().getSimpleWorldGuardAPI().getRegions(player.getLocation());
        if(!toRegions.getRegions().isEmpty()) {
            for(ProtectedRegion region : toRegions) {
                if(!regions.contains(region)) {
                    final RegionEnterEvent enter = new RegionEnterEvent(region, player, way, parent, from, to);
                    Bukkit.getPluginManager().callEvent(enter);
                    if(enter.isCancelled()) {
                        return true;
                    }
                    regions.add(region);
                    Bukkit.getScheduler().runTaskLater(WgRegionEvents.getInstance(), () -> Bukkit.getPluginManager().callEvent(new RegionEnteredEvent(region, player, way, parent, from, to)), 1);
                }

            }

            final Set<ProtectedRegion> toRemove = new HashSet<>();

            for(ProtectedRegion oldRegion : fromRegions) {
                if(!toRegions.getRegions().contains(oldRegion)) {
                    final RegionLeaveEvent leave = new RegionLeaveEvent(oldRegion, player, way, parent, from, to);
                    Bukkit.getPluginManager().callEvent(leave);
                    if(leave.isCancelled()) {
                        return true;
                    }
                    Bukkit.getScheduler().runTaskLater(WgRegionEvents.getInstance(), () -> Bukkit.getPluginManager().callEvent(new RegionLeftEvent(oldRegion, player, way, parent, from, to)), 1);
                    toRemove.add(oldRegion);
                }
            }
            regions.removeAll(toRemove);

        } else {
            for(ProtectedRegion region : regions) {
                final RegionLeaveEvent leave = new RegionLeaveEvent(region, player, way, parent, from, to);
                Bukkit.getPluginManager().callEvent(leave);
                if(leave.isCancelled()) {
                    return true;
                }
                Bukkit.getScheduler().runTaskLater(WgRegionEvents.getInstance(), () -> Bukkit.getPluginManager().callEvent(new RegionLeftEvent(region, player, way, parent, from, to)), 1);
            }
            regions.clear();
        }

        return false;
    }

    public List<ProtectedRegion> getRegions() {
        return regions;
    }

    public Player getPlayer() {
        return player;
    }
}
