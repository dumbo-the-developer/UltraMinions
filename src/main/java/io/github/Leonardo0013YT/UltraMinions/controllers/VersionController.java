package io.github.Leonardo0013YT.UltraMinions.controllers;

import org.bukkit.Bukkit;

public class VersionController {

    private String version;
    private boolean is1_9to15 = false;
    private boolean is1_12 = false;

    public VersionController() {
        setupVersion();
    }

    private void setupVersion() {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            switch (version) {
                case "v1_9_R1":
                case "v1_9_R2":
                case "v1_10_R1":
                case "v1_11_R1":
                    is1_9to15 = true;
                    break;
                case "v1_12_R1":
                    is1_9to15 = true;
                    is1_12 = true;
                    break;
                default:
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public boolean is1_12() {
        return is1_12;
    }

    public boolean is1_9to15() {
        return is1_9to15;
    }

}