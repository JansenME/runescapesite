package com.runescape.info.model;

/**
 * Created by Martijn Jansen on 6/17/2017.
 */
public class AdventurersLogInList {
    private String formattedDate;
    private String title;
    private String description;

    public AdventurersLogInList(String formattedDate, String title, String description) {
        this.formattedDate = formattedDate;
        this.title = title;
        this.description = description;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
