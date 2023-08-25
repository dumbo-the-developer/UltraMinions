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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MinionMiner extends Minion {

    private ItemStack place;
    private ArrayList<Vector> miners = new ArrayList<>(Arrays.asList(new Vector(1, -1, 0), new Vector(0, -1, 1), new Vector(1, -1, 1), new Vector(-1, -1, 0), new Vector(0, -1, -1), new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(-1, -1, 1), new Vector(2, -1, 0), new Vector(2, -1, -1), new Vector(2, -1, 1), new Vector(-2, -1, 0), new Vector(-2, -1, -1), new Vector(-2, -1, 1), new Vector(0, -1, 2), new Vector(1, -1, 2), new Vector(-1, -1, 2), new Vector(0, -1, -2), new Vector(1, -1, -2), new Vector(-1, -1, -2), new Vector(2, -1, -2), new Vector(2, -1, 2), new Vector(-2, -1, -2), new Vector(-2, -1, 2)));

    public MinionMiner(Main plugin, YamlConfiguration minion, String path, File f) {
        super(plugin, minion, path, f);
        this.place = getPlace();
    }

    public void update(PlayerMinion pm, ArmorStand armor, PlayerMinionStat stat, Location spawn, CallBackAPI<Boolean> action) {
        if (armor == null || pm == null) return;
        Location l = pm.getSelected("PLACE");
        if (l != null) {
            if (!plugin.getAdm().isStackable(l)) {
                l.getBlock().setType(getPlace().getType());
                l.getBlock().setData(Objects.requireNonNull(getPlace().getData()).getData());
                Bukkit.getServer().getPluginManager().callEvent(new MinionPlaceEvent(pm, l.getBlock()));
                stat.setGenerated(stat.getGenerated() + 1);
                armor.setItemInHand(getHandItem());
            }
            action.done(false);
        } else {
            Location lo = pm.getSelected("BREAK");
            if (lo != null) {
                if (!plugin.getAdm().isStackable(lo)) {
                    MinionUtils.damageBlock(lo, -1);
                    armor.setItemInHand(getPlace());
                    lo.getBlock().setType(Material.AIR);
                    Bukkit.getServer().getPluginManager().callEvent(new MinionBreakEvent(pm, lo.getBlock()));
                }
                action.done(true);
            }
        }
    }

    @Override
    public boolean check(Location spawn) {
        boolean yes = true;
        for (Vector l : miners) {
            Location lo = spawn.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo == null || lo.getBlock() == null) continue;
            Material t = lo.getBlock().getType();
            if (t.equals(Material.AIR) || (t.equals(place.getType()) && lo.getBlock().getData() == place.getData().getData())) {
                continue;
            }
            yes = false;
        }
        return yes;
    }

    public Location checkAround(Location loc) {
        for (Vector l : miners) {
            Location lo = loc.clone().add(l.getX(), l.getY(), l.getZ());
            if (lo.getBlock().getType().equals(Material.AIR)) {
                return lo.getBlock().getLocation();
            }
        }
        return null;
    }

    public Location getAroundRandom(Location loc) {
        Vector l = miners.get(ThreadLocalRandom.current().nextInt(miners.size()));
        return loc.clone().add(l.getX(), l.getY(), l.getZ()).getBlock().getLocation();
    }

}