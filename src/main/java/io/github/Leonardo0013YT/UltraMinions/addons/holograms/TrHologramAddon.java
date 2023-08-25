package io.github.Leonardo0013YT.UltraMinions.addons.holograms;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.interfaces.HologramAddon;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.hologram.Hologram;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public class TrHologramAddon implements HologramAddon {

    private HashMap<PlayerMinion, Hologram> holograms = new HashMap<>();

    @Override
    public void createHologram(PlayerMinion id, Location spawn, List<String> lines) {
        Location loc = spawn.clone();
        Hologram h = TrHologramAPI.createHologram(Main.get(), String.valueOf(id), loc.clone().add(0, 1.3 + (lines.size() * 0.3), 0), lines);
        holograms.put(id, h);
    }

    @Override
    public void deleteHologram(PlayerMinion id) {
        if (holograms.containsKey(id)) {
            holograms.get(id).delete();
            holograms.remove(id);
        }
    }

    @Override
    public boolean hasHologram(PlayerMinion id) {
        return holograms.containsKey(id);
    }

    @Override
    public void delete() {
        for (Hologram h : holograms.values()) {
            h.destroyAll();
            h.delete();
        }
        holograms.clear();
    }

}