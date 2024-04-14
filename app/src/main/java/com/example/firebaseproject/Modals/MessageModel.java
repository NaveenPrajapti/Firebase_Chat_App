package com.example.firebaseproject.Modals;

public class MessageModel {
    String uId, message;
    Long timestamp;

    //constructor id, message, timestamp
    public MessageModel(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    //constructor id, message
    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }



    public  MessageModel() {}// default constructor


    //setter and getter
        public String getuId() {
        return uId;
    }

        public void setuId(String uId) {
        this.uId = uId;
    }

        public String getMessage() {
        return message;
    }

        public void setMessage(String message) {
        this.message = message;
    }

        public Long getTimestamp() {
        return timestamp;
    }

        public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
