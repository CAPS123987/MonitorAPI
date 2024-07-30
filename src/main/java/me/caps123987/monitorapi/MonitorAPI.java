package me.caps123987.monitorapi;

import me.caps123987.monitorapi.listeners.InteractListener;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import static me.caps123987.monitorapi.registry.DisplaysRegistry.displayEntity;
import static me.caps123987.monitorapi.registry.DisplaysRegistry.interactionEntity;
import static org.bukkit.Bukkit.getEntity;

public final class MonitorAPI extends JavaPlugin {
    public static MonitorAPI PLUGIN_INSTANCE;

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
