package com.example.kidsgame;

import java.io.Serializable;

public class CardItem implements Serializable {
    private String name;
    private int image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(byte image) {
        this.image = image;
    }

    public CardItem() {
    }

    public CardItem(String name, int image) {
        this.name = name;
        this.image = image;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Player player = (Player) o;
//        return Objects.equals(name, player.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
}
