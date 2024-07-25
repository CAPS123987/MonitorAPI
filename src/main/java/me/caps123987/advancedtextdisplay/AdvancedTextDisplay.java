package me.caps123987.advancedtextdisplay;

import me.caps123987.advancedtextdisplay.listeners.InteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdvancedTextDisplay extends JavaPlugin {
    private InteractListener interactListener;
    public static AdvancedTextDisplay PLUGIN_INSTANCE;

    @Override
    public void onEnable() {
        PLUGIN_INSTANCE = this;
        interactListener = new InteractListener();
        getServer().getPluginManager().registerEvents(interactListener,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public InteractListener getInteractListener() {
        return interactListener;
    }
}
