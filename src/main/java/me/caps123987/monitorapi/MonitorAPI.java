package me.caps123987.monitorapi;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.caps123987.monitorapi.listeners.InteractListener;
import me.caps123987.monitorapi.listeners.JoinListener;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import static me.caps123987.monitorapi.registry.DisplaysRegistry.displayEntity;
import static me.caps123987.monitorapi.registry.DisplaysRegistry.interactionEntity;
import static org.bukkit.Bukkit.getEntity;

public final class MonitorAPI extends JavaPlugin {
    public static MonitorAPI PLUGIN_INSTANCE;
    public static PlayerManager PLAYER_MANAGER;

    @Override
    public void onLoad(){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(true)
                .checkForUpdates(true)
                .bStats(false)
                .debug(true);
        PacketEvents.getAPI().load();
    }
    @Override
    public void onEnable() {
        PLUGIN_INSTANCE = this;

        PacketEvents.getAPI().init();

        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        APIConfig settings = new APIConfig(PacketEvents.getAPI())
                .debugMode()
                .tickTickables()
                .usePlatformLogger();

        EntityLib.init(platform, settings);

        PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

        InteractListener interactListener = new InteractListener();
        JoinListener joinListener = new JoinListener();
        getServer().getPluginManager().registerEvents(interactListener,this);
        getServer().getPluginManager().registerEvents(joinListener,this);
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
        PacketEvents.getAPI().terminate();
    }

    public static PlayerManager getPlayerManager() {
        return PLAYER_MANAGER;
    }
}
