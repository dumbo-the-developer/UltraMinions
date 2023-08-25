package io.github.Leonardo0013YT.UltraMinions.minions.types;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionBreakEvent;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionPlaceEvent;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.utils.MinionUtils;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MinionFarmer extends Minion {

    private ArrayList<Vector> farmers = new ArrayList<>(Arrays.asList(new Vector(1, -1, 0), new Vector(0, -1, 1), new Vector(1, -1, 1), new Vector(-1, -1, 0), new Vector(0, -1, -1), new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(-1, -1, 1), new Vector(2, -1, 0), new Vector(2, -1, -1), new Vector(2, -1, 1), new Vector(-2, -1, 0), new Vector(-2, -1, -1), new Vector(-2, -1, 1), new Vector(0, -1, 2), new Vector(1, -1, 2), new Vector(-1, -1, 2), new Vector(0, -1, -2), new Vector(1, -1, -2), new Vector(-1, -1, -2), new Vector(2, -1, -2), new Vector(2, -1, 2), new Vector(-2, -1, -2), new Vector(-2, -1, 2)));

    public MinionFarmer(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
    }

    @Override
    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        ItemStack material = getPlace();
        Location l = pm.getSelected("DIRT");
        if (material.getType().equals(Material.NETHER_WARTS)) {
            l = null;
        }
        if (l != null) {
            if (!plugin.getAdm().isStackable(l) || material.getType().equals(Material.NETHER_WARTS)) {
                armor.setItemInHand(getHandItem());
                l.getBlock().setType(Material.SOIL);
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("PRODUCT");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    if (material.getType().equals(Material.SEEDS)) {
                        lo.getBlock().setType(Material.CROPS);
                        Crops crop = (Crops) lo.getBlock().getState().getData();
                        crop.setState(CropState.VERY_SMALL);
                        armor.setItemInHand(new ItemStack(material));
                    } else if (material.getType().equals(Material.CARROT) || material.getType().equals(Material.POTATO)) {
                        lo.getBlock().setType(material.getType());
                        armor.setItemInHand(new ItemStack(Material.valueOf(material.getType().name() + "_ITEM")));
                    } else if (material.getType().equals(Material.CARROT_ITEM) || material.getType().equals(Material.CARROT_ITEM)) {
                        lo.getBlock().setType(Material.valueOf(material.getType().name().split("_")[0]));
                        armor.setItemInHand(new ItemStack(material.getType()));
                    } else if (material.getType().equals(Material.NETHER_WARTS)) {
                        lo.getBlock().setType(Material.NETHER_WARTS);
                    } else {
                        lo.getBlock().setType(material.getType());
                        armor.setItemInHand(new ItemStack(material));
                    }
                    stat.setGenerated(stat.getGenerated() + 1);
                    Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, lo.getBlock()));
                }
                action.done(false);
            } else {
                Location mt = pm.getSelected("FARMER");
                if (mt != null && MinionUtils.isMax(mt.getBlock().getType().name(), mt.getBlock().getData())) {
                    mt = null;
                }
                if (mt != null) {
                    if (!plugin.getAdm().isStackable(mt)) {
                        armor.setItemInHand(new ItemStack(Material.INK_SACK, 1, (short) 15));
                        if (!material.getType().equals(Material.NETHER_WARTS) && !material.getType().equals(Material.SEEDS) && !material.getType().equals(Material.CARROT) && !material.getType().equals(Material.POTATO)) {
                            mt.getBlock().setType(material.getType());
                        } else {
                            if (material.getType().equals(Material.NETHER_WARTS)) {
                                mt.getBlock().setData((byte) 3);
                            } else {
                                mt.getBlock().setData((byte) 7);
                            }
                            mt.getBlock().getState().update();
                        }
                    }
                    action.done(false);
                } else {
                    if (material.getType().equals(Material.CARROT) || material.getType().equals(Material.POTATO)) {
                        armor.setItemInHand(new ItemStack(Material.valueOf(material.getType().name() + "_ITEM")));
                    } else if (material.getType().equals(Material.CARROT_ITEM) || material.getType().equals(Material.CARROT_ITEM)) {
                        armor.setItemInHand(new ItemStack(material.getType()));
                    } else if (material.getType().equals(Material.NETHER_WARTS)) {
                        armor.setItemInHand(new ItemStack(material.getType()));
                    } else {
                        armor.setItemInHand(material);
                    }
                    Location ll = pm.getSelected("READY");
                    Location mf = (ll == null) ? getArroundRandomReady(spawn) : ll;
                    if (mf != null) {
                        if (!plugin.getAdm().isStackable(mf)) {
                            mf.getBlock().setType(Material.AIR);
                            Bukkit.getServer().getPluginManager().callEvent(new MinionBreakEvent(pm, mf.getBlock()));
                        }
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
            Location lo = spawn.clone().add(l.getX(), l.getY(), l.getZ());
            Material t = lo.getBlock().getType();
            if (t.equals(Material.SOUL_SAND) || t.equals(Material.GRASS) || t.equals(Material.DIRT) || t.equals(Material.SOIL)) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkFamerDirt(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            Material t = lo.getBlock().getType();
            if (t.equals(Material.SOUL_SAND) || t.equals(Material.DIRT) || t.equals(Material.GRASS)) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomFarmer(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            Material t = lo.getBlock().getType();
            if (lo.getBlock().getType().equals(Material.NETHER_WARTS) && lo.getBlock().getData() != 3) {
                return lo;
            }
            if ((t.equals(Material.CROPS) || t.equals(Material.CARROT) || t.equals(Material.POTATO)) && lo.getBlock().getData() != 7) {
                return lo;
            }
        }
        return null;
    }

    public Location checkFamerProduct(Location loc) {
        for (Vector l : farmers) {
            Location lo = loc.clone().add(l.getX(), l.getY() + 1, l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
                return lo;
            }
        }
        return null;
    }

    public Location getArroundRandomReady(Location loc) {
        Location lo = loc.clone();
        lo.add(farmers.get(ThreadLocalRandom.current().nextInt(farmers.size())));
        return lo.clone().add(0, 1, 0);
    }

}