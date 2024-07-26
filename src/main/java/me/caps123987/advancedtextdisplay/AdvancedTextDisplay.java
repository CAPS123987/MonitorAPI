package me.caps123987.advancedtextdisplay;

import me.caps123987.advancedtextdisplay.listeners.InteractListener;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.displayEntity;
import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.interactionEntity;
import static org.bukkit.Bukkit.getEntity;

public final class AdvancedTextDisplay extends JavaPlugin {
    public static AdvancedTextDisplay PLUGIN_INSTANCE;

    @Override
    public void onEnable() {
        PLUGIN_INSTANCE = this;
        InteractListener interactListener = new InteractListener();
        getServer().getPluginManager().registerEvents(interactListener,this);
    }

    @Override
    public void onDisable() {
        interactionEntity.keySet().forEach(uuid -> {
            Entity e = getEntity(uuid);
            if(e==null){
                return;
            }
            e.remove();
        });

        displayEntity.forEach(uuid -> {
            Entity e = getEntity(uuid);
            if(e==null){
                return;
            }
            e.remove();
        });
    }
}
