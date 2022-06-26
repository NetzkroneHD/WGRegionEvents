package de.netzkronehd.wgregionevents;

import de.netzkronehd.wgregionevents.api.SimpleWorldGuardAPI;
import de.netzkronehd.wgregionevents.events.MovementWay;
import de.netzkronehd.wgregionevents.listener.WgRegionListener;
import de.netzkronehd.wgregionevents.objects.WgPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class WgRegionEvents extends JavaPlugin {

    private static WgRegionEvents instance;

    private HashMap<UUID, WgPlayer> playerCache;
    private SimpleWorldGuardAPI simpleWorldGuardAPI;

    @Override
    public void onLoad() {
        instance = this;
        playerCache = new HashMap<>();
        simpleWorldGuardAPI = new SimpleWorldGuardAPI();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new WgRegionListener(instance), instance);
    }

    @Override
    public void onDisable() {

    }

    /**
     * Force updates the regions of the {@link org.bukkit.entity.Player Player}
     * @param player {@link Player} to be checked.
     * @param way The {@link MovementWay way} how the player moved.
     * @param to {@link org.bukkit.Location} where the {@link Player} will go.
     * @param from {@link org.bukkit.Location} where the {@link Player} came from.
     * @param parent If the {@link MovementWay} was caused by a {@link org.bukkit.event.player.PlayerEvent}
     * @return true if the operation was cancelled
     */
    public boolean updateRegions(Player player, MovementWay way, Location to, Location from, PlayerEvent parent) {
        return updateRegions(getPlayer(player.getUniqueId()), way, to, from, parent);
    }

    /**
     * Force updates the regions of the {@link WgPlayer Player}
     * @param player {@link WgPlayer} to be checked.
     * @param way The {@link MovementWay way} how the player moved.
     * @param to {@link org.bukkit.Location} where the {@link WgPlayer} will go.
     * @param from {@link org.bukkit.Location} where the {@link WgPlayer} came from.
     * @param parent If the {@link MovementWay} was caused by a {@link org.bukkit.event.player.PlayerEvent}
     * @return true if the operation was cancelled
     */
    public boolean updateRegions(WgPlayer player, MovementWay way, Location to, Location from, PlayerEvent parent) {
        Objects.requireNonNull(player, "WgPlayer 'player' can't be null");
        return player.updateRegions(way, to, from, parent);
    }

    public SimpleWorldGuardAPI getSimpleWorldGuardAPI() {
        return simpleWorldGuardAPI;
    }

    public WgPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }

    public HashMap<UUID, WgPlayer> getPlayerCache() {
        return playerCache;
    }

    public static WgRegionEvents getInstance() {
        return instance;
    }
}
