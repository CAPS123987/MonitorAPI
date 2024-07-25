package me.caps123987.advancedtextdisplay.displays;

import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;

import java.util.HashSet;
import java.util.Set;

public class InteractiveDisplay {
    private TextDisplay mainDisplay;
    private final Set<DisplayComponent> components = new HashSet<>();

    public void addComponent(DisplayComponent component) {
        component.setParentDisplay(this);
        components.add(component);
    }

    public void create(Location location) {
        //TODO: Create spawn + init script
    }
}
