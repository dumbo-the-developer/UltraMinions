package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionBreakEvent;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
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

public class MinionPeasant extends Minion {

    private List<Vector> farmers = Arrays.asList(new Vector(2, 0, 2), new Vector(-2, 0, 2), new Vector(2, 0, -2), new Vector(-2, 0, -2), new Vector(2, 0, 0), new Vector(0, 0, 2), new Vector(-2, 0, 0), new Vector(0, 0, -2));
    private List<Vector> block = Arrays.asList(new Vector(3, 0, 2), new Vector(-3, 0, 2), new Vector(3, 0, -2), new Vector(-3, 0, -2), new Vector(3, 0, 0), new Vector(0, 0, 3), new Vector(-3, 0, 0), new Vector(0, 0, -3));

    public MinionPeasant(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        ItemStack material = getPlace();
        if (armor == null || pm == null) return;
        Location l = pm.getSelected("DIRT");
        if (l != null) {
            if (!plugin.getAdm().isStackable(l)) {
                armor.setItemInHand(getHandItem());
                l.getBlock().setType(Material.SOIL);
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("PRODUCT");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    if (material.getType().equals(Material.MELON)) {
                        lo.getBlock().setType(Material.MELON_STEM);
                    } else if (material.getType().equals(Material.PUMPKIN)) {
                        lo.getBlock().setType(Material.PUMPKIN_STEM);
                    }
                    stat.setGenerated(stat.getGenerated() + 1);
                    armor.setItemInHand(new ItemStack(material));
                    Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, lo.getBlock()));
                }
                action.done(true);
            } else {
                Location mt = pm.getSelected("FARMER");
                if (mt != null) {
                    if (!plugin.getAdm().isStackable(mt)) {
                        armor.setItemInHand(new ItemStack(Material.INK_SACK, 1, (short) 15));
                        mt.getBlock().setData((byte) 7);
                    }
                    action.done(false);
                } else {
                    Location bl = pm.getSelected("BLOCK");
                    if (bl != null) {
                        if (!plugin.getAdm().isStackable(bl)) {
                            armor.setItemInHand(new ItemStack(Material.INK_SACK, 1, (short) 15));
                            if (material.getType().equals(Material.MELON)) {
                                bl.getBlock().setType(Material.MELON_BLOCK);
                            } else if (material.getType().equals(Material.PUMPKIN)) {
                                bl.getBlock().setType(Material.PUMPKIN);
                            }
                        }
                        action.done(false);
                    } else {
                        armor.setItemInHand(new ItemStack(material));
                        pm.getSelected("READY");
                        List<Location> mf = getArroundRandomReady(spawn);
                        mf.forEach(bb -> bb.getBlock().setType(Material.AIR));
                        mf.forEach(bb -> Bukkit.getServer().getPluginManager().callEvent(new MinionBreakEvent(pm, bb.getBlock())));
                        action.done(true);
                    }
                }
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : farmers) {
            Location lo = spawn.clone().add(l.getX(), l.getY() - 1, l.getZ());
            if (lo.getBlock().getType().equals(Material.GRASS) || lo.getBlock().getType().equals(Material.DIRT) || lo.getBlock().getType().equals(Material.SOIL)) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkFamerDirt(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY() - 1, l.getZ());
            if (lo.getBlock().getType().equals(Material.DIRT) || lo.getBlock().getType().equals(Material.GRASS)) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomFarmer(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if ((lo.getBlock().getType().equals(Material.MELON_STEM) || lo.getBlock().getType().equals(Material.PUMPKIN_STEM)) && lo.getBlock().getData() != 7) {
                return lo;
            }
        }
        return null;
    }

    public Location checkFamerProduct(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
                return lo;
            }
        }
        return null;
    }

    public Location checkFarmerBlock(Location loc) {
        for (Vector l : block) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
                return lo;
            }
        }
        return null;
    }

    public List<Location> getArroundRandomReady(Location loc) {
        Location lo = loc.clone();
        Location lo1 = loc.clone();
        int selected = ThreadLocalRandom.current().nextInt(farmers.size());
        lo.add(farmers.get(selected));
        lo1.add(block.get(selected));
        return Arrays.asList(lo, lo1);
    }

}