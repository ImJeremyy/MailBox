package me.markiscool.mailbox.listeners;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.enums.Lang;
import me.markiscool.mailbox.enums.Perm;
import me.markiscool.mailbox.gui.Items;
import me.markiscool.mailbox.gui.XMaterial;
import me.markiscool.mailbox.objecthandlers.InventoryPageHandler;
import me.markiscool.mailbox.objecthandlers.MailHandler;
import me.markiscool.mailbox.objecthandlers.TaskHandler;
import me.markiscool.mailbox.objecthandlers.UserHandler;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.Task;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import me.markiscool.mailbox.utility.TextComponentUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Date;
import java.util.List;

public class ClickListener implements Listener {

    private UserHandler uh;
    private InventoryPageHandler iph;
    private TaskHandler th;
    private MailHandler mh;

    private String prefix;

    public ClickListener(MailboxPlugin plugin) {
        uh = plugin.getUserHandler();
        iph = plugin.getInventoryPageHandler();
        th = plugin.getTaskHandler();
        mh = plugin.getMailHandler();
        prefix = Lang.PREFIX.getMessage();
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof  Player) {
            Player player = (Player) event.getWhoClicked();
            if(player.hasPermission(Perm.MAIL)) {
                String invName = Chat.strip(player.getOpenInventory().getTitle());
                ItemStack item = event.getCurrentItem();
                if(invName.equalsIgnoreCase("Mailbox by MarkIsCool")) { //main menu
                    event.setCancelled(true);
                    User user = uh.getUser(player);
                    if(item.equals(Items.mailbox)) {
                        Inventory[] mailbox = Items.generateMailbox(user);
                        player.closeInventory();
                        player.openInventory(mailbox[0]);
                        iph.add(player, mailbox);
                    } else if (item.equals(Items.yourMail)) {
                        Inventory[] yourMail = Items.generateYourMail(user);
                        player.closeInventory();
                        player.openInventory(yourMail[0]);
                        iph.add(player, yourMail);
                    } else if (item.equals(Items.createNewMail)) {
                        player.closeInventory();
                        th.add(player, new Task(Task.Job.TITLE, new Mail(mh.generateId(), user.getUniqueId())));
                        player.sendMessage(prefix + Chat.colourize("&aEnter the title of your mail: "));
                    } else if(item.equals(Items.block)) {
                        player.closeInventory();;
                        th.add(player, new Task(Task.Job.BLOCK));
                        player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aEnter the players you would like to block. Click &7<DONE> &awhen you are done.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                    } else if(item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta instanceof SkullMeta) {
                                SkullMeta skullMeta = (SkullMeta) meta;
                                if (Chat.strip(skullMeta.getDisplayName()).equals("Blocked Players")) {
                                    Inventory[] blocked = Items.generateBlocked(uh.getUser(player));
                                    player.openInventory(blocked[0]);
                                    iph.add(player, blocked);
                                }
                            }
                        }
                    }
                } else if(invName.contains("Created Mail")) {
                    event.setCancelled(true);
                    if(item != null && item.getType().equals(XMaterial.PAPER.parseMaterial())) {
                        if(item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if(meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                String id = Chat.strip(lore.get(lore.size() - 1));
                                Mail mail = mh.getMail(id);
                                if (mail != null) {
                                    ClickType type = event.getClick();
                                    switch (type) {
                                        case LEFT:
                                            player.sendMessage(prefix + Chat.colourize("&aEnter the line you would like to add:"));
                                            player.closeInventory();
                                            th.add(player, new Task(Task.Job.MESSAGES, mail));
                                            break;
                                        case RIGHT:
                                            mail.getMessage().remove(mail.getMessage().size() - 1);
                                            player.sendMessage(prefix + Chat.colourize("&aRemoved last line of mail with title: &6" + mail.getTitle()));
                                            User user = uh.getUser(player);
                                            Inventory[] yourMail = Items.generateYourMail(user);
                                            player.closeInventory();
                                            updateInventory(player, yourMail);
                                            break;
                                        case SHIFT_LEFT:
                                            player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aEnter the players you would like to send this mail to. Click &7<DONE> &aor continue.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));
                                            player.closeInventory();
                                            th.add(player, new Task(Task.Job.SEND, mail));
                                            break;
                                        case SHIFT_RIGHT:
                                            user = uh.getUser(player);
                                            user.removeCreatedMail(mail);
                                            mh.remove(mail);
                                            player.sendMessage(prefix + Chat.colourize("&aRemoved mail with title: &6" + mail.getTitle()));
                                            for(User u : uh.getUsers()) {
                                                if(u != user) {
                                                    u.getMailbox().remove(mail);
                                                }
                                            }
                                            yourMail = Items.generateYourMail(user);
                                            player.closeInventory();
                                            if (yourMail.length != 0) {
                                                player.openInventory(yourMail[0]);
                                                iph.add(player, yourMail);
                                            } else {
                                                player.sendMessage(prefix + Lang.CREATED_MAIL_EMPTY.getMessage());
                                            }
                                            break;
                                        case DROP:
                                            if(!mail.hasItem()) {
                                                player.closeInventory();
                                                th.add(player, new Task(Task.Job.ITEM, mail));
                                                player.spigot().sendMessage(TextComponentUtil.generateTextComponent(prefix + "&aClick &7<DONE> &awhen you have the item in your hand, or hold air to cancel.", HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.GRAY + "Click me when you are done."), ClickEvent.Action.RUN_COMMAND, "/mailfinished"));

                                            } else {
                                                player.sendMessage(prefix + Chat.colourize("&cThis mail already has an item. Delete it first."));
                                            }
                                            break;
                                        case CONTROL_DROP:
                                            player.sendMessage(prefix + Chat.colourize("&aRemoved item from mail with title: &6" + mail.getTitle()));
                                            item = mail.getItem();
                                            mail.setItem(null);
                                            if(player.getInventory().firstEmpty() != -1) {
                                                player.getInventory().addItem(item);
                                            } else {
                                                player.getLocation().getWorld().dropItem(player.getLocation().add(0, 1, 0), item);
                                            }
                                            user = uh.getUser(player);
                                            yourMail = Items.generateYourMail(user);
                                            updateInventory(player, yourMail);
                                            break;
                                    }
                                } else {
                                    player.sendMessage(prefix + Chat.colourize("&cThis mail does not exist."));
                                    player.closeInventory();
                                }
                            }
                        }
                    } else if(item.equals(Items.next)) {
                        nextPage(player);
                    } else if(item.equals(Items.last)) {
                        lastPage(player);
                    }
                } else if(invName.contains("Mailbox")) {
                    event.setCancelled(true);
                    if(item != null && item.getType().equals(XMaterial.PAPER.parseMaterial())) {
                        if(item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if(meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                String id = Chat.strip(lore.get(lore.size() - 1));
                                Mail mail = mh.getMail(id);
                                User sender = uh.getCreator(mail);
                                if(mail != null) {
                                    ClickType click = event.getClick();
                                    switch(click) {
                                        case LEFT:
                                            player.closeInventory();
                                            player.sendMessage("");
                                            player.sendMessage(prefix + Chat.colourize("&aMail from &6" + sender.getUsername() + "&a."));
                                            player.sendMessage("");
                                            player.sendMessage(ChatColor.UNDERLINE + mail.getTitle());
                                            player.sendMessage("");
                                            for(String m : mail.getMessage()) {
                                                player.sendMessage("    " + ChatColor.ITALIC + m);
                                            }
                                            player.sendMessage("");
                                            player.sendMessage(Chat.colourize("&aMail sent on &6" + new Date(mail.getTimestamp(uh.getUser(player).getUniqueId()))));
                                            player.sendMessage("");
                                            break;
                                        case RIGHT:
                                            if(mail.hasItem()) {
                                                if (!mail.hasReceivedItem(player)) {
                                                    mail.addReceivedItemRepient(player);
                                                    player.getInventory().addItem(mail.getItem());
                                                    updateInventory(player, Items.generateMailbox(uh.getUser(player)));
                                                } else {
                                                    player.sendMessage(prefix + Chat.colourize("&cYou already received this item.")); //TODO Lang
                                                }
                                            }
                                            break;
                                        case SHIFT_RIGHT:
                                            User user = uh.getUser(player);
                                            user.removeMail(mail);
                                            Inventory[] mailbox = Items.generateMailbox(user);
                                            updateInventory(player, mailbox);
                                            break;
                                    }
                                } else {
                                    player.sendMessage(prefix + Chat.colourize("&cThis mail does not exist."));
                                    player.closeInventory();
                                }
                            }
                        }
                    } else if(item.equals(Items.next)) {
                        nextPage(player);
                    } else if(item.equals(Items.last)) {
                        lastPage(player);
                    }
                } else if(invName.contains("Blocked Players")) {
                    event.setCancelled(true);
                    if(item != null && item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial()) && item.hasItemMeta()) {
                        ClickType click = event.getClick();
                        if (click.equals(ClickType.SHIFT_RIGHT)) {
                            User userToUnblock = uh.getUser(Chat.strip(item.getItemMeta().getDisplayName()));
                            if(userToUnblock != null) {
                                User playerUser = uh.getUser(player);
                                playerUser.unblock(userToUnblock.getUniqueId());
                                Inventory[] invs = Items.generateBlocked(userToUnblock);
                                updateInventory(player, invs);
                                player.sendMessage(prefix + Chat.colourize("&aUnblocked &6" + userToUnblock.getUsername() + "&a."));
                            }
                        }
                    } else if(item.equals(Items.next)) {
                        nextPage(player);
                    } else if(item.equals(Items.last)) {
                        lastPage(player);
                    }
                }
            }
        }
    }

    /**
     * Updates GUI, closes it then re opens it with the given Inventory[].
     * @param player Player to update GUI
     * @param inventories See Items#generateYourMail and Items#generateMailbox
     */
    private void updateInventory(Player player, Inventory[] inventories) {
        int page = iph.getPage(player);
        player.closeInventory();
        player.openInventory(inventories[page - 1]);
        iph.add(player, inventories);
    }

    /**
     * Goes to the next page of the GUI
     * @param player player to change
     */
    private void nextPage(Player player) {
        Inventory[] invs = iph.getInventories(player);
        int page = iph.getPage(player);
        iph.setPage(player, page + 1);
        player.openInventory(invs[page]);
    }

    /**
     * Goes to the previous page of the GUI
     * @param player player to change
     */
    private void lastPage(Player player) {
        Inventory[] invs = iph.getInventories(player);
        int page = iph.getPage(player);
        iph.setPage(player, page - 1);
        player.openInventory(invs[page - 2]);
    }


}
