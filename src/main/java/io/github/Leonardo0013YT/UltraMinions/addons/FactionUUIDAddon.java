package io.github.Leonardo0013YT.UltraMinions.addons;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import io.github.Leonardo0013YT.UltraMinions.interfaces.ProtectionAddon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FactionUUIDAddon implements ProtectionAddon {

    @Override
    public boolean canBuild(Player p, Location loc) {
        Faction faction = Board.getInstance().getFactionAt(new FLocation(loc));
        if (faction == null) {
            return false;
        } else {
            boolean has = false;
            for (FPlayer fp : faction.getFPlayers()) {
                if (fp != null && fp.getPlayer() != null && fp.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                    has = true;
                    break;
                }
            }
            return has;
        }
    }

    @Override
    public boolean canBuild(Player p, Block b) {
        return canBuild(p, b.getLocation());
    }
}
