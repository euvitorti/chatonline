package br.chat.ChatOnline.models;

public class OutPutMessage{

    private String from;
    private String text;
    private String time;

    public OutPutMessage(String from, String text, String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }

    public OutPutMessage(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
