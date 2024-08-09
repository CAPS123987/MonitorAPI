package me.caps123987.monitorapi.listeners;

import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.registry.DisplaysRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

import static me.caps123987.monitorapi.MonitorAPI.PLUGIN_INSTANCE;

public class WorldChange implements Listener {
    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent e) {
        spawnAll(e.getPlayer());
    }

    public static void spawnAll(Player player) {
        UUID uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskLater(PLUGIN_INSTANCE, () -> {
            for(InteractiveDisplay display : DisplaysRegistry.allSharedDisplays) {
                display.getViewers().add(uuid);
                display.spawnDisplayNewPlayer(uuid);
            }
        }, 10);
    }
}
