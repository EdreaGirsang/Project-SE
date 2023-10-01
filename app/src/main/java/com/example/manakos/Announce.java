package com.example.manakos;

public class Announce {
    String Title;
    String Content;

    public Announce(){}

    public Announce(String Title, String Content) {
        this.Title = Title;
        this.Content = Content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
