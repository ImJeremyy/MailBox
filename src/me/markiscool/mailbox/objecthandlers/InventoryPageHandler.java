package me.markiscool.mailbox.objecthandlers;

import com.sun.istack.internal.NotNull;
import me.markiscool.mailbox.MailboxPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryPageHandler {

    private Map<Player, Inventory[]> players;
    private Map<Player, Integer> pages;

    public InventoryPageHandler(MailboxPlugin plugin) {
        players = new HashMap<>();
        pages = new HashMap<>();
    }

    public void add(final Player player, final @NotNull Inventory[] inventories) {
        players.put(player, inventories);
        pages.put(player, 1);
    }

    public void remove(Player player) {
        players.remove(player);
        pages.remove(player);
    }

    public boolean contains(Player player) {
        return players.containsKey(player) && pages.containsKey(player);
    }

    public int getPage(Player player) {
        return pages.get(player);
    }

    public void setPage(Player player, int page) {
        pages.remove(player);
        pages.put(player, page);
    }

    public Inventory[] getInventories(Player player) {
        return players.get(player);
    }
}
