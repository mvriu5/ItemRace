package de.noque.itemrace;

import de.noque.itemrace.util.Items;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Difficulty {

    public static String calculate() {
        String gameDiff = "";

        List<String> valuesList = new ArrayList<>(ItemRace.getVotes().values());
        int easy = Collections.frequency(valuesList, "easy");
        int medium = Collections.frequency(valuesList, "medium");
        int hard = Collections.frequency(valuesList, "hard");

        int max = Math.max(Math.max(easy, medium), hard);

        if (easy == max) gameDiff = "easy";
        if (medium == max) gameDiff = "medium";
        if (hard == max) gameDiff = "hard";

        if (easy == medium && hard == 0) gameDiff = (new Random().nextBoolean()) ? "easy" : "medium";
        if (easy == hard && medium == 0) gameDiff = (new Random().nextBoolean()) ? "easy" : "hard";
        if (medium == hard && easy == 0) gameDiff = (new Random().nextBoolean()) ? "medium" : "hard";

        return gameDiff;
    }

    public static List<Material> getItems(String diff) {
        int itemNumber = 0;
        List<Material> itemList = null;

        switch (diff) {
            case "easy" -> {
                itemNumber = 15;
                itemList = Items.getEasyItems();
            }
            case "medium" -> {
                itemNumber = 10;
                itemList = Items.getMediumItems();
            }
            case "hard" -> {
                itemNumber = 5;
                itemList = Items.getHardItems();
            }
        }

        List<Material> finalItems = new ArrayList<>();
        Random random = new Random();

        while (finalItems.size() < itemNumber) {
            Material item = itemList.get(random.nextInt(itemList.size()));
            if (!finalItems.contains(item)) {
                finalItems.add(item);
            }
        }
        return finalItems;
    }
}
