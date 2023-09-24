package com.example.manakos;

public class pending {
    String Message;
    String RoomNumber;
    int condition;

    int type;

    public pending(){}

    public pending(String message, String roomNumber, int condition, int type) {
        Message = message;
        RoomNumber = roomNumber;
        this.condition = condition;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        RoomNumber = roomNumber;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
