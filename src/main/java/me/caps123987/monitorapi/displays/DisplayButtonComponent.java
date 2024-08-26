package me.caps123987.monitorapi.displays;

import me.caps123987.monitorapi.messages.DisplayMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import static me.caps123987.monitorapi.MonitorAPI.PLUGIN_INSTANCE;
import static me.caps123987.monitorapi.registry.DisplaysRegistry.interactionEntity;
import static me.caps123987.monitorapi.utility.EntityUtility.createEntity;

public class DisplayButtonComponent extends DisplayComponent{
    protected TextDisplay componentDisplay;
    protected Interaction componentInteraction;

    protected BiConsumer<DisplayButtonComponent,PlayerInteractAtEntityEvent> callback;
    protected Set<UUID> hasPlayerCooldown;
    protected boolean cooldownEnabled = false;
    protected int cooldownTime;

    /**
     * creates new DisplayButtonComponent at relative position to the InteractiveDisplay (will be set after {@link InteractiveDisplay#addComponent(DisplayComponent)})
     * @param relativePosition relative position (0,-2,0 means 2 blocks under)
     */
    public DisplayButtonComponent(Vector relativePosition) {
        super(relativePosition);
        componentDisplay = createEntity(TextDisplay.class);
        componentInteraction = createEntity(Interaction.class);

        componentInteraction.setInteractionHeight(1.0f);
        componentInteraction.setInteractionWidth(1.0f);
    }

    /**
     * internal call to initiate this display component
     */
    public void init(float yaw, float pitch, Location spawnedLocation) {
        componentInteraction.setRotation(yaw, pitch);

        Location spawnInteract = interactiveDisplay.getLocation().clone().add(calculatedRelativePosition);

        spawnInteract.getChunk().load();

        spawnInteract.getNearbyEntities(.1,.1,.1).forEach(e->{
            if(e instanceof TextDisplay){
                e.remove();
            }
        });

        componentInteraction = (Interaction) componentInteraction.createSnapshot().createEntity(spawnInteract);

        setup(spawnedLocation);

        interactionEntity.put(componentInteraction.getUniqueId(),this);

        spawnInteract.getChunk().unload();
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
     * internal call to fire this display component {@link #onClick} with cooldown implementation of
     * custom message for the cooldown can be set by using {@link DisplayMessages}
     */
    public void callClick(PlayerInteractAtEntityEvent event) {
        if(cooldownEnabled){
            Player p = event.getPlayer();
            UUID uuid = p.getUniqueId();

            if(hasPlayerCooldown.contains(uuid)) {
                p.sendMessage(DisplayMessages.CLICK_COOLDOWN);
                return;
            }

            hasPlayerCooldown.add(uuid);
            Bukkit.getScheduler().runTaskLater(PLUGIN_INSTANCE,()-> hasPlayerCooldown.remove(uuid),cooldownTime);
        }
        callback.accept(this,event);
    }



    /**
     * gets interaction entity of this display component
     */
    public Interaction getComponentInteraction() {
        return componentInteraction;
    }

    /**
     * sets interaction of this display component should not be used
     */
    public void setComponentInteraction(Interaction componentInteraction) {
        this.componentInteraction = componentInteraction;
    }

    /**
     * this callback is fired after {@link #callClick} and you can use it for any interaction with this system
     *
     * @param callback BiConsumer that will be called after {@link #callClick}
     */
    public void onClick(BiConsumer<DisplayButtonComponent, PlayerInteractAtEntityEvent> callback) {
        this.callback = callback;
    }

    /**
     * gets the callback from {@link #onClick}
     */
    public BiConsumer<DisplayButtonComponent, PlayerInteractAtEntityEvent> getOnClick() {
        return callback;
    }

    /**
     * gets the relative vector <br>
     */
    public Vector getRelativePosition() {
        return relativePosition;
    }
    /**
     * gets the relative vector <br>
     * works only after {@link #init} that means after the InteractiveDisplay is created
     */
    public Vector getRotatedRelativePosition() {
        return calculatedRelativePosition;
    }
    /**
     * sets the relative vector (not rotated)
     *
     */
    public void setRelativePosition(Vector relativePosition) {
        this.relativePosition = relativePosition;
    }

    /**
     * enables the cooldown (default value 0 ticks) <br>
     * custom message can be set by using {@link DisplayMessages}
     **/
    public void enableCooldown(){
        cooldownEnabled = true;
        hasPlayerCooldown = new HashSet<>();
        cooldownTime = 0;
    }

    /**
     * sets the cooldown time to x ticks
     * custom message can be set by using {@link DisplayMessages}
     * @param ticks time in ticks
     */
    public void setCooldownTime(int ticks){
        cooldownTime = ticks;
    }
    /**
     * gets the cooldown time
     * custom message can be set by using {@link DisplayMessages}
     */
    public int getCooldownTime(){
        return cooldownTime;
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
