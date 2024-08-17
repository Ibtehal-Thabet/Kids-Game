package com.example.kidsgame;

import java.util.ArrayList;
import java.util.Arrays;

public interface Constants {
    ArrayList<CardItem> animals = new ArrayList<>(Arrays.asList(
            new CardItem("Lion", R.drawable.lion),
            new CardItem("Cat", R.drawable.cat),
            new CardItem("Cow", R.drawable.cow),
            new CardItem("Elephant", R.drawable.elephant),
            new CardItem("Dog", R.drawable.dog),
            new CardItem("Fox", R.drawable.fox),
            new CardItem("Giraffe", R.drawable.giraffe),
            new CardItem("Fish", R.drawable.fish),
            new CardItem("Rabbit", R.drawable.rabbit),
            new CardItem("Monkey", R.drawable.monkey)
    ));

    ArrayList<CardItem> clothes = new ArrayList<>(Arrays.asList(
            new CardItem("Cap", R.drawable.cap),
            new CardItem("Bag", R.drawable.bag),
            new CardItem("Belt", R.drawable.belt),
            new CardItem("Dress", R.drawable.dress),
            new CardItem("T-shirt", R.drawable.t_shirt),
            new CardItem("Gloves", R.drawable.gloves),
            new CardItem("Hat", R.drawable.hat),
            new CardItem("Suit", R.drawable.suit),
            new CardItem("Pants", R.drawable.pants),
            new CardItem("Socks", R.drawable.socks),
            new CardItem("Shoes", R.drawable.shoes)
    ));

    ArrayList<CardItem> fruits = new ArrayList<>(Arrays.asList(
            new CardItem("Banana", R.drawable.banana),
            new CardItem("Strawberry", R.drawable.strawberry),
            new CardItem("Apple", R.drawable.apple),
            new CardItem("Watermelon", R.drawable.watermelon),
            new CardItem("Pear", R.drawable.pear),
            new CardItem("Cherry", R.drawable.cherry),
            new CardItem("Mango", R.drawable.mango),
            new CardItem("Orange", R.drawable.orange)
    ));

    ArrayList<CardItem> vegetables = new ArrayList<>(Arrays.asList(
            new CardItem("Potato", R.drawable.potato),
            new CardItem("Tomato", R.drawable.tomato),
            new CardItem("Pepper", R.drawable.pepper),
            new CardItem("Onion", R.drawable.onion),
            new CardItem("Mushroom", R.drawable.mushroom),
            new CardItem("Eggplant", R.drawable.eggplant),
            new CardItem("Broccoli", R.drawable.broccoli),
            new CardItem("Carrot", R.drawable.carrot),
            new CardItem("Cucumber", R.drawable.cucumber)
    ));
}
