package me.markiscool.mailbox;

import me.markiscool.mailbox.commands.MailCommand;
import me.markiscool.mailbox.commands.MailFinishedCommand;
import me.markiscool.mailbox.listeners.*;
import me.markiscool.mailbox.objecthandlers.InventoryPageHandler;
import me.markiscool.mailbox.objecthandlers.MailHandler;
import me.markiscool.mailbox.objecthandlers.TaskHandler;
import me.markiscool.mailbox.objecthandlers.UserHandler;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MailboxPlugin extends JavaPlugin {

    private MailHandler mailHandler;
    private UserHandler userHandler;
    private TaskHandler taskHandler;
    private InventoryPageHandler inventoryPageHandler;

    @Override
    public void onEnable() {
        registerHandlers();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        mailHandler.push();
        userHandler.push();
    }

    private void registerHandlers() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        mailHandler = new MailHandler(this);
        userHandler = new UserHandler(this);
        taskHandler = new TaskHandler(this);
        inventoryPageHandler = new InventoryPageHandler(this);
        if(!getConfig().contains("debug-mode")) {
            getConfig().set("debug-mode", false);
            saveConfig();
        }
    }

    private void registerCommands() {
        final Map<String, CommandExecutor> commands = new HashMap<>();
        commands.put("mail", new MailCommand(this));
        commands.put("mailfinished", new MailFinishedCommand(this));
        for(Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
            getCommand(entry.getKey()).setExecutor(entry.getValue());
        }
    }

    private void registerListeners() {
        Object[] listeners = {
                new AsyncChatListener(this),
                new ClickListener(this),
                new JoinListener(this),
                new LeaveListener(this),
                new InventoryCloseListener(this),
                };
        PluginManager pm = getServer().getPluginManager();
        for(Object o : listeners) {
            pm.registerEvents((Listener) o, this);
        }
    }

    public MailHandler getMailHandler() {
        return mailHandler;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public TaskHandler getTaskHandler() {
        return taskHandler;
    }

    public InventoryPageHandler getInventoryPageHandler() {
        return inventoryPageHandler;
    }

    public long getDelay() {
        return getConfig().getBoolean("debug-mode") ? 20 : 600;
    }
}
