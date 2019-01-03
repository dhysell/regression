package repository.gw.generate.custom;

import repository.gw.enums.Topic;

public class Note {
    private String author;
    private repository.gw.enums.Topic topic;
    private String securityLevel;
    private String subject;
    private String relatedTo = "Account";
    private String text;
    private String date;

    public Note() {
    }

    public Note(repository.gw.enums.Topic topic, String subject, String relatedTo, String text) {
        this.topic = topic;
        this.subject = subject;
        this.relatedTo = relatedTo;
        this.text = text;
    }

    public Note(String author, repository.gw.enums.Topic topic, String securityLevel, String subject, String relatedTo, String text, String date) {
        this.author = author;
        this.topic = topic;
        this.securityLevel = securityLevel;
        this.subject = subject;
        this.relatedTo = relatedTo;
        this.text = text;
        this.date = date;
    }

    public repository.gw.enums.Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
