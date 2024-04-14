package com.example.firebaseproject.Modals;

public class Users {
    String prfilepic,username,mail,password,userId,lastMessage;

    public Users(String prfilepic, String username, String mail, String password, String userId, String lastMessage) {
        this.prfilepic = prfilepic;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;

    }
    public Users(){

    }

    //signupConstruction'
    public Users( String username, String mail, String password) {

        this.username = username;
        this.mail = mail;
        this.password = password;


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrfilepic() {
        return prfilepic;
    }

    public void setPrfilepic(String prfilepic) {
        this.prfilepic = prfilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
