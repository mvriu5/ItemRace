package de.noque.itemrace.game;

import com.destroystokyo.paper.Title;
import de.noque.itemrace.ConfigManager;
import de.noque.itemrace.Difficulty;
import de.noque.itemrace.ItemRace;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codehaus.plexus.util.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    @Getter
    private static HashMap<UUID, Integer> progress = new HashMap<>();
    @Getter
    private static String difficulty;
    @Getter
    private static List<Material> items;
    @Getter @Setter
    private static Player winner;

    public static void start() {
        /* GET DIFFICULTY/ITEMS */
        ItemRace.getServerData().updateState("ingame");
        difficulty = Difficulty.calculate();
        items = Difficulty.getItems(difficulty);

        /* READY UP PLAYERS */
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = Bukkit.getWorld("world");
            int x = (int) (Math.random() * 40 - 20);
            int z = (int) (Math.random() * 40 - 20);
            assert world != null;
            int y = world.getHighestBlockYAt(x, z);

            player.teleport(new Location(world, x, y, z));
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().setItem(0, new ItemStack(Material.IRON_PICKAXE));
            player.getInventory().setItem(1, new ItemStack(Material.IRON_AXE));
            player.getInventory().setItem(2, new ItemStack(Material.IRON_SHOVEL));

            progress.put(player.getUniqueId(), 1);
        });
    }

    public static void stop() {
        ItemRace.setState(GameState.FINISHED);
        ItemRace.getServerData().updateState("restarting");

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(ConfigManager.getLobbySpawn());
            player.getInventory().clear();
            player.playSound(Sound.sound(Key.key("music_disc.mall"), Sound.Source.MUSIC , 1f, 1f));
            player.sendTitle(Title.builder()
                    .title(String.valueOf(Component.text(winner.getName(), NamedTextColor.GOLD)))
                    .subtitle(String.valueOf(Component.text("won the game", NamedTextColor.GREEN))).build());
        });

        /* CLOSING COUNTDOWN */
        AtomicInteger closingTime = new AtomicInteger(10);
        Bukkit.getScheduler().runTaskLater(ItemRace.getItemRace(), () -> {
            Bukkit.broadcast(Component.text(
                    "Server is closing in " + closingTime.getAndDecrement() + "seconds...", NamedTextColor.RED));

            if (closingTime.get() == 0) {
                Bukkit.shutdown();
                Bukkit.unloadWorld("world", false);

                //Delete world and files
                Path worldDir = Paths.get("./world/");
                if (worldDir.toFile().exists()) {
                    try {
                        Files.deleteIfExists(worldDir.resolve("world_seed.txt"));
                        Files.deleteIfExists(worldDir.resolve("world_name.txt"));
                        FileUtils.deleteDirectory(worldDir.toFile());
                    } catch (IOException e) {
                        System.err.println("Failed to delete world: " + e.getMessage());
                    }
                }
            }
        }, 10 * 20);
    }
}
