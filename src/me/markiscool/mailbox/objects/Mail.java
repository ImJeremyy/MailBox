package me.markiscool.mailbox.objects;

import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Mail {

    private String id;
    private String title;
    private List<String> message;

    private UUID creator;
    private Map<UUID, Long> recipients;

    public Mail(String id, final ConfigurationSection cfgsection) {
        this.id = id;
        this.title = cfgsection.getString("title");
        this.message = cfgsection.getStringList("message");
        this.creator = UUID.fromString(cfgsection.getString("creator"));
        this.recipients = new HashMap<>();
        final ConfigurationSection recipientsSec = cfgsection.getConfigurationSection("recipients");
        for(String u : recipientsSec.getKeys(false)) {
            UUID uuid = UUID.fromString(u);
            long timeStamp = recipientsSec.getLong(u);
            recipients.put(uuid, timeStamp);
        }
    }

    public Mail(String id, UUID creator) {
        this.id = id;
        this.title = "";
        this.message = new ArrayList<>();
        this.creator = creator;
        this.recipients = new HashMap<>();
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

}
