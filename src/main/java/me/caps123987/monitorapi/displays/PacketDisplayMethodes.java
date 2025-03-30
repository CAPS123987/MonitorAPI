package me.caps123987.monitorapi.displays;

import me.caps123987.monitorapi.registry.DisplaysRegistry;
import me.caps123987.monitorapi.utility.Packets;
import me.caps123987.monitorapi.utility.TextUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.caps123987.monitorapi.registry.DisplaysRegistry.allSharedDisplays;

public interface PacketDisplayMethodes {
    TextDisplay getDisplay();
    Component getHeader();
    RenderMode getRenderMode();
    boolean isSpawned();
    boolean hasHeader();
    InteractiveDisplay getInteractiveDisplay();

    Map<UUID,TextDisplay> getPlayersDisplays();
    Set<UUID> getViewers();

    BiConsumer<TextDisplay,UUID> getOnSpawnCallback();

    void setSpawned(boolean spawned);
    void setDisplay(TextDisplay display);

    /**
     * this method is usualy called after {@link DisplayComponent#init} <br>
     * it sets up everything registry, location, packets etc. <br>
     * should not be called from outside
     * @param location
     */
    default void setup(Location location){
        setSpawned(true);

        getDisplay().setRotation(location.getYaw(), location.getPitch());
        getDisplay().teleportAsync(location);

        if(getRenderMode().isSharedDisplay()) {
            setDisplay((TextDisplay) getDisplay().createSnapshot().createEntity(location));
        }else{
            if(getRenderMode().isForAllPlayers()){
                getPlayersDisplays().putAll(Packets.spawnTextDisplay(getDisplay(),getOnSpawnCallback()));


                allSharedDisplays.add(getInteractiveDisplay());
            }else{
                getPlayersDisplays().putAll(Packets.spawnTextDisplay(getDisplay(), getViewers(),getOnSpawnCallback()));


                for(Map.Entry<UUID,TextDisplay> entry:getPlayersDisplays().entrySet()){
                    getViewers().add(entry.getKey());
                }
            }
        }
        DisplaysRegistry.displayEntity.add(getDisplay().getUniqueId());//TODO check this
    }

    /**
     * if the more is individual it sets something to the players display example:
     * <pre>
     * {@code
     * this.setChangeToOnePlayer((display) ->{
     *  display.setAlignment(TextDisplay.TextAlignment.CENTER);
     * }
     * }
     * </pre>
     *
     * @param callback Consumer that return display
     * @param uuid player
     */
    default void setChangeToOnePlayer(Consumer<TextDisplay> callback, UUID uuid){
        if(!isSpawned()) return;
        callback.accept(getPlayersDisplays().get(uuid));
        updatePlayer(uuid);
    }

    /**
     * it sets something to the all players display example:
     * <pre>
     * {@code
     * this.setChangeToAllPlayers((display) ->{
     *  display.setAlignment(TextDisplay.TextAlignment.CENTER);
     * }
     * }
     * </pre>
     *
     * @param callback Consumer that return display
     */
    default void setChangeToAllPlayers(Consumer<TextDisplay> callback){
        callback.accept(getDisplay());
        if(!isSpawned()) return;
        if(!getRenderMode().isSharedDisplay()){
            for(Map.Entry<UUID,TextDisplay> entry:getPlayersDisplays().entrySet()){
                callback.accept(entry.getValue());
                updatePlayer(entry.getKey(),entry.getValue());
            }
        }else {
            updateAll();
        }
    }
    /**
     * it sets something to the all players display example:
     * <pre>
     * {@code
     * this.setChangeToAllPlayers((display, uuid) ->{
     *  display.setText(Component.text("Your name is: "+getPlayer(uuid).getName()))
     * }
     * }
     * </pre>
     *
     * @param callback Consumer that return display
     */
    default void setChangeToAllPlayers(BiConsumer<TextDisplay,UUID> callback){
        callback.accept(getDisplay(),null);
        if(!isSpawned()) return;
        if(!getRenderMode().isSharedDisplay()){
            for(Map.Entry<UUID,TextDisplay> entry:getPlayersDisplays().entrySet()){
                callback.accept(entry.getValue(),entry.getKey());
                updatePlayer(entry.getKey(),entry.getValue());
            }
        }else {
            updateAll();
        }
    }


    /**
     * updates players display when mode is individual (sends packet)
     * @param uuid
     */
    default void updatePlayer(UUID uuid){
        updatePlayer(uuid,getPlayersDisplays().get(uuid));
    }
    /**
     * updates player with given display when mode is individual (sends packet) <br>
     * should not be used
     * @param uuid
     * @param display
     */
    default void updatePlayer(UUID uuid,TextDisplay display){
        if(!isSpawned()) return;
        if(!getRenderMode().isSharedDisplay()) {
            Packets.updateOneTextDisplay(display,uuid);
        }
    }
    /**
     * updates display for allplayers when mode is individual (sends packet)
     */
    default void updateAll(){
        if(!isSpawned()) return;
        if(!getRenderMode().isSharedDisplay()) {
            if(getRenderMode().isForAllPlayers()){
                Packets.updateTextDisplayToAllPlayers(getDisplay());
            }else{
                Packets.updateTextDisplay(getDisplay(), getViewers());
            }
        }
    }

    /**
     * Sets text of all displays to string
     *
     * @param text string text
     */
    default void setDisplayText(String text) {
        Component out = TextUtility.getComponentFromString(text,hasHeader(),getHeader());
        setChangeToAllPlayers(display -> display.text(out));
    }
    /**
     * Sets text of the players display text to string
     *
     * @param text string text
     * @param uuid player
     */
    default void setDisplayText(String text, UUID uuid) {
        Component out = TextUtility.getComponentFromString(text,hasHeader(),getHeader());
        setChangeToOnePlayer(display -> display.text(out),uuid);
    }
    /**
     * Sets text of all displays to list of string each representing new line
     *
     * @param text list of string
     */
    default void setDisplayTextLines(List<String> text)  {
        Component out = TextUtility.getComponentFromStringLines(text,hasHeader(),getHeader());
        setChangeToAllPlayers(display -> display.text(out));
    }
    /**
     * Sets text of players display to list of string each representing new line
     *
     * @param text list of string
     */
    default void setDisplayTextLines(List<String> text, UUID uuid)  {
        Component out = TextUtility.getComponentFromStringLines(text,hasHeader(),getHeader());
        setChangeToOnePlayer(display -> display.text(out),uuid);
    }
    /**
     * Sets text of all text displays to component
     *
     * @param text component
     */
    default void setDisplayText(Component text) {
        Component out = TextUtility.getComponentFromComponent(text,hasHeader(),getHeader());
        setChangeToAllPlayers(display -> display.text(out));
    }
    /**
     * Sets text of players text display to component
     *
     * @param text component
     */
    default void setDisplayText(Component text, UUID uuid) {
        Component out = TextUtility.getComponentFromComponent(text,hasHeader(),getHeader());
        setChangeToOnePlayer(display -> display.text(out),uuid);
    }
    /**
     * Sets text of all text displays to list of components each representing new line
     *
     * @param text list of components
     */
    default void setDisplayText(List<Component> text)  {
        Component out = TextUtility.getComponentFromComponentLines(text,hasHeader(),getHeader());
        setChangeToAllPlayers(display -> display.text(out));
    }
    /**
     * Sets text of playes text display to list of components each representing new line
     *
     * @param text list of components
     */
    default void setDisplayText(List<Component> text, UUID uuid)  {
        Component out = TextUtility.getComponentFromComponentLines(text,hasHeader(),getHeader());
        setChangeToOnePlayer(display -> display.text(out),uuid);
    }

    /**
     * Sets billboard property of all text displays <br>
     * same as {@link #getDisplay }.setBillboard()<br><br>
     *
     * @param billboard billboard
     */
    default void setBillboard(Display.Billboard billboard) {
        setChangeToAllPlayers(display -> display.setBillboard(billboard));
    }

    /**
     * Sets billboard property of the players display <br>
     *
     * @param billboard billboard
     * @param uuid player's uuid
     */
    default void setBillboard(Display.Billboard billboard, UUID uuid){
        setChangeToOnePlayer(display -> display.setBillboard(billboard),uuid);
    }

    /**
     * Sets alignment property of all text displays <br>
     * same as {@link #getDisplay() getMainDisplay()}.setAlignment()
     *
     * @param alignment aligment
     */
    default void setAlignment(TextDisplay.TextAlignment alignment) {
        setChangeToAllPlayers(display -> display.setAlignment(alignment));
    }
    /**
     * Sets alignment property of the players display <br>
     *
     * @param alignment aligment
     * @param uuid player's uuid
     */
    default void setAlignment(TextDisplay.TextAlignment alignment,UUID uuid) {
        setChangeToOnePlayer(display -> display.setAlignment(alignment),uuid);
    }

    default void spawnDisplayNewPlayer(UUID uuid){
        if(getRenderMode().isForAllPlayers()) {
            getPlayersDisplays().putAll(Packets.spawnTextDisplay(getDisplay(),Set.of(uuid),getOnSpawnCallback()));
        }
    }
}
