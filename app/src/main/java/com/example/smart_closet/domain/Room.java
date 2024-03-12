package com.example.smart_closet.domain;

public class Room {
    private int id;
    private String name;
    private int roomClothesNum;
    private String description;

    public Room(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomClothesNum() {
        return roomClothesNum;
    }

    public void setRoomClothesNum(int roomClothesNum) {
        this.roomClothesNum = roomClothesNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
