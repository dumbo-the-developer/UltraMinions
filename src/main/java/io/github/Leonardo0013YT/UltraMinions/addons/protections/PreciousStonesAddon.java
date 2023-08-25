package io.github.Leonardo0013YT.UltraMinions.addons.protections;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.api.IApi;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PreciousStonesAddon {

    private IApi api;

    public PreciousStonesAddon() {
        api = PreciousStones.API();
    }

    public boolean checkRegion(Player p, Location pb) {
        return api.canPlace(p, pb);
    }

}