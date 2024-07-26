package me.caps123987.advancedtextdisplay.utility;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import static org.bukkit.Bukkit.getServer;

public class EntityUtility {
    public static <T extends Entity> T createEntity(Class<T> clazz) {
        return getServer().getWorlds().getFirst().createEntity(new Location(getServer().getWorlds().getFirst(),0,0,0), clazz);
    }
}
