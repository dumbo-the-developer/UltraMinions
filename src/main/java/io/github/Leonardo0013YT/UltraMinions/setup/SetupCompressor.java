package io.github.Leonardo0013YT.UltraMinions.setup;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetupCompressor {

    private Player p;
    private String name;
    private SetupCraft craft;
    private ItemStack result;
    private boolean isCraft;
    private int amount;

    public SetupCompressor(Player p, String name) {
        this.p = p;
        this.name = name;
        this.amount = 9;
        this.craft = new SetupCraft();
    }

    public void save(CallBackAPI<Boolean> backAPI) {
        Main plugin = Main.get();
        String pt = "upgrades.compressor." + name;
        plugin.getUpgrades().set(pt + ".name", name);
        plugin.getUpgrades().set(pt + ".amount", amount);
        plugin.getUpgrades().set(pt + ".isCraft", isCraft);
        plugin.getUpgrades().set(pt + ".result", result);
        backAPI.done(result != null);
        if (isCraft) {
            plugin.getUpgrades().set(pt + ".craft.permission", craft.getPermission());
            for (int i = 0; i < 9; i++) {
                plugin.getUpgrades().set(pt + ".craft.items." + i, craft.getMatrix()[i]);
            }
        }
        plugin.getUpgrades().save();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public SetupCraft getCraft() {
        return craft;
    }

    public void setCraft(boolean craft) {
        isCraft = craft;
    }

    public void setCraft(SetupCraft craft) {
        this.craft = craft;
    }

}