package me.markiscool.mailbox.commands;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.enums.Lang;
import me.markiscool.mailbox.enums.Perm;
import me.markiscool.mailbox.gui.Items;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MailCommand implements CommandExecutor {

    private String prefix;

    public MailCommand(MailboxPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
    }

    /**
     * /mail --> opens gui
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission(Perm.MAIL)) {
                player.openInventory(Items.getMainMenu(player));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.5f);
            } else {
                player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.NOT_A_PLAYER.getMessage());
        }
        return true;
    }

}
