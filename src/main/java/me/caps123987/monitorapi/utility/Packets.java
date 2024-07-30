package me.caps123987.monitorapi.utility;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import me.caps123987.monitorapi.MonitorAPI;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.meta.EntityMeta;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.*;

import static me.caps123987.monitorapi.utility.Conversions.toSusVector3f;
import static me.caps123987.monitorapi.utility.Conversions.toQuaternion4f;
import static org.bukkit.Bukkit.*;

public class Packets {
    //https://github.com/Tofaa2/EntityLib?tab=readme-ov-file
    public static void spawnTextDisplay(TextDisplay display){
        spawnTextDisplay(display, getServer().getOnlinePlayers().stream().map(Player::getUniqueId).toList());
    }
    public static void spawnTextDisplay(TextDisplay display, List<UUID> players){
        /*WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                display.getEntityId(),
                display.getUniqueId(),
                EntityTypes.TEXT_DISPLAY,
                Positions.toPapiLocation(display.getLocation()),
                display.getYaw(),
                0,
                new Vector3d()
        );*/
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                display.getEntityId(),
                Optional.of(UUID.randomUUID()),
                EntityTypes.TEXT_DISPLAY,
                new Vector3d(display.getLocation().getX(), display.getLocation().getY() + 1, display.getLocation().getZ()),
                display.getPitch(),
                display.getYaw(),
                0f,
                0,
                Optional.empty()
        );
        sendPacket(packet,players);
        TextDisplayMeta meta = createTextDisplayMeta(display);
        sendPacket(meta.createPacket(), players);
    }
    public static void sendPacket(PacketWrapper<?> packet, List<UUID> players){
        players.forEach(uuid -> {
            Player player = getServer().getPlayer(uuid);
            if(player==null){
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously(MonitorAPI.PLUGIN_INSTANCE, () -> {
                PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
            });
        });
    }
    public static TextDisplayMeta createTextDisplayMeta(TextDisplay display){
        Bukkit.broadcast(display.text());
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
        meta.setRightRotation(toQuaternion4f(display.getTransformation().getRightRotation()));
        meta.setRightRotation(toQuaternion4f(display.getTransformation().getRightRotation()));


        return meta;
    }
    public static void spawnPig(TextDisplay display, Location loc){
        /*WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                display.getEntityId(),
                display.getUniqueId(),
                EntityTypes.TEXT_DISPLAY,
                Positions.toPapiLocation(display.getLocation()),
                display.getYaw(),
                0,
                new Vector3d()
        );*/
        UUID uuid = UUID.randomUUID();
        int id = EntityLib.getPlatform().getEntityIdProvider().provide(uuid,EntityTypes.PIG);
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                id,
                Optional.of(uuid),
                EntityTypes.PIG,
                new Vector3d(loc.getX(), loc.getY() + 1, loc.getZ()),
                display.getPitch(),
                display.getYaw(),
                0f,
                0,
                Optional.empty()
        );
        sendPacket(packet,new ArrayList<>(Collections.singleton(getPlayer("CAPS123987").getUniqueId())));
    }
}
