package io.github.Leonardo0013YT.UltraMinions.addons;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.entity.Player;

public class PlotSquaredAddon {

    public boolean isAllowedPlot(Player p, org.bukkit.Location loc) {
        if (loc.getWorld() == null) {
            return false;
        }
        Location l = new Location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
        Plot plot = Plot.getPlot(l);
        if (plot == null) {
            return false;
        }
        return plot.isAdded(p.getUniqueId()) || plot.isOwner(p.getUniqueId());
    }

}