package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Friend {
    private int x;
    private int y;
    private int speed;

    private final int maxX;
    private final int maxY;

    private final Bitmap bitmap;

    private final Rect detectCollision;



    public Friend(Context context, int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        Random generator = new Random();
        speed = ThreadLocalRandom.current().nextInt(1,10);

        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.friend);

        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        x -= speed;

        if (x + bitmap.getWidth() < 0) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = 10 + generator.nextInt(15);
        }
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
