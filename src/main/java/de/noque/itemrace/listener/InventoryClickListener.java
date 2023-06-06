package de.noque.itemrace.listener;

import de.noque.itemrace.ItemRace;
import de.noque.itemrace.game.Game;
import de.noque.itemrace.game.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        //Cancel item dragging in lobby
        if (ItemRace.getState() != GameState.INGAME) {
            e.setCancelled(true);
        }

        /* VOTING ITEM */
        if (e.getView().getTitle().equals("Difficulty Voting") && e.getCurrentItem().hasItemMeta()) {
            ItemRace.getVotes().remove(player.getUniqueId());

            switch (e.getCurrentItem().getType()) {
                case GREEN_WOOL -> ItemRace.getVotes().put(player.getUniqueId(), "easy");
                case YELLOW_WOOL -> ItemRace.getVotes().put(player.getUniqueId(), "medium");
                case RED_WOOL -> ItemRace.getVotes().put(player.getUniqueId(), "hard");
            }
            player.getInventory().close();
        }
    }

    @EventHandler
    public void onItemPickUp(PlayerAttemptPickupItemEvent e) {
        Player player = e.getPlayer();
        Material currentItem = Game.getItems().get(Game.getProgress().get(player.getUniqueId()));

        if (ItemRace.getState() == GameState.INGAME && player.getInventory().contains(currentItem)) {
            Bukkit.broadcast(Component.text(player.getName() + " found " + currentItem, NamedTextColor.GREEN));
            Game.getProgress().put(player.getUniqueId(), Game.getProgress().get(player.getUniqueId()) + 1);
            player.sendMessage(Component.text("Your next item is: " + Game.getItems().get(Game.getProgress().get(player.getUniqueId()))));
        }

        if (Game.getProgress().get(player.getUniqueId()).equals(Game.getItems().size() + 1)) {
            Game.stop();
            Game.setWinner(player);
        }
    }
}
