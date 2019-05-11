package me.markiscool.mailbox.objects;

public class Task {

    private Job job;
    private Mail mail;

    public Task(Job job) {
        this.job = job;
    }

    public Task(Job job, Mail mail) {
        this.job = job;
        this.mail = mail;
    }

    public Job getJob() {
        return job;
    }

    public Mail getMail() {
        return mail;
    }

    public enum Job {
        TITLE, MESSAGES, SEND, BLOCK, ITEM,
        ;
    }

}
