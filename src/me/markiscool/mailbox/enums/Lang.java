package me.markiscool.mailbox.enums;

import me.markiscool.mailbox.utility.Chat;

public enum Lang {

    PREFIX("&6[&bMailbox&6]&r "),
    NO_PERMISSION("&cYou do not have permission to this command."),
    NOT_A_PLAYER("&cYou are not a player."),
    PLAYER_NOT_FOUND("&cPlayer not found."),
    MAIL_NOT_FOUND("&cMail not found."),
    INVALID_ARGUMENTS("&cInvalid arguments."),
    NOT_CURRENTLY_RUNNING_TASK("&cYou are not currently running a task.."),
    MAILBOX_EMPTY("&cYour mailbox is empty"),
    CREATED_MAIL_EMPTY("&cYou have not created any mail yet!"),
    ;

    private String message;

    /**
     * Set commonly used messages. Constructor colourizes message already.
     * @param message Use & for chat color
     */
    Lang(String message) {
        this.message = Chat.colourize(message);
    }

    /**
     * @return String message (already colourized)
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set a Lang enum's message
     * @param message Use & for chat color
     */
    public void setMessage(String message) {
        this.message = Chat.colourize(message);
    }

}
