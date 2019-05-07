package me.markiscool.mailbox.objects;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String username;
    private Set<Mail> createdMail;
    private Set<Mail> mailbox;
    private Set<UUID> blocked;

    /**
     * Constructor is for creating new users entirely
     * @param uuid uuid of the player
     * @param username username of the player
     */
    public User(final UUID uuid, final String username) {
        this.uuid = uuid;
        this.username = username;
        this.createdMail = new HashSet<>();
        this.mailbox = new HashSet<>();
        this.blocked = new HashSet<>();
    }

    /**
     * Constructor is for onJoin
     * @param player player to wrap
     */
    public User(final Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Set<Mail> getCreatedMail() {
        return createdMail;
    }

    public boolean addCreatedMail(final Mail mail) {
        return createdMail.add(mail);
    }

    public boolean removeCreatedMail(final Mail mail) {
        return createdMail.remove(mail);
    }

    public Set<Mail> getMailbox() {
        return mailbox;
    }

    public boolean addMail(final Mail mail) {
        return mailbox.add(mail);
    }

    public boolean removeMail(final Mail mail) {
        return mailbox.remove(mail);
    }

    public boolean block(final UUID uuid) {
        return blocked.add(uuid);
    }

    public boolean unblock(final UUID uuid) {
        return blocked.remove(uuid);
    }

    public boolean containsBlockedUser(final UUID uuid) {
        return blocked.contains(uuid);
    }

    public Set<UUID> getBlockedUsers() {
        return blocked;
    }

}
