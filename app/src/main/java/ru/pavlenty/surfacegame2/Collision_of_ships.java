package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Collision_of_ships {
    private final Bitmap bitmap;
    private int x;
    private int y;

    public Collision_of_ships(Context context) {
        x = -200;
        y = -200;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
