package de.netzkronehd.wgregionevents.api;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.netzkronehd.wgregionevents.WgRegionEvents;
import de.netzkronehd.wgregionevents.objects.WgPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleWorldGuardAPI {

    public SimpleWorldGuardAPI() {}

    /**
     * Receives a {@link ProtectedRegion} via id.
     * @param regionId Id of the {@link ProtectedRegion}
     * @return Instance of a {@link ProtectedRegion} - null if the {@link ProtectedRegion} does not exist
     */
    public ProtectedRegion getRegion(String regionId) {
        for(World world : Bukkit.getWorlds()) {
            final ProtectedRegion rg = getRegion(regionId, world);
            if (rg != null) {
                return rg;
            }
        }
        return null;
    }

    /**
     * Checks if {@link Location} is in {@link ProtectedRegion}
     * @param loc {@link Location Location} which should be checked
     * @param regionId Id of the {@link ProtectedRegion}
     * @return true if the {@link Location} is in the {@link ProtectedRegion Region}
     */
    public boolean isInRegion(Location loc, String regionId) {
        for(ProtectedRegion region : getRegions(loc)) {
            if(region.getId().equalsIgnoreCase(regionId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Receives the {@link ProtectedRegion} via id.
     * @param regionId Id of the {@link ProtectedRegion}
     * @param world {@link World} which the region is in
     * @return Instance of {@link ProtectedRegion} - null if the {@link ProtectedRegion} does not exist
     */
    public ProtectedRegion getRegion(String regionId, World world) {
        final RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regions != null) {
            return regions.getRegion(regionId);
        }
        return null;
    }


    /**
     * Receives a {@link Map} of all {@link ProtectedRegion ProtectedRegions}.
     * @return A {@link Map} of all {@link ProtectedRegion ProtectedRegions} that exists.
     */
    public Map<String, ProtectedRegion> getRegions() {
        final Map<String, ProtectedRegion> regions = new HashMap<>();
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        for(World world : Bukkit.getWorlds()) {
            regions.putAll(container.get(BukkitAdapter.adapt(world)).getRegions());
        }
        return regions;
    }

    /**
     * Receives all {@link ProtectedRegion ProtectedRegions} that exists in a {@link World}
     * @param world {@link World} to be checked
     * @return A {@link Map} of all {@link ProtectedRegion ProtectedRegions} that exists in a {@link World}.
     */
    public Map<String, ProtectedRegion> getRegions(World world) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getRegions();
    }


    /**
     * Receives a {@link ApplicableRegionSet} via a {@link Location}.
     * @param loc {@link Location} of the {@link ApplicableRegionSet}
     * @return Instance of {@link ApplicableRegionSet}
     */
    public ApplicableRegionSet getRegions(Location loc) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
    }

    /**
     * Receives a {@link List} of players that are in a specific region via id.
     * @param regionId Id of the {@link ProtectedRegion}
     * @return List of {@link Player Players}
     */
    public List<Player> getPlayersInRegion(String regionId) {
        final List<Player> players = new ArrayList<>();
        for(WgPlayer wgPlayer : WgRegionEvents.getInstance().getPlayerCache().values()) {
            wgPlayer.getRegions().forEach(protectedRegion -> {
                if(regionId.equals(protectedRegion.getId())) players.add(wgPlayer.getPlayer());
            });
        }
        return players;
    }

    /**
     * Receives a {@link List} of players that are in a specific region via {@link ProtectedRegion}.
     * @param protectedRegion {@link ProtectedRegion} to be checked
     * @return List of {@link Player Players}
     */
    public List<Player> getPlayersInRegion(ProtectedRegion protectedRegion) {
        final List<Player> players = new ArrayList<>();
        for(WgPlayer wgPlayer : WgRegionEvents.getInstance().getPlayerCache().values()) {
            if(wgPlayer.getRegions().contains(protectedRegion)) {
                players.add(wgPlayer.getPlayer());
            }
        }
        return players;
    }
}

