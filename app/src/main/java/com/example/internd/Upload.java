package com.example.internd;

public class Upload {
    public String name;
    public String url;
    public String user;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public Upload(String name, String url, String user) {
        this.name = name;
        this.url = url;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
