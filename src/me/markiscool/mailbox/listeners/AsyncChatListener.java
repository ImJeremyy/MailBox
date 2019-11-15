package me.markiscool.mailbox.listeners;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.enums.Lang;
import me.markiscool.mailbox.enums.Perm;
import me.markiscool.mailbox.objecthandlers.UserHandler;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.Task;
import me.markiscool.mailbox.objecthandlers.TaskHandler;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import me.markiscool.mailbox.utility.TextComponentUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listens for player chat and cancels it and stores the
 * data depending on what task they are accomplishing.
 */
public class AsyncChatListener implements Listener {

    private TaskHandler th;
    private UserHandler uh;
    private String prefix;

    public AsyncChatListener(MailboxPlugin plugin) {
        th = plugin.getTaskHandler();
        uh = plugin.getUserHandler();
        prefix = Lang.PREFIX.getMessage();
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(th.contains(player)) {
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 0.9f);
            String message = event.getMessage();
            Task task = th.getTask(player);
            Task.Job job = task.getJob();
            Mail mail = task.getMail();
            switch(job) {
                case TITLE:
                    mail.setTitle(message);
                    th.remove(player);
                    th.add(player, new Task(Task.Job.MESSAGES, mail));
                    player.sendMessage(prefix + Chat.colourize("&aNow enter the messages."));
                    break;
                case MESSAGES:
                    mail.getMessage().add(message);
                    player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aAdded message. Click &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                    break;
                case SEND:
                    if(!message.equalsIgnoreCase(player.getName())) {
                        User user = uh.getUser(message);
                        if (user != null) {
                            if(!mail.containsRecipient(user.getUniqueId())) {
                                if(!user.containsBlockedUser(player.getUniqueId())) {
                                    player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aAdded &6" + user.getUsername() + "&a. Click &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                                    mail.addRecipient(user.getUniqueId(), System.currentTimeMillis());
                                    user.addMail(mail);
                                    Player userPlayer = Bukkit.getPlayer(user.getUniqueId());
                                    if(userPlayer.isOnline() && userPlayer.hasPermission(Perm.MAIL)) {
                                        userPlayer.sendMessage(prefix + Chat.colourize("&aYou received mail from &6" + player.getName() + "&a. Open it through /mail."));
                                    }
                                } else {
                                    player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&6" + user.getUsername() + " &cblocked you. Click &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                                }
                            } else {
                                player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&6" + user.getUsername() + " &calready received this mail. Click &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                            }
                        } else {
                            player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&6" + message + "&c was not found.. Click &7<DONE> &cor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                        }
                    } else {
                        player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&cYou cannot send that to yourself. Click &7<DONE> or continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                    }
                    break;
                case BLOCK:
                    if(!message.equalsIgnoreCase(player.getName())) {
                        User user = uh.getUser(message);
                        if(user != null) {
                            User userPlayer = uh.getUser(player);
                            if(!userPlayer.containsBlockedUser(user.getUniqueId())) {
                                userPlayer.block(user.getUniqueId());
                                player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aBlocked &6" + user.getUsername() + " &aClick &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                            } else {
                                player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&6" + user.getUsername() + " &cis already blocked. &aClick &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                            }
                        } else {
                            player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&cPlayer not found. &aClick &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));

                        }
                    } else {
                        player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&cYou cannot block yourself. &aClick &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));

                    }
                    break;
            }
        }
    }

}
