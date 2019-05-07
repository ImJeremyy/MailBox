package me.markiscool.mailbox.objecthandlers;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.objects.Mail;
import me.markiscool.mailbox.objects.User;
import me.markiscool.mailbox.utility.Chat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserHandler {

    private MailboxPlugin plugin;
    private MailHandler mh;

    private File file;
    private FileConfiguration cfg;

    private Set<User> users;

    public UserHandler(final MailboxPlugin plugin) {
        this.plugin = plugin;
        mh = plugin.getMailHandler();
        users = new HashSet<>();
        createFile();
        pull();
        registerPushRunnable(plugin, 40, plugin.getDelay());
    }

    public User getUser(final Player player) {
        for(final User u : users) {
            if(u.getUniqueId().equals(player.getUniqueId()) && u.getUsername().equals(player.getName())) {
                return u;
            }
        }
        return null;
    }

    public User getUser(final String playerName) {
        for(final User u : users) {
            if(u.getUsername().equalsIgnoreCase(playerName)) {
                return u;
            }
        }
        return null;
    }

    public User getUser(final UUID uuid) {
        for(final User u : users) {
            if(u.getUniqueId().equals(uuid)) {
                return u;
            }
        }
        return null;
    }

    public User getCreator(final Mail mail) {
        for(final User u : users) {
            for(final Mail m : u.getCreatedMail()) {
                if(m.equals(mail)) {
                    return u;
                }
            }
        }
        return null;
    }

    public boolean isRegistered(final Player player) {
        return getUser(player) != null;
    }

    public boolean containsName(final String username) {
        for(final User u : users) {
            if(u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(final User user) {
        return users.add(user);
    }

    public boolean remove(final User user) {
        return users.remove(user);
    }

    public boolean contains(final User user) {
        return users.contains(user);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void register(final Player player) {
        User user = null;
        if(getUser(player.getUniqueId()) == null) {
            user = new User(player);
        } else {
            user = getUser(player.getUniqueId());
            remove(user);
            user.setUsername(player.getName());
        }
        add(user);
    }

    public void pull() {
        for(String uuidStr : cfg.getConfigurationSection("users").getKeys(false)) {
            final ConfigurationSection section = cfg.getConfigurationSection("users." + uuidStr);
            final UUID uuid = UUID.fromString(uuidStr);
            final String username = section.getString("username");
            final User user = new User(uuid, username);
            final List<String> mailbox = section.getStringList("mailbox");
            if(!mailbox.isEmpty()) {
                for (final String mailId : mailbox) {
                    Mail mail = mh.getMail(mailId);
                    if(mail != null) {
                        user.addMail(mail);
                    }
                }
            }
            final List<String> createdMail = section.getStringList("createdmail");
            if(!createdMail.isEmpty()) {
                for(final String mailId : createdMail) {
                    Mail mail = mh.getMail(mailId);
                    if(mail != null) {
                        user.addCreatedMail(mail);
                    }
                }
            }
            final List<String> blocked = section.getStringList("blocked");
            if(!blocked.isEmpty()) {
                for(final String blockedUuid : blocked) {
                    UUID u = UUID.fromString(blockedUuid);
                    user.block(u);
                }
            }
            add(user);
        }
    }

    public void push() {
        cfg.set("users", null);
        cfg.createSection("users");
        for(final User user : users) {
            final UUID uuid = user.getUniqueId();
            final String username = user.getUsername();
            final Set<Mail> mailbox = user.getMailbox();
            final Set<Mail> createdMail = user.getCreatedMail();
            final Set<UUID> blocked = user.getBlockedUsers();
            final List<String> mailboxStr = new ArrayList<>();
            final List<String> createdMailStr = new ArrayList<>();
            final List<String> blockedStr = new ArrayList<>();
            for (final Mail mail : mailbox) {
                mailboxStr.add(mail.getId());
            }
            for(final Mail mail : createdMail) {
                createdMailStr.add(mail.getId());
            }
            for(final UUID u : blocked) {
                blockedStr.add(u.toString());
            }
            final ConfigurationSection section = cfg.createSection("users." + uuid.toString());
            section.set("username", username);
            section.set("mailbox", mailboxStr);
            section.set("createdmail", createdMailStr);
            section.set("blocked", blockedStr);
        }
        saveConfig();
    }

    private void createFile() {
        file = new File(plugin.getDataFolder(), "users.yml");
        try {
            file.createNewFile();
        } catch (final IOException ex) {
            plugin.getLogger().warning(Chat.colourize("&cCould not create users.yml"));
        }
        cfg = YamlConfiguration.loadConfiguration(file);
        if(!cfg.contains("users")) {
            cfg.createSection("users");
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            cfg.save(file);
        } catch (final IOException ex) {
            plugin.getLogger().warning(Chat.colourize("&cCould not save users.yml"));
        }
    }

    private void registerPushRunnable(final Plugin plugin, final long delay, final long period) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                push();
            }
        };
        runnable.runTaskTimer(plugin, delay, period);
    }

}
