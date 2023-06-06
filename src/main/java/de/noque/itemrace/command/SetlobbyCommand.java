package de.noque.itemrace.command;

import de.noque.itemrace.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetlobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        /* ERROR HANDLING */
        if (!player.isOp()) {
            player.sendMessage(Component.text("Unknown command!", NamedTextColor.WHITE));
            return false;
        }

        /* COMMAND */
        ConfigManager.setLobbySpawn(player.getLocation());
        player.sendMessage(Component.text("You set the lobby spawn", NamedTextColor.YELLOW));
        return false;
    }
}
