package de.noque.itemrace.listener;

import de.noque.itemrace.ItemRace;
import de.noque.itemrace.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Player player = (Player) e.getEntity();

        if (ItemRace.getState() != GameState.INGAME) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getEntity();

        e.setCancelled(true);
    }
}
