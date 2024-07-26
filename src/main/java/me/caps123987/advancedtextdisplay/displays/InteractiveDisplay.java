package me.caps123987.advancedtextdisplay.displays;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

import java.util.*;

import static me.caps123987.advancedtextdisplay.registry.DisplaysRegistry.displayEntity;
import static me.caps123987.advancedtextdisplay.utility.EntityUtility.createEntity;

public class InteractiveDisplay {
    private TextDisplay mainDisplay;
    private final Set<DisplayComponent> components = new HashSet<>();
    private final Map<String,Object> data = new HashMap<>();
    private Location location;

    private boolean hasHeader = false;
    private Component header = Component.text("");

    public InteractiveDisplay() {
        mainDisplay = createEntity(TextDisplay.class);
        mainDisplay.setBillboard(Display.Billboard.FIXED);
        mainDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        mainDisplay.setRotation(0,0);
    }
    /**
     * Adds new component, usually button <br>
     * automatically sets the {@link DisplayComponent#setParentDisplay(InteractiveDisplay) parent display}
    **/
    public void addComponent(DisplayComponent component) {
        component.setParentDisplay(this);
        components.add(component);
    }

    /**
     * Creates display + it's components <br>
     * Can be used only once (it would break the old one and create new functioning)
     *
     * @param location location where to spawn the display (yaw, pitch works)
     */
    public void create(Location location) {
        this.location = location;
        mainDisplay.setRotation(location.getYaw(), location.getPitch());
        mainDisplay = (TextDisplay) mainDisplay.createSnapshot().createEntity(location);
        displayEntity.add(mainDisplay.getUniqueId());

        for(DisplayComponent component : components){
            component.init();
        }
    }

    /**
     * Sets billboard property of the main display <br>
     * same as {@link #getMainDisplay() getMainDisplay()}.setBillboard()
     *
     * @param billboard billboard
     */
    public void setBillboard(Display.Billboard billboard) {
        mainDisplay.setBillboard(billboard);
    }

    /**
     * Sets alignment property of the main display <br>
     * same as {@link #getMainDisplay() getMainDisplay()}.setAlignment()
     *
     * @param alignment aligment
     */
    public void setAlignment(TextDisplay.TextAlignment alignment) {
        mainDisplay.setAlignment(alignment);
    }

    /**
     * Sets text of the main text to string
     *
     * @param text string text
     */
    public void setMainText(String text) {
        Component builder = Component.text("");
        if(hasHeader)builder = builder.append(header);
        builder = builder.append(Component.text(text));

        mainDisplay.text(builder);
    }
    /**
     * Sets text of the main text to component
     *
     * @param text component
     */
    public void setMainText(Component text) {
        Component builder = Component.text("");
        if(hasHeader) builder = builder.append(header);
        builder = builder.append(text);

        mainDisplay.text(builder);
    }
    /**
     * Sets text of the main text to list of components each representing new line
     *
     * @param text list of components
     */
    public void setMainText(List<Component> text)  {
        Component builder = Component.text("");

        if(hasHeader){
            builder = builder.append(header).appendNewline();
        }

        for(Component line : text){
            builder = builder.append(line).appendNewline();
        }

        mainDisplay.text(builder);
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
    public TextDisplay getMainDisplay() {
        return mainDisplay;
    }
    /**
     * sets the main text display
     */
    public void setMainDisplay(TextDisplay mainDisplay) {
        this.mainDisplay = mainDisplay;
    }

    /**
     * gets the location of the main display  <br>
     * USE AFTER {@link #create(Location)} methode
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
     * enables header, use to set the componet {@link #setHeader(Component)}
     */
    public void enableHeader(){
        hasHeader = true;
    }
    /**
     * disables header
     */
    public void disableHeader(){
        hasHeader = false;
    }
    /**
     * sets header to the component <br>
     * header is always the first line
     *
     * @param header component of the header
     */
    public void setHeader(Component header){
        this.header = header;
        hasHeader = true;
    }

    /**
     * gets the header
     */
    public Component getHeader() {
        return header;
    }
}
