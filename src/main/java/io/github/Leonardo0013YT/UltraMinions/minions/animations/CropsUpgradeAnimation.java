package io.github.Leonardo0013YT.UltraMinions.minions.animations;

import io.github.Leonardo0013YT.UltraMinions.interfaces.BlockAnimation;
import io.github.Leonardo0013YT.UltraMinions.utils.MinionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CropsUpgradeAnimation implements BlockAnimation {

    private Block block;
    private boolean finished = false, execute = false;

    public CropsUpgradeAnimation(Block block) {
        this.block = block;
    }

    @Override
    public void update() {
        if (block == null || block.getType().equals(Material.AIR)) {
            return;
        }
        execute = !execute;
        if (!execute) return;
        if (MinionUtils.isMax(block.getType().name(), block.getData()) || block.getData() >= 7) {
            finished = true;
            return;
        }
        if ((byte) (block.getData() + 1) > 7) {
            finished = true;
            return;
        }
        block.setData((byte) (block.getData() + 1));
        block.getState().update();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}