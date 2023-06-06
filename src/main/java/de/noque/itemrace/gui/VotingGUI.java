package de.noque.itemrace.gui;

import de.noque.itemrace.ItemRace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class VotingGUI {

    public VotingGUI(Player player) {

        Inventory votingInv = Bukkit.createInventory(null, 9,
                Component.text("Difficulty Voting", NamedTextColor.GREEN));

        ItemStack easy = new ItemStack(Material.GREEN_WOOL);
        ItemMeta easyMeta = easy.getItemMeta();
        easyMeta.displayName(Component.text("Easy", NamedTextColor.GREEN));
        easyMeta.lore(Arrays.asList(Component.text("Votes: " +
                Collections.frequency(ItemRace.getVotes().values(), "easy"), NamedTextColor.GREEN)));
        easy.setItemMeta(easyMeta);

        ItemStack medium = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta mediumMeta = medium.getItemMeta();
        mediumMeta.displayName(Component.text("Medium", NamedTextColor.YELLOW));
        mediumMeta.lore(Arrays.asList(Component.text("Votes: " +
                Collections.frequency(ItemRace.getVotes().values(), "medium"), NamedTextColor.GREEN)));
        medium.setItemMeta(mediumMeta);

        ItemStack hard = new ItemStack(Material.RED_WOOL);
        ItemMeta hardMeta = hard.getItemMeta();
        hardMeta.displayName(Component.text("Hard", NamedTextColor.RED));
        hardMeta.lore(Arrays.asList(Component.text("Votes: " +
                Collections.frequency(ItemRace.getVotes().values(), "hard"), NamedTextColor.GREEN)));
        hard.setItemMeta(hardMeta);

        votingInv.setItem(1, hard);
        votingInv.setItem(4, medium);
        votingInv.setItem(7, hard);

        player.openInventory(votingInv);
    }
}
