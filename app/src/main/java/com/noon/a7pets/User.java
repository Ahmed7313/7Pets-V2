package com.noon.a7pets;

public class User {

    private String name;
    private String password;
    private String mobile;
    private String email;
    private String photo;


    public User(String name, String password, String mobile, String email, String photo) {
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.photo = photo;
    }

    public User(String username, String phoneNumber, String email, String profileImageUrl) {
        this.name = username;
        this.mobile = phoneNumber;
        this.email = email;
        this.photo = profileImageUrl;
    }

    public User(String username, String phoneNumber, String email) {
        this.name = username;
        this.mobile = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
