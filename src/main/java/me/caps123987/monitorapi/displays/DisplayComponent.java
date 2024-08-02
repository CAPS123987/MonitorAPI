package me.caps123987.monitorapi.displays;

import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class DisplayComponent implements PacketDisplayMethodes{
    protected Vector relativePosition;
    protected Vector calculatedRelativePosition;
    protected final Map<UUID, TextDisplay> playersDisplays = new HashMap<>();
    protected InteractiveDisplay interactiveDisplay;
    protected BiConsumer<TextDisplay, UUID> onSpawnCallback = (t,u)->{};

    protected boolean spawned = false;
    DisplayComponent(Vector relativePosition) {
        this.relativePosition = relativePosition.multiply(new Vector(-1,1,-1));
    }

    /**
     * internal call, fired after {@link InteractiveDisplay#create}, should not be used <br>
     * after this the {@link #init} is called
     */
    public void baseInit() {
        float yaw = interactiveDisplay.getLocation().getYaw();
        float pitch = interactiveDisplay.getLocation().getPitch();

        updateRelativePosition(yaw,pitch);

        Location spawnLoc = interactiveDisplay.getLocation().clone().add(calculatedRelativePosition);
        spawnLoc.setPitch(pitch);
        spawnLoc.setYaw(yaw);

        init(yaw,pitch,spawnLoc);
    }
    /**
     * internal call to initiate this display component
     */
    abstract void init(float yaw, float pitch, Location spawnedLocation);
    /**
     * rotates the original vector according to the yaw and pitch
     * @param yaw yaw
     * @param pitch pitch
     */
    public void updateRelativePosition(double yaw, double pitch){
        calculatedRelativePosition = relativePosition.rotateAroundAxis(new Vector(1,0,0),Math.toRadians(-pitch));

        calculatedRelativePosition = calculatedRelativePosition.rotateAroundAxis(new Vector(0,1,0),Math.toRadians(-yaw)).multiply(new Vector(-1,1,-1));
    }

    /**
     * Sets the parent display {@link InteractiveDisplay } <br>
     * NOT RECOMMENDED
     *
     * @param interactiveDisplay the parent Display
     */
    public void setInteractiveDisplay(InteractiveDisplay interactiveDisplay) {
        this.interactiveDisplay = interactiveDisplay;
    }
    /**
     * gets the {@link InteractiveDisplay} (parent of this display component)
     */
    public InteractiveDisplay getInteractiveDisplay() {
        return interactiveDisplay;
    }
    /**
     * gets callback when textdisplay for new player spawns
     */
    public BiConsumer<TextDisplay,UUID> getOnSpawnCallback(){
        return onSpawnCallback;
    }

    /**
     * sets callback when display is created for new player
     * @param callback
     */
    public void setOnSpawnCallback(BiConsumer<TextDisplay,UUID> callback){
        onSpawnCallback = callback;
    }
}
