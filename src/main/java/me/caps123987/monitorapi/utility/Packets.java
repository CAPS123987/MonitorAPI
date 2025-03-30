package me.caps123987.monitorapi.utility;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import me.caps123987.monitorapi.MonitorAPI;
import me.tofaa.entitylib.meta.EntityMeta;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static me.caps123987.monitorapi.utility.Conversions.toQuaternion4f;
import static me.caps123987.monitorapi.utility.Conversions.toSusVector3f;
import static org.bukkit.Bukkit.getServer;

public class Packets {
    //https://github.com/Tofaa2/EntityLib?tab=readme-ov-file
    public static Map<UUID,TextDisplay> spawnTextDisplay(TextDisplay display){
        return spawnTextDisplay(display, (t,u)->{});
    }
    public static Map<UUID,TextDisplay> spawnTextDisplay(TextDisplay display, BiConsumer<TextDisplay,UUID> callback){
        return spawnTextDisplay(display, getServer().getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet()),callback);
    }
    public static Map<UUID,TextDisplay> spawnTextDisplay(TextDisplay display, Set<UUID> players){
        return spawnTextDisplay(display, players, (t,u)->{});
    }
    public static Map<UUID,TextDisplay> spawnTextDisplay(TextDisplay display, Set<UUID> players,BiConsumer<TextDisplay,UUID> callback){
        Map<UUID,TextDisplay> entityIds = new HashMap<>();

        for(UUID player : players){
            TextDisplay playerDisplay = (TextDisplay) display.createSnapshot().createEntity(display.getWorld());
            playerDisplay.teleportAsync(display.getLocation());
            spawnTextDisplay(playerDisplay, player,callback);
            entityIds.put(player, playerDisplay);
        }
        return entityIds;
    }
    public static void spawnTextDisplay(TextDisplay display, UUID player){
        spawnTextDisplay(display, display, player, (t,u)->{});
    }
    public static void spawnTextDisplay(TextDisplay display, UUID player,BiConsumer<TextDisplay,UUID> callback){
        spawnTextDisplay(display, display, player,callback);
    }
    public static void spawnTextDisplay(TextDisplay display, TextDisplay idDisplay, UUID player){
        spawnTextDisplay(display, idDisplay, player, (t,u)->{});
    }
    public static void spawnTextDisplay(TextDisplay display, TextDisplay idDisplay, UUID player, BiConsumer<TextDisplay,UUID> callback){
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                idDisplay.getEntityId(),
                Optional.of(idDisplay.getUniqueId()),
                EntityTypes.TEXT_DISPLAY,
                new Vector3d(display.getLocation().getX(), display.getLocation().getY(), display.getLocation().getZ()),
                display.getPitch(),
                display.getYaw(),
                0f,
                0,
                Optional.empty()
        );

        sendPacket(packet,player);
        TextDisplayMeta meta = createTextDisplayMeta(display);
        Bukkit.getScheduler().runTaskLater(MonitorAPI.PLUGIN_INSTANCE, () -> {
            sendPacket(meta.createPacket(), player);
            callback.accept(display,player);
        },2);
    }
    public static void sendPacket(PacketWrapper<?> packet, UUID uuid){
        Player player = getServer().getPlayer(uuid);
        if(player==null){
            return;
        }
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
    public static TextDisplayMeta createTextDisplayMeta(TextDisplay display){
        TextDisplayMeta meta = (TextDisplayMeta) EntityMeta.createMeta(display.getEntityId(), EntityTypes.TEXT_DISPLAY);
        meta.setText(display.text());
        meta.setShadow(display.isShadowed());
        meta.setBillboardConstraints(AbstractDisplayMeta.BillboardConstraints.valueOf(display.getBillboard().name()));
        meta.setAlignLeft(display.getAlignment().equals(TextDisplay.TextAlignment.LEFT));
        meta.setAlignRight(display.getAlignment().equals(TextDisplay.TextAlignment.RIGHT));
        meta.setInterpolationDelay(display.getInterpolationDelay());
        meta.setLineWidth(display.getLineWidth());
        meta.setSeeThrough(display.isSeeThrough());
        meta.setViewRange(display.getViewRange());
        meta.setScale(toSusVector3f(display.getTransformation().getScale()));
        meta.setTranslation(toSusVector3f(display.getTransformation().getTranslation()));
        meta.setLeftRotation(toQuaternion4f(display.getTransformation().getLeftRotation()));
        meta.setRightRotation(toQuaternion4f(display.getTransformation().getRightRotation()));
        meta.setWidth((float) display.getWidth());
        meta.setHeight((float) display.getHeight());

        return meta;
    }
    public static void updateTextDisplayToAllPlayers(TextDisplay display){
        updateTextDisplay(display, getServer().getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet()));
    }
    public static void updateTextDisplay(TextDisplay display, Set<UUID> players){
        for(UUID player : players){
            updateOneTextDisplay(display, player);
        }
    }
    public static void updateOneTextDisplay(TextDisplay display, UUID player){
        TextDisplayMeta meta = createTextDisplayMeta(display);
        sendPacket(meta.createPacket(), player);
    }
}
