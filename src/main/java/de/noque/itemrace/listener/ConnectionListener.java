package de.noque.itemrace.listener;

import de.noque.itemrace.ConfigManager;
import de.noque.itemrace.game.Countdown;
import de.noque.itemrace.game.Game;
import de.noque.itemrace.game.GameState;
import de.noque.itemrace.ItemRace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionListener implements Listener {

    Countdown countdown = new Countdown();
    int players = Bukkit.getOnlinePlayers().size();

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        if (ItemRace.getState() != GameState.WAITING)
            player.kick(Component.text("Game already started!", NamedTextColor.RED));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        ItemRace.getServerData().updatePlayerCount();

        /* PLAYER JOIN */
        if (ItemRace.getState() == GameState.WAITING) {
            e.joinMessage(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE)
                    .append(Component.text(" joined the game", NamedTextColor.GRAY))
                    .append(Component.text("["+ players + "/2]", NamedTextColor.GREEN)));

            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(ConfigManager.getLobbySpawn());
            player.getInventory().clear();

            /* VOTE ITEM */
            ItemStack vote = new ItemStack(Material.BOOK);
            ItemMeta voteMeta = vote.getItemMeta();
            voteMeta.displayName(Component.text("Vote for DIFFICULTY", NamedTextColor.LIGHT_PURPLE));
            List<Component> voteLore = new ArrayList<>(Arrays.asList(
                    Component.text("Easy: 15 Items", NamedTextColor.YELLOW),
                    Component.text("Medium: 10 Items", NamedTextColor.YELLOW),
                    Component.text("Hard: 5 Items", NamedTextColor.YELLOW)));
            voteMeta.lore(voteLore);
            vote.setItemMeta(voteMeta);

            player.getInventory().setItem(4, vote);
        }

        /* START COUNTDOWN */
        if (ItemRace.getState() == GameState.WAITING && players == 2) {
            countdown.start();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ItemRace.getServerData().updatePlayerCount();

        /* QUIT MESSAGE */
        e.quitMessage(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(" left the game", NamedTextColor.GRAY))
                .append(Component.text("["+ players + "/2]", NamedTextColor.GREEN)));

        /* STOP COUNTDOWN ON LEAVE */
        if (ItemRace.getState() == GameState.COUNTDOWN && players < 2) {
            countdown.stop();
        }
        if (ItemRace.getState() == GameState.INGAME && players < 2) {
            Game.stop();
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        ItemRace.getServerData().updatePlayerCount();

        /* KICK MESSAGE */
        e.leaveMessage(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(" left the game", NamedTextColor.GRAY))
                .append(Component.text("["+ players + "/2]", NamedTextColor.GREEN)));

        /* STOP COUNTDOWN ON LEAVE */
        if (ItemRace.getState() == GameState.COUNTDOWN && players < 2) {
            countdown.stop();
        }
        if (ItemRace.getState() == GameState.INGAME && players < 2) {
            Game.stop();
        }
    }
}
