package me.markiscool.mailbox.objecthandlers;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MailHandler {

    private MailboxPlugin plugin;

    private File file;
    private FileConfiguration filecfg;

    private Set<Mail> mails;

    public MailHandler(final MailboxPlugin plugin) {
        this.plugin = plugin;
        mails = new HashSet<>();
        createFile();
        pull();
        registerPushRunnable(plugin, 40, plugin.getDelay());
    }

    public boolean add(final Mail mail) {
        return mails.add(mail);
    }

    public boolean remove(final Mail mail) {
        return mails.remove(mail);
    }

    public boolean contains(final Mail mail) {
        return mails.contains(mail);
    }

    public Mail getMail(final String id) {
        for(final Mail mail : mails) {
            if(mail.getId().equalsIgnoreCase(id)) {
                return mail;
            }
        }
        return null;
    }

    public void push() {
        filecfg.set("mail", null);
        final ConfigurationSection section = filecfg.createSection("mail");
        for(final Mail mail : mails) {
            final ConfigurationSection sec = section.createSection(mail.getId());
            sec.set("title", mail.getTitle());
            sec.createSection("message");
            sec.set("message", mail.getMessage());
            sec.set("creator", mail.getCreator().toString());
            sec.createSection("recipients");
            List<String> receivedItemRecipients = new ArrayList<>();
            sec.set("receiveditemrecipients", receivedItemRecipients);
            final ConfigurationSection recipientsSec = sec.getConfigurationSection("recipients");
            for(final Map.Entry<UUID, Long> entry : mail.getRecipients().entrySet()) {
                final UUID uuid = entry.getKey();
                final long timestamp = entry.getValue();
                recipientsSec.set(uuid.toString(), timestamp);
            }
            for(final UUID uuid : mail.getReceivedItemRecipients()) {
                receivedItemRecipients.add(uuid.toString());
            }
            sec.set("receiveditemrecipients", receivedItemRecipients);
            sec.set("item", mail.getItem());
        }
        saveConfig();
    }

    public void pull() {
        mails.clear();
        final ConfigurationSection section = filecfg.getConfigurationSection("mail");
        for(final String mailId : section.getKeys(false)) {
            final ConfigurationSection mailSec = section.getConfigurationSection(mailId);
            final Mail mail = new Mail(mailId, mailSec);
            add(mail);
        }
    }

    public String generateId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void createFile() {
        file = new File(plugin.getDataFolder(), "mail.yml");
        try {
            file.createNewFile();
        } catch (final IOException ex) {
            plugin.getLogger().warning(Chat.colourize("&cWarning! mail.yml could not be created.."));
            return;
        }
        filecfg = YamlConfiguration.loadConfiguration(file);
        if(!filecfg.contains("mail")) {
            filecfg.createSection("mail");
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            filecfg.save(file);
        } catch (IOException ex) {
            plugin.getLogger().warning(Chat.colourize("&cWarning! mail.yml could not be saved.."));
        }
    }

    private void registerPushRunnable(final MailboxPlugin plugin, final long delay, final long period) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                push();
            }
        };
        runnable.runTaskTimer(plugin, delay, period);
    }

}
