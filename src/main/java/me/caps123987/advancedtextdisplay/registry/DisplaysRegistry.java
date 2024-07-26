package me.caps123987.advancedtextdisplay.registry;

import me.caps123987.advancedtextdisplay.displays.DisplayComponent;

import java.util.*;

public class DisplaysRegistry {
    public static final Map<UUID, DisplayComponent> interactionEntity = new HashMap<>();
    public static final Set<UUID> displayEntity = new HashSet<>();
}
