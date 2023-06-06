package de.noque.itemrace.game;

import com.destroystokyo.paper.Title;
import de.noque.itemrace.ItemRace;
import de.noque.itemrace.ServerData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


public class Countdown extends BukkitRunnable {

    private int time = 15;

    public void start() {
        ItemRace.setState(GameState.COUNTDOWN);
        runTaskTimer(ItemRace.getItemRace(), 0, 20);
        ItemRace.getServerData().updateState("starting");
    }

    public void stop() {
        ItemRace.setState(GameState.WAITING);
        Bukkit.broadcast(Component.text("The countdown got cancelled", NamedTextColor.RED));
        ItemRace.getServerData().updateState("waiting");
        cancel();
    }

    @Override
    public void run() {
        //Countdown
        Bukkit.getOnlinePlayers().forEach(player -> {
            Bukkit.broadcast(Component.text("Game is starting in " + time + "seconds", NamedTextColor.GREEN));
            player.playSound(Sound.sound(Key.key("ui_button_click"), Source.AMBIENT , 1f, 1f));

            if (time == 10 || time <= 5) {
                player.sendTitle(Title.builder().title(String.valueOf(Component.text(time, NamedTextColor.GOLD))).build());
            }
        });

        //Start
        if (time == 0) {
            Bukkit.broadcast(Component.text("Game is STARTING...", NamedTextColor.GREEN));
            Game.start();
            ItemRace.setState(GameState.INGAME);
            cancel();
            return;
        }
        time--;
    }
}
