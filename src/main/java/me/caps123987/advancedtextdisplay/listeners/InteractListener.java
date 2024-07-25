package me.caps123987.advancedtextdisplay.listeners;

import me.caps123987.advancedtextdisplay.displays.DisplayComponent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractListener implements Listener {
    private final Map<UUID, DisplayComponent> registry = new HashMap<>();

    @EventHandler
    public void interaction(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        UUID uuid = entity.getUniqueId();
        if(!registry.containsKey(uuid)) return;
        registry.get(uuid).click(event);

    }

    public void registerComponent(UUID uuid, DisplayComponent component) {
        registry.put(uuid, component);
    }
}
