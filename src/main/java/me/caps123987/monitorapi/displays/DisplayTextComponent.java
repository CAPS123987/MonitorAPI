package me.caps123987.monitorapi.displays;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static me.caps123987.monitorapi.utility.EntityUtility.createEntity;

public class DisplayTextComponent extends DisplayComponent{
    protected final Map<UUID,TextDisplay> playersDisplays = new HashMap<>();
    protected TextDisplay componentDisplay;

    /**
     * creates new DisplayTextComponent at relative position to the InteractiveDisplay (will be set after {@link InteractiveDisplay#addComponent(DisplayComponent)})
     * @param relativePosition relative position (0,-2,0 means 2 blocks under)
     */
    public DisplayTextComponent(Vector relativePosition, World world) {
        super(relativePosition);
        componentDisplay = createEntity(TextDisplay.class, world);
    }

    /**
     * this method is internal don't use
     */
    @Override
    void init(float yaw, float pitch, Location spawnedLocation) {
        setup(spawnedLocation);
    }
    @Override
    public TextDisplay getDisplay() {
        return componentDisplay;
    }

    @Override
    public RenderMode getRenderMode() {
        return interactiveDisplay.getRenderMode();
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public Map<UUID, TextDisplay> getPlayersDisplays() {
        return playersDisplays;
    }

    @Override
    public Set<UUID> getViewers() {
        return interactiveDisplay.getViewers();
    }

    @Override
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    @Override
    public void setDisplay(TextDisplay display) {
        componentDisplay = display;
    }

    /**
     * not implemented for basic components
     * @return
     */
    @Override
    public Component getHeader() {
        return null;
    }
    /**
     * not implemented for basic components
     * @return
     */
    @Override
    public boolean hasHeader() {
        return false;
    }
}
