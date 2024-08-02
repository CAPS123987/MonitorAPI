package me.caps123987.monitorapi.registry;

import me.caps123987.monitorapi.displays.DisplayButtonComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import org.bukkit.entity.TextDisplay;

import java.util.*;

public class DisplaysRegistry {
    public static final Map<UUID, DisplayButtonComponent> interactionEntity = new HashMap<>();
    public static final Set<InteractiveDisplay> allSharedDisplays = new HashSet<>();
    public static final Set<UUID> displayEntity = new HashSet<>();
}
