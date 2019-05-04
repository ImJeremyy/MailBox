package me.markiscool.mailbox.objecthandlers;

import me.markiscool.mailbox.MailboxPlugin;
import me.markiscool.mailbox.objects.Task;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TaskHandler {

    private MailboxPlugin plugin;
    private Map<Player, Task> tasks;

    public TaskHandler(final MailboxPlugin plugin) {
        this.plugin = plugin;
        tasks = new HashMap<>();
    }

    public boolean contains(final Player player) {
        return tasks.containsKey(player);
    }

    public void remove(final Player player) {
        tasks.remove(player);
    }

    public void add(final Player player, final Task task) {
        tasks.put(player, task);
    }

    public Task getTask(final Player player) {
        return tasks.get(player);
    }

}
