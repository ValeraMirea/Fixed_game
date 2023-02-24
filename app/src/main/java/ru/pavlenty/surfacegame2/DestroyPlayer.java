package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DestroyPlayer {
    private final Bitmap bitmap;
    private int x;
    private int y;

    public DestroyPlayer(Context context) {
        x = -200;
        y = -200;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyofplayer);
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
