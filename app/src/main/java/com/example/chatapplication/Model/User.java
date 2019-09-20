package com.example.chatapplication.Model;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String isOnline;
    private String search;

    public User() {
    }

    public User(String id, String username, String imageURL, String isOnline, String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.isOnline = isOnline;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
