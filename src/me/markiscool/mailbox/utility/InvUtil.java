package me.markiscool.mailbox.utility;

public class InvUtil {

    public static int getPages(int amount) {
        int pages = (int) Math.ceil(amount / 45);
        if(pages == 0) {
            return 1;
        }
        return pages;
    }

}
