package me.markiscool.mailbox.listeners;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.enums.Lang;
import me.markiscool.mailbox.objecthandlers.UserHandler;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private UserHandler uh;
    private String prefix;

    public JoinListener(MailboxPlugin plugin) {
        uh = plugin.getUserHandler();
        prefix = Lang.PREFIX.getMessage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!uh.isRegistered(player)) {
            uh.register(player);
        }
        User user = uh.getUser(player);
        if(!user.getMailbox().isEmpty()) {
            int count = user.getMailbox().size();
            player.sendMessage(prefix + Chat.colourize("&aYou have &6" + count + " &amail in your mailbox."));
        }

    }

}
