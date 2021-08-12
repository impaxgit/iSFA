package com.example.pauldavies.isfa;

public class Notifications
{
    int id;
    String subject;
    String content;
    String status;

    public Notifications(int id, String subject, String content, String status)
    {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
