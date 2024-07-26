package me.caps123987.advancedtextdisplay.displays;

import me.caps123987.advancedtextdisplay.messages.DisplayMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import static me.caps123987.advancedtextdisplay.AdvancedTextDisplay.PLUGIN_INSTANCE;
import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.displayEntity;
import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.interactionEntity;
import static me.caps123987.advancedtextdisplay.utility.EntityUtility.createEntity;

public class DisplayComponent {
    private TextDisplay componentDisplay;
    private Interaction componentInteraction;

    private Vector relativePosition;

    private BiConsumer<DisplayComponent,PlayerInteractAtEntityEvent> callback;

    private InteractiveDisplay parentDisplay;
    private Set<UUID> hasPlayerCooldown;
    private boolean cooldownEnabled = false;
    private int cooldownTime;

    /**
     * Creates object of DisplayComponent, which you can set some data <br>
     *
     * @param relativePosition relative position of this display component to the {@link InteractiveDisplay } (parent display)
     */
    public DisplayComponent(Vector relativePosition) {
        this.relativePosition = relativePosition;
        componentDisplay = createEntity(TextDisplay.class);
        componentInteraction = createEntity(Interaction.class);

        componentInteraction.setInteractionHeight(1.0f);
        componentInteraction.setInteractionWidth(1.0f);
    }

    /**
     * internal call to initiate this display component
     */
    public void init(){
        float yaw = parentDisplay.getLocation().getYaw();
        float pitch = parentDisplay.getLocation().getPitch();

        updateRelativePosition(yaw,pitch);

        componentInteraction.setRotation(yaw, pitch);

        componentInteraction = (Interaction) componentInteraction.createSnapshot().createEntity(parentDisplay.getLocation().clone().add(relativePosition));

        componentDisplay.setRotation(yaw, pitch);
        componentDisplay = (TextDisplay) componentDisplay.createSnapshot().createEntity(parentDisplay.getLocation().clone().add(relativePosition));

        displayEntity.add(componentDisplay.getUniqueId());
        interactionEntity.put(componentInteraction.getUniqueId(),this);
    }

    /**
     * rotates the original vector according to the yaw and pitch
     * @param yaw yaw
     * @param pitch pitch
     */
    public void updateRelativePosition(double yaw, double pitch){
        relativePosition = relativePosition.rotateAroundAxis(new Vector(1,0,0),Math.toRadians(-pitch));

        relativePosition = relativePosition.rotateAroundAxis(new Vector(0,1,0),Math.toRadians(-yaw)).multiply(new Vector(-1,1,-1));
    }

    /**
     * sets text of this display component to string
     *
     * @param text string text
     */
    public void setDisplayText(String text) {
        componentDisplay.text(Component.text(text));
    }
    /**
     * sets text of this display component to component
     *
     * @param text component text
     */
    public void setDisplayText(Component text) {
        componentDisplay.text(text);
    }
    /**
     * Sets text of this display component to list of components each representing new line
     *
     * @param text list of components
     */
    public void setDisplayText(List<Component> text)  {
        Component builder = Component.text("");

        for(Component line : text){
            builder = builder.append(line).appendNewline();
        }

        componentDisplay.text(builder);
    }
    /**
     * Sets the parent display {@link InteractiveDisplay } <br>
     * NOT RECOMMENDED
     *
     * @param parentDisplay the parent Display
     */
    public void setParentDisplay(InteractiveDisplay parentDisplay) {
        this.parentDisplay = parentDisplay;
    }

    /**
     * internal call to fire this display component {@link #onClick} with cooldown implementation
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
     * gets text display of this display component
     *
     */
    public TextDisplay getComponentDisplay() {
        return componentDisplay;
    }

    /**
     * sets text display of this display component
     *
     */
    public void setComponentDisplay(TextDisplay componentDisplay) {
        this.componentDisplay = componentDisplay;
    }

    /**
     * gets interaction of this display component
     *
     */
    public Interaction getComponentInteraction() {
        return componentInteraction;
    }

    /**
     * sets interaction of this display component
     *
     */
    public void setComponentInteraction(Interaction componentInteraction) {
        this.componentInteraction = componentInteraction;
    }

    /**
     * this callback is fired after {@link #callClick} and you can use it for any interaction with this system
     *
     * @param callback BiConsumer that will be called after {@link #callClick}
     */
    public void onClick(BiConsumer<DisplayComponent, PlayerInteractAtEntityEvent> callback) {
        this.callback = callback;
    }

    /**
     * gets the callback from {@link #onClick}
     */
    public BiConsumer<DisplayComponent, PlayerInteractAtEntityEvent> getOnClick() {
        return callback;
    }

    /**
     * gets the {@link InteractiveDisplay} (parent of this display component)
     */
    public InteractiveDisplay getParentDisplay() {
        return parentDisplay;
    }
    /**
     * gets the relative vector <br>
     * before {@link #init()} not rotated vector (original) <br>
     * after {@link #init()} rotated vector
     */
    public Vector getRelativePosition() {
        return relativePosition;
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
}
