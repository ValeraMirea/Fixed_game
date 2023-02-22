package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Collision_of_ships {
    private Bitmap bitmap;
    private int x;
    private int y;

    public Collision_of_ships(Context context) {
        x = -1000;
        y = -1000;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
