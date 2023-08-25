package io.github.Leonardo0013YT.UltraMinions.enums;

import org.bukkit.Material;

import java.util.Arrays;

public enum MinionType {

    FARMER(Material.WOOD_HOE, 3),
    FISHER(Material.FISHING_ROD, 2),
    LUMBERJACK(Material.WOOD_AXE, 3, Material.LOG, Material.LOG_2),
    MINER(Material.WOOD_PICKAXE, 2),
    RANCHER(Material.WOOD_SWORD, 2),
    HUNTER(Material.WOOD_SWORD, 2),
    PEASANT(Material.WOOD_HOE, 3, Material.MELON, Material.PUMPKIN),
    CACTUSCANE(Material.WOOD_HOE, 3, Material.CACTUS, Material.SUGAR_CANE),
    COLLECTOR(Material.CHEST, 1),
    SELLER(Material.GOLD_NUGGET, 1);

    private Material[] works;
    private int actions;
    private Material handItem;

    MinionType(Material handItem, int actions) {
        this.handItem = handItem;
        this.works = new Material[0];
        this.actions = actions;
    }

    MinionType(Material handItem, int actions, Material... works) {
        this.handItem = handItem;
        this.works = works;
        this.actions = actions;
    }

    public Material getHandItem() {
        return handItem;
    }

    public int getActions() {
        return actions;
    }

    public String toString() {
        return Arrays.toString(works);
    }

    public boolean check(Material material) {
        return Arrays.asList(works).contains(material);
    }
}