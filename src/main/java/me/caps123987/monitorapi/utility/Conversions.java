package me.caps123987.monitorapi.utility;

import com.github.retrooper.packetevents.util.Quaternion4f;
import org.joml.Quaternionf;

public class Conversions {
    public static com.github.retrooper.packetevents.protocol.world.Location toSusLocation(org.bukkit.Location location) {
        return new com.github.retrooper.packetevents.protocol.world.Location(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }
    public static com.github.retrooper.packetevents.util.Vector3f toSusVector3f(org.joml.Vector3f vector) {
        return new com.github.retrooper.packetevents.util.Vector3f(
                vector.x,
                vector.y,
                vector.z
        );
    }
    public static Quaternion4f toQuaternion4f(Quaternionf vector) {
        return new Quaternion4f(
                vector.x,
                vector.y,
                vector.z,
                vector.w
        );
    }
}
