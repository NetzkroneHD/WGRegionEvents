package de.netzkronehd.wgregionevents.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.netzkronehd.wgregionevents.WgRegionEvents;
import de.netzkronehd.wgregionevents.objects.WgPlayer;
import de.netzkronehd.wgregionevents.events.*;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class WgRegionListener implements Listener {

    private final WgRegionEvents wg;

    public WgRegionListener(WgRegionEvents wg) {
        this.wg = wg;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent e) {
        if(WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer())) return;
        wg.getPlayerCache().remove(e.getPlayer().getUniqueId());
        wg.getPlayerCache().put(e.getPlayer().getUniqueId(), new WgPlayer(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent e) {
        if(WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer())) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        for (ProtectedRegion region : wp.getRegions()) {
            final RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            final RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            Bukkit.getPluginManager().callEvent(leaveEvent);
            Bukkit.getPluginManager().callEvent(leftEvent);
        }
        wp.getRegions().clear();
        wg.getPlayerCache().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        if(WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer())) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        for (ProtectedRegion region : wp.getRegions()) {
            final RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            final RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT, e, e.getPlayer().getLocation(), e.getPlayer().getLocation());
            Bukkit.getPluginManager().callEvent(leaveEvent);
            Bukkit.getPluginManager().callEvent(leftEvent);

        }
        wp.getRegions().clear();
        wg.getPlayerCache().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        if(e.isCancelled() || (WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer()))) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        e.setCancelled(wp.updateRegions(MovementWay.MOVE, e.getTo(), e.getFrom(), e));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerTeleportEvent e) {
        if(e.isCancelled() || (WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer()))) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        e.setCancelled(wp.updateRegions(MovementWay.TELEPORT, e.getTo(), e.getFrom(), e));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        if(WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer())) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        wp.updateRegions(MovementWay.SPAWN, e.getPlayer().getLocation(), e.getPlayer().getLocation(), e);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        if(WgRegionEvents.citizens && CitizensAPI.getNPCRegistry().isNPC(e.getPlayer())) return;
        final WgPlayer wp = wg.getPlayer(e.getPlayer().getUniqueId());
        if(wp == null) return;

        wp.updateRegions(MovementWay.SPAWN, e.getRespawnLocation(), e.getPlayer().getLocation(), e);
    }

}
