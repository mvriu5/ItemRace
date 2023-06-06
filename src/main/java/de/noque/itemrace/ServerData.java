package de.noque.itemrace;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;

public class ServerData {

    private final String serverName = Bukkit.getServer().getName();

    public ServerData() { }

    public void createDocument() {
        Document document = new Document();
        document.append("servername", serverName);
        document.append("serverstate", "waiting");
        document.append("gamemode", "itemrace");
        document.append("playercount", Bukkit.getOnlinePlayers());

        ItemRace.getServerCollection().insertOne(document);
    }

    public void updateState(String gamestate) {
        ItemRace.getServerCollection().updateOne(Filters.eq("servername", serverName), new Document("$set", new Document("serverstate", gamestate)));
    }

    public void updatePlayerCount() {
        ItemRace.getServerCollection().updateOne(Filters.eq("servername", serverName), new Document("$set", new Document("playercount", Bukkit.getOnlinePlayers())));
    }
}
