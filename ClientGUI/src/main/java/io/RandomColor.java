package io;

import javafx.scene.paint.Color;

import java.util.Random;

public class RandomColor {
    public static String getRandomColor(){
        Color user_color;

        Random random = new Random();

        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        user_color = Color.rgb(r,g,b);

        return user_color.toString();
    }
}