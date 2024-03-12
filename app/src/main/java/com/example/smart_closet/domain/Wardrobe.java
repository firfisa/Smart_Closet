package com.example.smart_closet.domain;

import java.util.List;

public class Wardrobe {
        private int id;
        private String name;
        private int clothesNum;
        private String description;
        private int roomId;

        public void setClothesNum(int clothesNum) {
                this.clothesNum = clothesNum;
        }

        public int getRoomId() {
                return roomId;
        }

        public void setRoomId(int roomId) {
                this.roomId = roomId;
        }

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

        public int getClothesNum() {
                return clothesNum;
        }

        public void setWardrobeClothesNum(int wardrobeClothesNum) {
                clothesNum = wardrobeClothesNum;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }
}
