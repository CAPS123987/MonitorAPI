package me.caps123987.monitorapi.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityUtility {
    public static <T extends Entity> T createEntity(Class<T> clazz, World world) {
        return world.createEntity(new Location(world,0,0,0), clazz);
    }
}
