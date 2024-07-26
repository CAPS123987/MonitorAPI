package me.caps123987.advancedtextdisplay.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;

import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.interactionEntity;

public class InteractListener implements Listener {

    @EventHandler
    public void interaction(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        UUID uuid = entity.getUniqueId();

        if(!interactionEntity.containsKey(uuid)) return;
        interactionEntity.get(uuid).callClick(event);
    }
}
