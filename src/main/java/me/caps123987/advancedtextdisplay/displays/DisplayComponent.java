package me.caps123987.advancedtextdisplay.displays;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.function.BiConsumer;

import static me.caps123987.advancedtextdisplay.AdvancedTextDisplay.PLUGIN_INSTANCE;

public class DisplayComponent {
    private TextDisplay componentDisplay;
    private Interaction componentInteraction;

    private Vector relativePosition;

    private BiConsumer<DisplayComponent,PlayerInteractAtEntityEvent> callback;

    private InteractiveDisplay parentDisplay;

    public DisplayComponent(Vector relativePosition) {
        this.relativePosition = relativePosition;
    }

    public void create(){
        //TODO create spawn + init script
        UUID interactionUUID = componentInteraction.getUniqueId();

        PLUGIN_INSTANCE.getInteractListener().registerComponent(interactionUUID, this);
    }
    public void setParentDisplay(InteractiveDisplay parentDisplay) {
        this.parentDisplay = parentDisplay;
    }
    public void click(PlayerInteractAtEntityEvent event) {
        callback.accept(this,event);
    }
    public TextDisplay getComponentDisplay() {
        return componentDisplay;
    }
    public void setComponentDisplay(TextDisplay componentDisplay) {
        this.componentDisplay = componentDisplay;
    }
    public Interaction getComponentInteraction() {
        return componentInteraction;
    }
    public void setComponentInteraction(Interaction componentInteraction) {
        this.componentInteraction = componentInteraction;
    }
    public void setCallback(BiConsumer<DisplayComponent, PlayerInteractAtEntityEvent> callback) {
        this.callback = callback;
    }
    public BiConsumer<DisplayComponent, PlayerInteractAtEntityEvent> getCallback() {
        return callback;
    }
    public InteractiveDisplay getParentDisplay() {
        return parentDisplay;
    }
}
