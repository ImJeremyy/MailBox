package me.markiscool.mailbox.objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Mail {

    private String id;
    private String title;
    private List<String> message;

    private UUID creator;
    private Map<UUID, Long> recipients;
    private Set<UUID> receivedItemRecipients;

    private ItemStack item;

    public Mail(String id, final ConfigurationSection cfgsection) {
        this.id = id;
        this.title = cfgsection.getString("title");
        this.message = cfgsection.getStringList("message");
        this.creator = UUID.fromString(cfgsection.getString("creator"));
        this.recipients = new HashMap<>();
        this.receivedItemRecipients = new HashSet<>();
        final ConfigurationSection recipientsSec = cfgsection.getConfigurationSection("recipients");
        for(final String u : recipientsSec.getKeys(false)) {
            final UUID uuid = UUID.fromString(u);
            final long timeStamp = recipientsSec.getLong(u);
            recipients.put(uuid, timeStamp);
        }
        for(final String u : cfgsection.getStringList("receiveditemrecipients")) {
            final UUID uuid = UUID.fromString(u);
            receivedItemRecipients.add(uuid);
        }
        item = cfgsection.getItemStack("item");
    }

    public Mail(String id, UUID creator) {
        this.id = id;
        this.title = "";
        this.message = new ArrayList<>();
        this.creator = creator;
        this.recipients = new HashMap<>();
        this.receivedItemRecipients = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public UUID getCreator() {
        return creator;
    }

    public Map<UUID, Long> getRecipients()  {
        return recipients;
    }

    public void setRecipients(Map<UUID, Long> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(UUID uuid, long timestamp) {
        recipients.put(uuid, timestamp);
    }

    public boolean containsRecipient(UUID uuid) {
        return recipients.containsKey(uuid);
    }

    public boolean removeRecipient(UUID uuid) {
        if(recipients.containsKey(uuid)) {
            recipients.remove(uuid);
            return true;
        }
        return false;
    }

    public long getTimestamp(UUID uuid) {
        return recipients.get(uuid);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean hasItem() {
        return item != null;
    }

    public void addReceivedItemRepient(Player player) {
        this.receivedItemRecipients.add(player.getUniqueId());
    }

    public boolean hasReceivedItem(Player player) {
        return receivedItemRecipients.contains(player.getUniqueId());
    }

    public Set<UUID> getReceivedItemRecipients() {
        return receivedItemRecipients;
    }

}
