package io.github.Leonardo0013YT.UltraMinions.utils;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinionUtils {

    private static Constructor<?> breaking, block;
    private static Class<?> packet;
    private static String version;

    static {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            breaking = getNMSClass("PacketPlayOutBlockBreakAnimation").getConstructor(int.class, getNMSClass("BlockPosition"), int.class);
            block = getNMSClass("BlockPosition").getConstructor(double.class, double.class, double.class);
            packet = getNMSClass("Packet");
        } catch (Exception ignored) {
        }
    }

    public static void damageBlock(Location l, int damage) {
        try {
            Object packet = breaking.newInstance(0, block.newInstance(l.getX(), l.getY(), l.getZ()), damage);
            for (Entity ent : l.getWorld().getNearbyEntities(l, 4, 4, 4)) {
                if (!(ent instanceof Player)) continue;
                Player p = (Player) ent;
                sendPacket(p, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object object) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", packet).invoke(connection, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMinionUUID(UUID uuid) {
        Main plugin = Main.get();
        List<String> uuids = plugin.getTemp().getListOrDefault("minionsData", new ArrayList<>());
        uuids.add(uuid.toString());
        plugin.getTemp().set("minionsData", uuids);
        plugin.getTemp().save();
    }

    public static boolean isMinionUUID(UUID uuid) {
        Main plugin = Main.get();
        List<String> uuids = plugin.getTemp().getListOrDefault("minionsData", new ArrayList<>());
        return uuids.contains(uuid.toString());
    }

    public static void removeMinionUUID(UUID uuid) {
        Main plugin = Main.get();
        List<String> uuids = plugin.getTemp().getListOrDefault("minionsData", new ArrayList<>());
        uuids.remove(uuid.toString());
        plugin.getTemp().set("minionsData", uuids);
        plugin.getTemp().save();
    }

    public static int getMaxSlots(PlayerMinion pm) {
        MinionLevel ml = pm.getMinionLevel();
        while (ml == null) {
            if (pm.getStat().getLevel() <= 1) return 1;
            pm.getStat().setLevel(pm.getStat().getLevel() - 1);
            ml = pm.getMinionLevel();
        }
        int slots = ml.getMax() / 64;
        if (ml.getMax() % 64 != 0) {
            slots++;
        }
        return slots;
    }

    public static boolean isMax(String name, int amount) {
        if (name.equals("NETHER_WARTS")) {
            return amount >= 3;
        }
        if (name.equals("CROPS") || name.equals("BEETROOTS") || name.equals("CARROT") || name.equals("POTATO") || name.equals("WHEAT")) {
            return amount >= 7;
        }
        if (name.equals("COCOA")) {
            return amount >= 2;
        }
        return false;
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }

}