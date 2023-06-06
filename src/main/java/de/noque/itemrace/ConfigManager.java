package de.noque.itemrace;

import org.bukkit.Location;

public class ConfigManager {

    public static Location getLobbySpawn() {
        return ItemRace.getConfig().getLocation("spawn");
    }

    public static void setLobbySpawn(Location location) {
        ItemRace.getConfig().set("spawn", location);
        ItemRace.getItemRace().saveConfig();
    }
}
