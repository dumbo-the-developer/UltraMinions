package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        return true;
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        return true;
    }

}