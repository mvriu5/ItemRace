package de.noque.itemrace.listener;

import de.noque.itemrace.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        e.deathMessage(Component.text(""));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        e.setRespawnLocation(ConfigManager.getLobbySpawn());

        player.getInventory().clear();

        player.getInventory().setItem(0, new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().setItem(1, new ItemStack(Material.IRON_AXE));
        player.getInventory().setItem(2, new ItemStack(Material.IRON_SHOVEL));
    }
}
