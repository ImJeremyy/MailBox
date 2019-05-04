package me.markiscool.mailbox.listeners;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.objecthandlers.InventoryPageHandler;
import me.markiscool.mailbox.objecthandlers.TaskHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private TaskHandler th;
    private InventoryPageHandler iph;

    public LeaveListener(MailboxPlugin plugin) {
        th = plugin.getTaskHandler();
        iph = plugin.getInventoryPageHandler();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(th.contains(player)) {
            th.remove(player);
        }
        if(iph.contains(player)) {
            iph.remove(player);
        }
    }

}
