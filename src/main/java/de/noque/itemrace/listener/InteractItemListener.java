package de.noque.itemrace.listener;

import de.noque.itemrace.ItemRace;
import de.noque.itemrace.game.GameState;
import de.noque.itemrace.gui.VotingGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractItemListener implements Listener {

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Material material = player.getInventory().getItemInHand().getType();

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) &&
                (ItemRace.getState() == GameState.WAITING || ItemRace.getState() == GameState.COUNTDOWN) && material == Material.BOOK) {

            new VotingGUI(player);
        }
    }
}
