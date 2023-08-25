package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.Leonardo0013YT.UltraMinions.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ProtectionStonesAddon {

    Plugin wdp;
    private Main plugin;

    public ProtectionStonesAddon(Main plugin) {
        this.plugin = plugin;
        wdp = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    public boolean checkRegion(Player p, Location pb) {
        WorldGuardPlugin wg = (WorldGuardPlugin) wdp;
        String psx = Double.toString(pb.getX());
        String psy = Double.toString(pb.getY());
        String psz = Double.toString(pb.getZ());
        String id = "ps" + psx.substring(0, psx.indexOf(".")) + "x" + psy.substring(0, psy.indexOf(".")) + "y" + psz.substring(0, psz.indexOf(".")) + "z";

        return false;
    }

}