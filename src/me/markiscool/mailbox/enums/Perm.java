package me.markiscool.mailbox.enums;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Perm {

    public static Permission MAIL;

    static {
        MAIL = new Permission("mailbox.mail");
        MAIL.setDefault(PermissionDefault.OP);
    }

}
