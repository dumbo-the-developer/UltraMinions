package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraMinions.utils.MinionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MinionLumberjack extends Minion {

    private List<Vector> woods = Arrays.asList(new Vector(2, 0, 2), new Vector(-2, 0, 2), new Vector(2, 0, -2), new Vector(-2, 0, -2), new Vector(2, 0, 0), new Vector(0, 0, 2), new Vector(-2, 0, 0), new Vector(0, 0, -2));

    public MinionLumberjack(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        ItemStack material = getPlace();
        if (armor == null || pm == null) return;
        Location l = pm.getSelected("SAPPLING");
        if (l != null) {
            if (!plugin.getAdm().isStackable(l)) {
                armor.setItemInHand(new ItemStack(Material.SAPLING, 1, material.getData().getData()));
                l.getBlock().setType(Material.SAPLING);
                l.getBlock().setData(material.getData().getData());
                Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, l.getBlock()));
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("BUILD");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    armor.setItemInHand(ItemBuilder.item(Material.INK_SACK, 1, (short) 15, "", ""));
                    buildTree(lo, (material.getType().equals(Material.LOG_2) ? material.getData().getData() + 4 : material.getData().getData()));
                    stat.setGenerated(stat.getGenerated() + 3);
                }
                action.done(false);
            } else {
                armor.setItemInHand(getHandItem());
                Location de = pm.getSelected("DESTROY");
                if (de != null) {
                    if (!plugin.getAdm().isStackable(de)) {
                        destroyTree(de);
                        MinionUtils.damageBlock(de.getBlock().getLocation(), -1);
                    }
                    action.done(true);
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : woods) {
            Location lo = spawn.clone().add(l.getX(), l.getY() - 1, l.getZ());
            Material t = lo.getBlock().getType();
            if (t.equals(Material.DIRT) || t.equals(Material.GRASS)) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkArroundWood(Location loc) {
        for (Vector l : woods) {
            Location lo1 = loc.clone().add(l.getX(), l.getY(), l.getZ());
            Location lo2 = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            Location lo3 = loc.clone().add(l.getX(), l.getY() + 2, l.getZ());
            if (lo1.getBlock().getType().name().contains("SAPLING")) {
                continue;
            }
            if (lo1.getBlock().getType().equals(Material.AIR) || lo2.getBlock().getType().equals(Material.AIR) || lo3.getBlock().getType().equals(Material.AIR)) {
                return lo1;
            }
        }
        return null;
    }

    public Location checkArroundSappling(Location loc) {
        for (Vector l : woods) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().name().contains("SAPLING")) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomWood(Location loc) {
        return loc.clone().add(woods.get(ThreadLocalRandom.current().nextInt(woods.size())));
    }

    public void destroyTree(Location loc) {
        Location t1 = loc.clone();
        Location t2 = loc.clone().add(0, 1, 0);
        Location t3 = loc.clone().add(0, 2, 0);
        t1.getBlock().setType(Material.AIR);
        t2.getBlock().setType(Material.AIR);
        t3.getBlock().setType(Material.AIR);
    }

    public void buildTree(Location loc, int id) {
        Location t1 = loc.clone();
        Location t2 = loc.clone().add(0, 1, 0);
        Location t3 = loc.clone().add(0, 2, 0);
        Location l1 = loc.clone().add(1, 2, 0);
        Location l2 = loc.clone().add(-1, 2, 0);
        Location l3 = loc.clone().add(0, 2, -1);
        Location l4 = loc.clone().add(0, 2, 1);
        Location l5 = loc.clone().add(0, 3, 0);
        Material m = (id == 4 || id == 5) ? Material.valueOf("LOG_2") : Material.valueOf("LOG");
        Material l = (id == 4 || id == 5) ? Material.valueOf("LEAVES_2") : Material.valueOf("LEAVES");
        byte d = (id == 4 || id == 5) ? (byte) id : (byte) (id - 4);
        t1.getBlock().setType(m);
        t1.getBlock().setData(d);
        t2.getBlock().setType(m);
        t2.getBlock().setData(d);
        t3.getBlock().setType(m);
        t3.getBlock().setData(d);
        l1.getBlock().setType(l);
        l1.getBlock().setData(d);
        l2.getBlock().setType(l);
        l2.getBlock().setData(d);
        l3.getBlock().setType(l);
        l3.getBlock().setData(d);
        l4.getBlock().setType(l);
        l4.getBlock().setData(d);
        l5.getBlock().setType(l);
        l5.getBlock().setData(d);
    }

}