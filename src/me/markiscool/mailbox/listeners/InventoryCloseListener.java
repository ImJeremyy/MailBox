package me.markiscool.mailbox.listeners;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.objecthandlers.InventoryPageHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private InventoryPageHandler iph;

    public InventoryCloseListener(MailboxPlugin plugin) {
        iph = plugin.getInventoryPageHandler();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(iph.contains(player)) {
            iph.remove(player);
        }
    }
}
