package io.github.Leonardo0013YT.UltraMinions.utils;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionFace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static DecimalFormat df = new DecimalFormat("##.#");

    public static int getMaxPages(int size, int maxPerPage) {
        return size / Math.max(maxPerPage, 1) + 1;
    }

    public static void check(String path, Object value, YamlConfiguration minion, File f) {
        if (!minion.isSet(path)) {
            minion.set(path, value);
            try {
                minion.save(f);
            } catch (IOException ignored) {
            }
        }
    }

    public static String format(double value) {
        return df.format(value);
    }

    public static Location getStringLocation(String location) {
        String[] l = location.split(";");
        World world = Bukkit.getWorld(l[0]);
        double x = Double.parseDouble(l[1]);
        double y = Double.parseDouble(l[2]);
        double z = Double.parseDouble(l[3]);
        float yaw = Float.parseFloat(l[4]);
        float pitch = Float.parseFloat(l[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getLocationString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static String parseBoolean(boolean b) {
        return (b) ? Main.get().getLang().get("enabled") : Main.get().getLang().get("disabled");
    }

    public static String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 50)
            return "C";
        StringBuilder s = new StringBuilder();
        while (input >= 50) {
            s.append("L");
            input -= 50;
        }
        while (input >= 40) {
            s.append("XL");
            input -= 40;
        }
        while (input >= 10) {
            s.append("X");
            input -= 10;
        }
        while (input >= 9) {
            s.append("IX");
            input -= 9;
        }
        while (input >= 5) {
            s.append("V");
            input -= 5;
        }
        while (input >= 4) {
            s.append("IV");
            input -= 4;
        }
        while (input >= 1) {
            s.append("I");
            input -= 1;
        }
        return s.toString();
    }

    // Da el lugar contrario
    public static MinionFace getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw();
        if (0 <= rotation && rotation < 22.5) {
            return MinionFace.SOUTH; // NORTH
        } else if (22.5 <= rotation && rotation < 67.5) {
            return MinionFace.SOUTHWEST; // NORTHEAST
        } else if (67.5 <= rotation && rotation < 112.5) {
            return MinionFace.WEST; // EAST
        } else if (112.5 <= rotation && rotation < 157.5) {
            return MinionFace.NORTHWEST; // SOUTHEAST
        } else if (157.5 <= rotation && rotation < 202.5) {
            return MinionFace.NORTH; // SOUTH
        } else if (202.5 <= rotation && rotation < 247.5) {
            return MinionFace.NORTHEAST; // SOUTHWEST
        } else if (247.5 <= rotation && rotation < 292.5) {
            return MinionFace.EAST; // WEST
        } else if (292.5 <= rotation && rotation < 337.5) {
            return MinionFace.SOUTHEAST; // NORTHWEST
        } else /*(337.5 <= rotation && rotation < 360.0)*/ {
            return MinionFace.SOUTH; /* NORTH */
        }
    }

    public static String convertTime(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        String formattedTime = "";
        if (hours > 0) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + "h:";
        }
        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + "m:";
        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds + "s";
        return formattedTime;
    }

    public static String getFormatedLocation(Location loc) {
        if (loc == null) {
            return "§cNot set!";
        }
        return loc.getWorld().getName() + ", " + df.format(loc.getX()) + ", " + df.format(loc.getY()) + ", " + df.format(loc.getZ());
    }

    public static void addItems(Player p, HashMap<ItemStack, Integer> items, int max) {
        boolean drop = false;
        int gived = 0;
        for (Map.Entry<ItemStack, Integer> entry : items.entrySet()) {
            if (entry.getKey() == null || entry.getKey().getType().equals(Material.AIR)) continue;
            int stack = entry.getValue() / 64;
            int rest = entry.getValue() % 64;
            for (int i = 0; i < stack; i++) {
                ItemStack item = entry.getKey().clone();
                item.setAmount(64);
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                    drop = true;
                    continue;
                }
                p.getInventory().addItem(item);
            }
            if (rest > 0) {
                ItemStack add = entry.getKey().clone();
                add.setAmount(rest);
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItemNaturally(p.getLocation(), add);
                    drop = true;
                    continue;
                }
                p.getInventory().addItem(add);
            }
        }
        if (drop) {
            p.sendMessage(Main.get().getLang().get("messages.fullInventory"));
        }
    }

    public static String ObjectOrDefaultString(Object ob, String def) {
        if (ob == null) {
            return def;
        }
        return ob.toString();
    }

    public static void addItems(Player p, ItemStack... items) {
        boolean drop = false;
        for (ItemStack i : items) {
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItemNaturally(p.getLocation(), i);
                drop = true;
                continue;
            }
            p.getInventory().addItem(i);
        }
        if (drop) {
            p.sendMessage(Main.get().getLang().get("messages.fullInventory"));
        }
    }

    public static boolean isSimilar(ItemStack first, ItemStack second) {
        boolean similar = false;
        if (first == null || second == null) {
            return false;
        }
        boolean sameTypeId = (first.getTypeId() == second.getTypeId());
        boolean sameDurability = (first.getDurability() == second.getDurability());
        boolean sameAmount = (first.getAmount() == second.getAmount());
        boolean sameHasItemMeta = false;
        if ((first.hasItemMeta() && !second.hasItemMeta()) || (!first.hasItemMeta() && second.hasItemMeta()))
            return false;
        if (first.hasItemMeta() && second.hasItemMeta()) {
            sameHasItemMeta = checkBoolean(first.hasItemMeta(), second.hasItemMeta());
        }
        boolean sameEnchantments = (first.getEnchantments().equals(second.getEnchantments()));
        boolean sameItemMeta = true;
        if (sameHasItemMeta) {
            boolean sameHasDisplayName = checkBoolean(first.getItemMeta().hasDisplayName(), second.getItemMeta().hasDisplayName());
            boolean sameHasLore = checkBoolean(first.getItemMeta().hasLore(), second.getItemMeta().hasLore());
            if (sameHasDisplayName && sameHasLore) {
                sameItemMeta = first.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName()) && first.getItemMeta().getLore().equals(second.getItemMeta().getLore());
            } else if (sameHasDisplayName) {
                sameItemMeta = first.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName());
            } else if (sameHasLore) {
                sameItemMeta = first.getItemMeta().getLore().equals(second.getItemMeta().getLore());
            }
        }
        if (sameTypeId && sameDurability && sameAmount && sameEnchantments && sameItemMeta) {
            similar = true;
        }
        return similar;
    }

    static boolean checkBoolean(boolean b1, boolean b2) {
        if (!b1 && !b2) {
            return false;
        }
        return b1 == b2;
    }

}