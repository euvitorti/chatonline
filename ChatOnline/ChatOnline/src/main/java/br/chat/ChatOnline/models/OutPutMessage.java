package br.chat.ChatOnline.models;

public class OutPutMessage{

    String from;
    String text;

    public OutPutMessage(String from, String text) {
        this.from = from;
        this.text = text;
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

}

