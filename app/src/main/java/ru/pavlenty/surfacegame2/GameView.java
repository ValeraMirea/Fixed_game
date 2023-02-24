package ru.pavlenty.surfacegame2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;

    private final Player player;
    private final Friend friend;
    private final Enemy enemy;
    private final Collision_of_ships collision_of_ships;
    private final DestroyPlayer destroy_Player;

    private final Paint paint;
    private final SurfaceHolder surfaceHolder;

    private final ArrayList<Star> stars = new ArrayList<>();

    int screenX;
    int Armor;
    int Safe_Friends = 0;

    private boolean isGameOver;

    int score;

    SharedPreferences sharedPreferences;

    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;
    final MediaPlayer addingpoints;

    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        score = 0;
        player = new Player(context, screenX, screenY); // Класс игрока
        friend = new Friend(context, screenX, screenY); // Класс друга
        enemy = new Enemy(context, screenX, screenY); // Класс Врага
        collision_of_ships = new Collision_of_ships(context); // Класс Столкновения
        destroy_Player = new DestroyPlayer(context);// Класс для уничтожения игрока

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        this.screenX = screenX;
        Armor = 10;
        isGameOver = false;


        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);


        int[] highScore = new int[4];
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);
        this.context = context;


        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);
        addingpoints = MediaPlayer.create(context,R.raw.addingpoints);
        gameOnsound.setLooping(true);
        gameOnsound.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;

        }

        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));

            }
        }
        return true;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);


            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }


            paint.setTextSize(30);
            canvas.drawText("Очки: " + score,100,50,paint);
            canvas.drawText("Броня Корабля: " + Armor, 300, 50,paint);
            canvas.drawText("Спасено друзей: " + Safe_Friends, 650, 50,paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint);

            canvas.drawBitmap(
                    enemy.getBitmap(),
                    enemy.getX(),
                    enemy.getY(),
                    paint);

            canvas.drawBitmap(
                    collision_of_ships.getBitmap(),
                    collision_of_ships.getX(),
                    collision_of_ships.getY(),
                    paint);


            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                paint.setColor(Color.RED);
                canvas.drawText("Вы разбились", canvas.getWidth() >> 1,yPos,paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize(100);
                canvas.drawText("Ваш счет: " + score + ". Спасено друзей: " + Safe_Friends, canvas.getWidth() >> 1,yPos + 200,paint);
                gameOversound.start();
                canvas.drawBitmap(destroy_Player.getBitmap(),player.getX() - 90,player.getY() - 150,paint);
                canvas.drawBitmap(player.getBitmap(),-10000,-10000,paint);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                int BestScore = sharedPreferences.getInt("score1",0);
                int MedScore_1 = sharedPreferences.getInt("score2",0);
                int MedScore_2 = sharedPreferences.getInt("score3",0);
                int MinScore;

                int TotalScore = score;

                if (TotalScore > BestScore){
                    MedScore_2 = MedScore_1;
                    MedScore_1 = BestScore;
                    BestScore = TotalScore;
                    editor.putInt("score1", BestScore);
                    editor.putInt("score2", MedScore_1);
                    editor.putInt("score3", MedScore_2);
                    editor.apply();
                }
                else if (TotalScore > MedScore_1){
                    MedScore_2 = MedScore_1;
                    MedScore_1 = TotalScore;
                    editor.putInt("score2", MedScore_1);
                    editor.putInt("score3", MedScore_2);
                    editor.apply();
                }
                else if (TotalScore > MedScore_2){
                    MinScore = MedScore_2;
                    MedScore_2 = TotalScore;
                    editor.putInt("score3", MedScore_2);
                    editor.putInt("score4", MinScore);
                    editor.apply();
                }
                else {
                    MinScore = TotalScore;
                    editor.putInt("score4", MinScore);
                    editor.apply();
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }


    public static void stopMusic(){
        gameOnsound.stop();
    }

    private void update() {
        score+=1;

        if (score >=1500){
            player.IncreeseSpeed();
        }
        if (score >=3500){
            enemy.update();
            score+=2;
        }
        if (score >=5700){
            player.IncreeseSpeed();
            friend.update();
        }
        if (score >=7600){
            player.IncreeseSpeed();
            score+=3;
        }
        if (score >=8400){
            player.IncreeseSpeed();
            enemy.update();
        }
        if (score >=9500){
            player.IncreeseSpeed();
            friend.update();

        }
        if (score >=15000){
            player.IncreeseSpeed();
            score+=4;
            enemy.update();
            friend.update();
        }
        player.update();
        friend.update();
        enemy.update();


        for (Star s : stars) {
            s.update(player.getSpeed());
        }
        collision_of_ships.setX(-450);
        collision_of_ships.setY(-450);

        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())) {
            collision_of_ships.setX(friend.getX());
            collision_of_ships.setY(friend.getY());
            addingpoints.start();
            friend.setX(-500);
            score += 200;
            Armor += 1;
            Safe_Friends+=1;
            if (Safe_Friends >= 10){
                Armor += 1;
            }
            if (Safe_Friends >=30){
                Armor+=3;
            }
        }
        if(Rect.intersects(player.getDetectCollision(),enemy.getDetectCollision())) {
            collision_of_ships.setX(enemy.getX());
            collision_of_ships.setY(enemy.getY());
            killedEnemysound.start();
            enemy.setX(-500);
            Armor-=3;
            Safe_Friends-=1;
            if (Safe_Friends<=0){
                Safe_Friends = 0;
            }
            if (Safe_Friends >= 15 && score >= 5000){
                Armor -= 2;
            }
            if (Safe_Friends >=30 && score >=14000){
                Armor-=5;
            }
            if (Armor <= 0) {
                Armor = 0;
                gameOnsound.stop();
                isGameOver = true;
                playing = false;
                gameOversound.start();
            }
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        gameOnsound.pause();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        gameOnsound.start();
    }
}