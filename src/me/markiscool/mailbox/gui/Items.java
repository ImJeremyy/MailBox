package me.markiscool.mailbox.gui;

import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import me.markiscool.mailbox.utility.InvUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Items {

    public static ItemStack blank;
    public static ItemStack block;
    public static ItemStack mailbox;
    public static ItemStack yourMail;
    public static ItemStack createNewMail;
    public static ItemStack next;
    public static ItemStack last;

    public static Inventory mainmenu;

    static {
        blank = generateItemStack(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&f-", null);
        block = generateItemStack(XMaterial.BARRIER, 1, "&bBlock a player", Arrays.asList("&eClick me to block a player"));
        mailbox = generateItemStack(XMaterial.CHEST, 1, "&bMailbox", Arrays.asList("&eClick me to open your mailbox"));
        yourMail = generateItemStack(XMaterial.BOOK, 1, "&bYour created Mail", Arrays.asList("&eClick me to view/edit your created mail"));
        createNewMail = generateItemStack(XMaterial.PAPER, 1, "&bCreate new Mail", Arrays.asList("&eClick me to create a new mail", "&eto send to other players"));
        next = generateItemStack(XMaterial.ARROW, 1, "&iNext Page", null);
        last = generateItemStack(XMaterial.ARROW, 1, "&iLast Page", null);
        generateMainMenu();
    }

    public static ItemStack generateItemStack(XMaterial xmaterial, int amount, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(xmaterial.parseMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.colourize(displayName));
        if(lore != null) meta.setLore(Chat.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack generatePlayerHead(OfflinePlayer player, int amount, String displayName, List<String> lore) {
        ItemStack head = generateItemStack(XMaterial.PLAYER_HEAD, amount, displayName, lore);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    public static Inventory[] generateMailbox(User user) {
        Set<Mail> mailbox = user.getMailbox();
        int pagesAmount = InvUtil.getPages(mailbox.size());
        Inventory[] pages = new Inventory[pagesAmount];
        for(int i = 0; i < pagesAmount; i++) {
            Inventory inv = Bukkit.createInventory(null, 54, Chat.colourize("&fYour Mailbox &6#" + (i + 1)));
            Iterator<Mail> iterator = mailbox.iterator();
            for(int j = 0; j < 45 && iterator.hasNext(); j++) {
                Mail mail = iterator.next();
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add("&dLEFT CLICK &e- View");
                if(mail.hasItem()) {
                    lore.add("&dRIGHT CLICk &e- Receive item");
                }
                lore.add("&dSNEAK RIGHT CLICK &e- Mark as read");
                lore.add("&cWARNING: Marking as read will delete it");
                lore.add("&cand users who have not opened it will");
                lore.add("&cno longer be able to open it..");
                lore.add("&7" + mail.getId());
                ItemStack paper = Items.generateItemStack(XMaterial.PAPER, 1, mail.getTitle() + " &6from " + Bukkit.getOfflinePlayer(mail.getCreator()).getName(), lore);
                inv.setItem(j, paper);
            }
            for(int k = 46; k < 53; k++) {
                inv.setItem(k, Items.blank );
            }
            if(i > 0) {
                inv.setItem(45, Items.last);
            } else {
                inv.setItem(45, Items.blank);
            }
            if(i < pagesAmount - 1) {
                inv.setItem(53, Items.next);
            } else {
                inv.setItem(53, Items.blank);
            }
            pages[i] = inv;
        }
        return pages;
    }

    public static Inventory[] generateYourMail(User user) {
        Set<Mail> yourMail = user.getCreatedMail();
        int pagesAmount = InvUtil.getPages(yourMail.size());
        Inventory[] pages = new Inventory[pagesAmount];
        for(int i = 0; i < pagesAmount; i++) {
            Inventory inv = Bukkit.createInventory(null, 54, Chat.colourize("&fYour Created Mail &6#" + (i + 1)));
            Iterator<Mail> iterator = yourMail.iterator();
            for(int j = 0; j < 45 && iterator.hasNext(); j++) {
                Mail mail = iterator.next();
                List<String> lore = new ArrayList<>(mail.getMessage());
                lore.add("");
                if(!mail.getRecipients().isEmpty()) {
                    lore.add("&dRecipients:");
                    for(UUID uuid : mail.getRecipients().keySet()) {
                        String username = Bukkit.getOfflinePlayer(uuid).getName();
                        lore.add("&f- &e" + username);
                    }
                } else {
                    lore.add("&dRecipients: &eNone");
                }
                if(mail.hasItem()) {
                    lore.add("&dItem: &e" + mail.getItem().getType().name());
                } else {
                    lore.add("&dItem: &eNone");
                }
                lore.add("");
                lore.add("&dLEFT CLICK &e- Add lines");
                lore.add("&dRIGHT CLICK &e- Remove last line");
                lore.add("&dDROP &e- Add item");
                lore.add("&dSNEAK LEFT CLICK &e- Send to players");
                lore.add("&dSNEAK RIGHT CLICK &e- Delete this mail");
                lore.add("&dCONTROL DROP &e- Delete item");
                lore.add(Chat.colourize("&7" + mail.getId()));
                ItemStack paper = Items.generateItemStack(XMaterial.PAPER, 1, mail.getTitle(), lore);
                inv.setItem(j, paper);
            }
            for(int k = 46; k < 53; k++) {
                inv.setItem(k, Items.blank );
            }
            if(i > 0) {
                inv.setItem(45, Items.last);
            } else {
                inv.setItem(45, Items.blank);
            }
            if(i < pagesAmount - 1) {
                inv.setItem(53, Items.next);
            } else {
                inv.setItem(53, Items.blank);
            }
            pages[i] = inv;
        }
        return pages;
    }

    public static Inventory[] generateBlocked(User user) {
        Set<UUID> blocked = user.getBlockedUsers();
        int pagesAmount = InvUtil.getPages(blocked.size());
        Inventory[] pages = new Inventory[pagesAmount];
        for(int i = 0; i < pagesAmount; i++) {
            Inventory inv = Bukkit.createInventory(null, 54, Chat.colourize("&fBlocked Players &6#" + (i + 1)));
            Iterator<UUID> iterator = blocked.iterator();
            for(int j = 0; j < 45 && iterator.hasNext(); j++) {
                UUID blockedUser = iterator.next();
                List<String> lore = new ArrayList<>();
                lore.add("&dSHIFT RIGHT CLICK &e- Unblock this player");
                OfflinePlayer blockedPlayer = Bukkit.getOfflinePlayer(blockedUser);
                ItemStack head = Items.generatePlayerHead(blockedPlayer, 1, blockedPlayer.getName(), lore);
                inv.setItem(j, head);
            }
            for(int k = 46; k < 53; k++) {
                inv.setItem(k, Items.blank );
            }
            if(i > 0) {
                inv.setItem(45, Items.last);
            } else {
                inv.setItem(45, Items.blank);
            }
            if(i < pagesAmount - 1) {
                inv.setItem(53, Items.next);
            } else {
                inv.setItem(53, Items.blank);
            }
            pages[i] = inv;
        }
        return pages;
    }

    public static Inventory getMainMenu(Player player) {
        mainmenu.setItem(10, generatePlayerHead(player, 1, "&bBlocked Players", Arrays.asList("&eClick me to view your blocked players", "&eYou may unblock players here as well.")));
        return mainmenu;
    }

    private static void generateMainMenu() {
        mainmenu = Bukkit.createInventory(null, 27, Chat.colourize("&fMailbox &rby MarkIsCool"));
        for(int i = 0; i < mainmenu.getSize(); i++) {
            mainmenu.setItem(i, blank);
        }
        mainmenu.setItem(11, block);
        mainmenu.setItem(12, mailbox);
        mainmenu.setItem(14, yourMail);
        mainmenu.setItem(15, createNewMail);
    }

}
