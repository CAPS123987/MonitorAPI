package me.caps123987.monitorapi.displays;

import me.caps123987.monitorapi.utility.Packets;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.caps123987.monitorapi.utility.EntityUtility.createEntity;

/**
 * InteractiveDisplay is a class that allows creation of interactive displays <br>
 * It can be used to create displays with buttons, text, etc. using DisplayComponents <br>
 * RenderMode is used to determine how the display will be rendered. Meaning if all playes see it or some and if display is individual or one for all players <br>
 *
 */
public class InteractiveDisplay implements PacketDisplayMethodes{
    protected TextDisplay mainDisplay;
    protected final Set<DisplayComponent> components = new HashSet<>();
    protected final Map<String,Object> data = new HashMap<>();
    public final Set<UUID> viewers = new HashSet<>();
    protected final Map<UUID,TextDisplay> playersDisplays = new HashMap<>();

    protected BiConsumer<TextDisplay, UUID> onSpawnCallback = (t,u)->{};
    protected Location location;

    protected boolean hasHeader = false;
    protected boolean isSpawned = false;
    protected Component header = Component.text("");

    protected final RenderMode mode;

    /**
     * Creates new InteractiveDisplay but doesn't spawn it <br>
     *
     * @param mode render mode of the display (individual display, one display for all, etc.)
     */
    public InteractiveDisplay(RenderMode mode, World world) {
        this.mode = mode;
        mainDisplay = createEntity(TextDisplay.class, world);
        mainDisplay.setBillboard(Display.Billboard.FIXED);
        mainDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        mainDisplay.setRotation(0,0);
    }
    /**
     * Adds new component, usually button or text <br>
     * automatically sets the {@link DisplayButtonComponent#setInteractiveDisplay(InteractiveDisplay) interactive display} of the component
    **/
    public void addComponent(DisplayComponent component) {
        component.setInteractiveDisplay(this);
        components.add(component);
    }

    /**
     * Creates display + it's components <br>
     * Should be used only once (it would break the old one and create new functioning)
     *
     * @param location location where to spawn the display (yaw, pitch works)
     */
    public void create(Location location) {
        this.location = location;
        setup(location);

        for(DisplayComponent component : components){
            component.baseInit();
        }
        isSpawned = true;
    }

    /**
     * Sets custom data to this Display, type of Object, so you can use anything as data
     *
     * @param key data adress
     * @param data data as a Object
     */
    public void setData(String key, Object data) {
        this.data.put(key, data);
    }
    /**
     * gets custom data from this Display, type of Object, so you can use anything as data (cast)
     *
     * @param key data adress
     */
    public Object getData(String key) {
        return data.get(key);
    }
    /**
     * gets the main text display
     */
    public TextDisplay getDisplay() {
        return mainDisplay;
    }
    /**
     * gets the location of the main display  <br>
     * USE AFTER {@link #create(Location)} method
     */
    public Location getLocation() {
        return location;
    }

    /**
     * gets if the header is enabled
     */
    public boolean hasHeader() {
        return hasHeader;
    }
    /**
     * enables header, use to set the componet {@link #setHeader(Component)} <br>
     * manual update is needed after enabling like {@link #setDisplayText(Component)}
     */
    public void enableHeader(){
        hasHeader = true;
        updateAll();
    }
    /**
     * disables header
     * manual update is needed after enabling like {@link #setDisplayText(Component)}
     */
    public void disableHeader(){
        hasHeader = false; //TODO check if it works
        updateAll();
    }
    /**
     * sets header to the component and also {@link #enableHeader enables header} <br>
     * header is always the first line
     *
     * @param header component of the header
     */
    public void setHeader(Component header){
        this.header = header;
        hasHeader = true;
        updateAll();
    }

    /**
     * gets the header
     */
    public Component getHeader() {
        return header;
    }

    /**
     * @return render mode of the display
     */
    public RenderMode getRenderMode() {
        return mode;
    }

    /**
     * @return if the display is spawned
     */
    public boolean isSpawned(){
        return isSpawned;
    }

    /**
     * sets if the display is spawned <br>
     * should NOT be used
     * @param isSpawned
     */
    public void setSpawned(boolean isSpawned){
        this.isSpawned = isSpawned;
    }

    /**
     * gets map when mode is set to individual displays else null <br>
     * @return map where UUID is player and TextDisplay is his display, BUT this is like dataholder it doesn't update,
     * You SHOULDN'T use this please use {@link #setChangeToOnePlayer(Consumer, UUID)} for any changes
     */
    public Map<UUID,TextDisplay> getPlayersDisplays() {
        return playersDisplays;
    }

    /**
     * gets all viewers
     * @return viewers
     */
    public Set<UUID> getViewers(){
        return viewers;
    }

    @Override
    public BiConsumer<TextDisplay, UUID> getOnSpawnCallback() {
        return onSpawnCallback;
    }

    /**
     * sets callback when display is created for new player
     * @param callback
     */
    public void setOnSpawnCallback(BiConsumer<TextDisplay, UUID> callback) {
        this.onSpawnCallback = callback;
    }

    /**
     * sets the main display, if mode is shared then you can use this but if the mode is indivudual please use {@link #setChangeToOnePlayer(Consumer, UUID)}, {@link #setChangeToAllPlayers(Consumer)} or {@link #setChangeToAllPlayers(BiConsumer)} for any changes
     * @param display
     */
    public void setDisplay(TextDisplay display) {
        mainDisplay = display;
    }
    public InteractiveDisplay getInteractiveDisplay() {
        return this;
    }
    @Override
    public void spawnDisplayNewPlayer(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        if(player!=null){
            World world = player.getWorld();
            if(!world.equals(location.getWorld())){
                return;
            }
        }
        if(getRenderMode().isForAllPlayers()) {
            getPlayersDisplays().putAll(Packets.spawnTextDisplay(getDisplay(),Set.of(uuid),getOnSpawnCallback()));
        }
        for(DisplayComponent component : components){
            component.spawnDisplayNewPlayer(uuid);
        }
    }
}
