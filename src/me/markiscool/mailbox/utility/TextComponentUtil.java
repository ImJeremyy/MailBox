package me.markiscool.mailbox.utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtil {

    public static TextComponent generateTextComponent(String text, HoverEvent.Action hoverAction, ComponentBuilder hoverActionValue, ClickEvent.Action clickAction, String clickActionValue) {
        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
        component.setHoverEvent(new HoverEvent(hoverAction, hoverActionValue.create()));
        component.setClickEvent(new ClickEvent(clickAction, clickActionValue));
        return component;
    }

}