package ru.pavlenty.surfacegame2;

import java.util.Random;


public class Star {
    private int x;
    private int y;
    private int speed;

    private final int maxX;
    private final int maxY;


    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        Random generator = new Random();
        speed = generator.nextInt(10);


        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed) {

        x -= playerSpeed;
        x -= speed;

        if (x < 0) {

            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }

    public float getStarWidth() {

        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        return rand.nextFloat()* (maxX - minX) + minX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
