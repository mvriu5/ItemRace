package de.noque.itemrace;

import com.mongodb.client.MongoCollection;
import de.noque.itemrace.command.SetlobbyCommand;
import de.noque.itemrace.game.GameState;
import de.noque.itemrace.listener.*;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public final class ItemRace extends JavaPlugin {

    @Getter
    private static ItemRace itemRace;
    @Getter
    private static Configuration config;

    @Getter
    private static ServerData serverData;

    //MongoDB
    @Getter
    private MongoManager mongoManager;
    @Getter
    private static MongoCollection<Document> serverCollection;

    @Getter @Setter
    private static GameState state;
    @Getter @Setter
    private static HashMap<UUID, String> votes;

    @Override
    public void onEnable() {
        itemRace = this;

        serverData = new ServerData();
        serverData.createDocument();

        /* CREATE NEW WORLD */
        if (new File("world").exists()) {
            WorldCreator worldCreator = new WorldCreator("world");
            Bukkit.createWorld(worldCreator);
        } else {
            Bukkit.createWorld(new WorldCreator("world"));
        }

        /* CONFIG */
        config = ItemRace.getConfig();
        saveConfig();
        saveDefaultConfig();

        //DATABASE
        mongoManager = new MongoManager();
        mongoManager.connect();
        serverCollection = getMongoManager().getDatabase().getCollection("serverlist");

        /* GAME */
        state = GameState.WAITING;

        /* LISTENERS */
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new PreventionListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        /* COMMANDS */
        getCommand("setlobby").setExecutor(new SetlobbyCommand());

        /* LOBBY RULES */
        Bukkit.getWorld("lobby").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("lobby").setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Bukkit.getWorld("lobby").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("lobby").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("lobby").setTime(6000);

        /* WORLD RULES */
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DISABLE_RAIDS, true);
    }

    @Override
    public void onDisable() {
        mongoManager.disconnect();
    }
}
