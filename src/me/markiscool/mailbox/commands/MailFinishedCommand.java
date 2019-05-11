package me.markiscool.mailbox.commands;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.enums.Lang;
import me.markiscool.mailbox.enums.Perm;
import me.markiscool.mailbox.gui.XMaterial;
import me.markiscool.mailbox.objecthandlers.MailHandler;
import me.markiscool.mailbox.objecthandlers.TaskHandler;
import me.markiscool.mailbox.objecthandlers.UserHandler;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.Task;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MailFinishedCommand implements CommandExecutor {

    private MailHandler mh;
    private UserHandler uh;
    private TaskHandler th;
    private String prefix;

    public MailFinishedCommand(MailboxPlugin plugin) {
        mh = plugin.getMailHandler();
        uh = plugin.getUserHandler();
        th = plugin.getTaskHandler();
        prefix = Lang.PREFIX.getMessage();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission(Perm.MAIL)) {
                if(th.contains(player)) {
                    Task task = th.getTask(player);
                    Task.Job job = task.getJob();
                    Mail mail = task.getMail();
                    if(job.equals(Task.Job.MESSAGES)) {
                        mh.add(mail);
                        uh.getUser(player).addCreatedMail(mail);
                        th.remove(player);
                        player.sendMessage(prefix + Chat.colourize("&aTask finished. Open your mailbox in /mail to send to other players")); //TODO Lang
                    } else if(job.equals(Task.Job.SEND)) {
                        th.remove(player);
                        player.sendMessage(prefix + Chat.colourize("&aTask finished.")); //TODO Lang
                    } else if(job.equals(Task.Job.BLOCK)) {
                        th.remove(player);
                        player.sendMessage(prefix + Chat.colourize("&aTask finished.")); //TODO Lang
                    } else if(job.equals(Task.Job.ITEM)) {
                        th.remove(player);
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if(item == null || item.getType().equals(XMaterial.AIR.parseMaterial())) {
                            mail.setItem(null);
                        } else {
                            mail.setItem(item);
                        }
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                        player.sendMessage(prefix + Chat.colourize("&aTask finished.")); //TODO Lang
                    } else {
                        player.sendMessage(prefix + Chat.colourize("&cYou cannot cancel that task.")); //TODO Lang
                    }
                } else {
                    player.sendMessage(prefix + Lang.NOT_CURRENTLY_RUNNING_TASK.getMessage());
                }
            } else {
                player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.NOT_A_PLAYER.getMessage());
        }
        return true;
    }

}
