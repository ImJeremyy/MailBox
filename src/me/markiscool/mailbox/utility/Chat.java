package me.markiscool.mailbox.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static String colourize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> colourize(List<String> texts) {
        List<String> m = new ArrayList<>();
        for(String s : texts) {
            m.add(colourize(s));
        }
        return m;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(text);
    }

    public static List<String> strip(List<String> texts) {
        List<String> m = new ArrayList<>();
        for(String s : texts) {
            m.add(strip(s));
        }
        return m;
    }
}
