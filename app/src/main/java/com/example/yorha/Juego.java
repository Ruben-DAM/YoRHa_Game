package com.example.yorha;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener{
    private SurfaceHolder holder;
    public BucleJuego bucle;
    private Bitmap fondo;
    public Bitmap character;
    public Bitmap machine;
    public Bitmap powerup;
    public Bitmap bomb_sheet;
    public Bitmap pod;
    public Bitmap boss;
    private MediaPlayer music;
    private Context contexto;
    public Android android;
    private Joystick joystick;
    private Bomb bomb;
    private boolean end = false;
    public int screenW;
    public int screenH;
    private int mapW;
    private int mapH;
    private int mapPosY;
    private int frame_count=0;
    private int frames_to_shoot = 0;
    private int ch_fire_rate = BucleJuego.MAX_FPS/3;
    private final int MAX_FIRE_RATE = BucleJuego.MAX_FPS/6;
    private ArrayList<Machine> enemies = new ArrayList<>();
    private ArrayList<Bullet> character_bullets = new ArrayList<>();
    private ArrayList<Bullet> enemy_bullets = new ArrayList<>();
    private ArrayList<PowerUp> powerups = new ArrayList<>();
    private boolean explosion = false;
    private int frame_exp = 3;
    private boolean win;
    private int points = 0;
    private int lvl = 1;
    private boolean spawn;
    private MachineBoss finalenemy;
    private boolean damaged = false;
    private Bitmap life;

    public Juego(AppCompatActivity context) {
        super(context);
        contexto = context;
        holder = getHolder();
        holder.addCallback(this);

        cargar();
        setOnTouchListener(this);
    }

    private void incializar() {
        Canvas canvas = getHolder().lockCanvas();
        screenW = canvas.getWidth();
        screenH = canvas.getHeight();
        getHolder().unlockCanvasAndPost(canvas);

        mapH = fondo.getHeight()/2;
        mapW = fondo.getWidth();
        mapPosY = 0;
        android = new Android(this);
        joystick = new Joystick(this);
        bomb = new Bomb(this);
    }

    private void cargar() {
        music = MediaPlayer.create(contexto,R.raw.alien_manifestation);
        music.start();
        fondo = BitmapFactory.decodeResource(getResources(),R.drawable.bg_moon);
        character = BitmapFactory.decodeResource(getResources(),R.drawable.ho229);
        machine = BitmapFactory.decodeResource(getResources(),R.drawable.machine);
        powerup = BitmapFactory.decodeResource(getResources(),R.drawable.powerup);
        bomb_sheet = BitmapFactory.decodeResource(getResources(),R.drawable.bombspritesheet);
        pod =  BitmapFactory.decodeResource(getResources(),R.drawable.pod);
        boss = BitmapFactory.decodeResource(getResources(),R.drawable.bossfinal);
        life = BitmapFactory.decodeResource(getResources(),R.drawable.life);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        bucle.start();

        incializar();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public void actualizar(){
        if(!end){
            //Check level
            if(enemies.isEmpty() && frame_count>60){
                lvl++;
                spawn = true;
            }
            frame_count++;
            //Move map
            mapPosY++;
            if(mapPosY >= mapH)
                mapPosY = 0;

            //Joystick movement
            joystick.move();

            //Move character
            android.move(joystick);

            //Make character shoot
            if(frames_to_shoot == 0){
                shootPattern();
                frames_to_shoot = ch_fire_rate;
            }
            frames_to_shoot--;
            
            //Spawn enemies
            if(frame_count == 30){
                enemies.add(new Machine((screenW-machine.getWidth())/2,10 + machine.getHeight(),this));
            }
            if(lvl==2 & spawn){
                spawn=false;
                enemies.add(new Machine((screenW*1/10)+machine.getWidth()/2,10+machine.getHeight(),this));
                enemies.add(new Machine((screenW*9/10)-machine.getHeight()/2, 10+machine.getHeight(),this));
            }
            if(lvl==3 & spawn){
                spawn=false;
                enemies.add(new Machine((screenW*1/10)+machine.getWidth()/2,100+machine.getHeight(),this));
                enemies.add(new Machine((screenW*9/10)-machine.getHeight()/2, 100+machine.getHeight(),this));
                finalenemy = new MachineBoss((screenW-machine.getWidth())/2,10 + machine.getHeight(),this);
            }

            //Move character bullets
            for(Iterator<Bullet> bullets = character_bullets.iterator();bullets.hasNext();){
                Bullet bullet = bullets.next();
                bullet.move(frame_count);
                if(bullet.outOfBounds())
                    bullets.remove();
            }

            //Move power up
            for(Iterator<PowerUp> it_p = powerups.iterator();it_p.hasNext();){
                Bullet bullet = it_p.next();
                bullet.move(frame_count);
                if(bullet.outOfBounds())
                    it_p.remove();
            }

            //Check collisions
            for(Iterator<Bullet> bullets = character_bullets.iterator();bullets.hasNext();){
                Bullet b = bullets.next();
                for(Iterator<Machine> it_enemies = enemies.iterator();it_enemies.hasNext();){
                    Machine e = it_enemies.next();
                    if(colision(b.posX,b.posY,b.getRadius(),e.getPosX(),e.getPosY(),e.getRadius())){
                        points++;
                        try{
                        bullets.remove();
                        if(e.damage()) {
                            powerups.add(new PowerUp(this,e.getPosX(),e.getPosY(),
                                    screenH/10/BucleJuego.MAX_FPS,Math.toRadians(90),0,30,0));
                            it_enemies.remove();
                        }}catch (Exception ex){}
                    }
                }
                if(finalenemy!=null){
                    if(colision(b.posX,b.posY,b.getRadius(),finalenemy.getPosX(),finalenemy.getPosY(),finalenemy.getRadius())){
                        try {
                            bullets.remove();
                            if(finalenemy.damage()){
                                end = true;
                                win = true;
                            }
                        } catch (Exception ex){}
                    }
                }
            }
            for(Iterator<PowerUp> it_p = powerups.iterator();it_p.hasNext();){
                PowerUp p = it_p.next();
                if(colision(android.getCenterX(),android.getCenterY(),character.getWidth(),p.posX,p.posY,p.getRadius())){
                    bomb.charged = true;
                    if(ch_fire_rate > MAX_FIRE_RATE)
                        ch_fire_rate--;
                    try{ it_p.remove();} catch(Exception ex){}
                }
            }

            //Use speacial attack
            if(bomb.pulsado && bomb.charged){
                enemy_bullets.clear();
                explosion = true;
                bomb.charged = false;
            }
            if(explosion) {
                if(frame_exp==0) {
                    frame_exp = 3;
                    bomb.estado++;
                } else {
                    frame_exp--;
                }
            }
            if(bomb.estado == 9) {
                explosion = false;
            }

            //Enemies shoot
            for (Machine enemy :enemies) {
                if (enemy.cooldown == BucleJuego.MAX_FPS){
                    if (enemy.f_to_shoot == 0) {
                        shootPatternEnemy(enemy);
                        enemy.f_to_shoot = MAX_FIRE_RATE;
                        enemy.n_bullets++;
                        if(enemy.n_bullets == 6){
                            enemy.cooldown = 0;
                            enemy.n_bullets = 0;
                        }
                    }
                    enemy.f_to_shoot--;
                    Log.d("disparo",enemy.f_to_shoot +": cooldown");
                } else {
                    enemy.cooldown++;
                }
            }
            if(finalenemy != null){
                if(finalenemy.f_to_shoot == 0) {
                    shootPatternFinalEnemy(finalenemy);
                    finalenemy.f_to_shoot = BucleJuego.MAX_FPS;
                } else{
                    finalenemy.f_to_shoot--;
                }

            }
            //Check collision with enemy bullets
            for(Iterator<Bullet> bullets = enemy_bullets.iterator();bullets.hasNext();){
                Bullet bullet = bullets.next();
                bullet.move(frame_count);
                if(colision(bullet.posX,bullet.posY,bullet.getRadius(),android.centerX,android.centerY, android.radius)) {
                    bullets.remove();
                    damaged = true;
                    if(android.damage()){
                        end = true;
                        win = false;
                    }
                }
                if(bullet.outOfBounds())
                    bullets.remove();
            }
        }
    }

    private void shootPatternFinalEnemy(MachineBoss finalenemy) {
        double vel = screenH/12/BucleJuego.MAX_FPS;
        int n_angles = 12;
        double initAngle = Math.toRadians(280);; // Ángulo inicial en grados
        double finAngle = Math.toRadians(420); // Ángulo final en grados

        // Iterar sobre los ángulos en radianes desde el ángulo inicial hasta el ángulo final
        for (int i = 0; i < n_angles; i++) {
            double angle = initAngle + ((finAngle - initAngle) * i) / (n_angles - 1);
            enemy_bullets.add(new EnemyBullet(this,finalenemy.centerX,finalenemy.centerY,vel,angle,Color.RED,15,frame_count));
        }
    }

    private void shootPatternEnemy(Machine m) {
        int x1 = m.centerX, y1 = m.centerY;
        int x2 = android.centerX, y2 = android.centerY;

        int distanceX = x2 - x1;
        int distanceY = y2 - y1;

        // Calcular el ángulo en radianes entre los dos vectores
        double angle = Math.atan2(distanceX,distanceY);
        double vel = screenH/8/BucleJuego.MAX_FPS;
        enemy_bullets.add(new EnemyBullet(this,m.centerX,m.centerY,vel,angle,Color.RED,20,frame_count));
        Log.d("disparo","angulo: "+angle);
    }

    private boolean colision(int posX1, int posY1, int radius1, int posX2, int posY2, int radius2) {
        int distanceX = posX1 - posX2;
        int distanceY = posY1 - posY2;
        double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        return distance <= radius1+radius2;
    }

    private void shootPattern() {
        double vel = screenH/3/BucleJuego.MAX_FPS;
        for (int i = 6; i < 13; i++) {
            double spawnangle = Math.toRadians(30 * i);
            double angle = Math.toRadians(30*((i/7.9)+7.9));
            int bulletX = (int)(70*Math.cos(spawnangle)+android.getCenterX());
            int bulletY = (int)(character.getHeight()/2*Math.sin(spawnangle)+android.getCenterY());
            character_bullets.add(new Bullet(this,bulletX,bulletY,vel,angle,Color.WHITE,15,frame_count));
        }
    }

    public void render(Canvas canvas){
        if(!end) {
            canvas.drawColor(Color.WHITE);

            Rect rctSrcMap = new Rect(0, mapPosY, mapW, mapH + mapPosY);
            Rect rctDstMap = new Rect(0, 0, screenW, screenH - 400);
            canvas.drawBitmap(fondo, rctSrcMap, rctDstMap, null);

            joystick.draw(canvas);
            bomb.draw(canvas);

            if(damaged) {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setAlpha(25);
                android.draw(canvas,paint);
                damaged=false;
            } else
                android.draw(canvas, null);

            for (int i = 0; i <= android.life; i++) {
                canvas.drawBitmap(life,(screenW-life.getHeight())/2,screenH-(i*life.getHeight()),null);
            }

            for (Machine enemy : enemies)
                enemy.draw(canvas);

            if(finalenemy != null)
                finalenemy.draw(canvas);

            for (Bullet bullet : character_bullets)
                bullet.draw(canvas);

            for (PowerUp p : powerups)
                p.draw(canvas);

            for (Bullet b : enemy_bullets) {
                b.draw(canvas);
            }

            if (explosion)
                bomb.drawExplosion(canvas);
        } else{
            if(!win) {
                canvas.drawColor(Color.BLACK);
                Paint p = new Paint();
                p.setColor(getResources().getColor(R.color.YorHa_blue));
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(5);
                p.setTextSize(50);
                canvas.drawText("Ending Ñ: estamos apa[Ñ]ados",100,screenH/2,p);
            } else {
                canvas.drawColor(Color.BLACK);
                Paint p = new Paint();
                p.setColor(getResources().getColor(R.color.YorHa_blue));
                p.setStyle(Paint.Style.FILL_AND_STROKE);
                p.setStrokeWidth(5);
                p.setTextSize(50);
                canvas.drawText("Ending A: flowers for m[A]chines",100,screenH/2,p);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x,y;

        // Obtener el pointer asociado con la acción
        index = event.getActionIndex();

        x = (int) event.getX(index);
        y = (int) event.getY(index);
        Log.d("Pulsacion","Pulsado en "+x+","+y);
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.estaPulsado(x,y)){
                    joystick.pulsado = true;
                }
                if(bomb.estaPulsado(x,y)){
                    bomb.pulsado = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(joystick.pulsado){
                    joystick.actualizar(x,y);
                }
                break;
            case MotionEvent.ACTION_UP:
                joystick.pulsado = false;
                joystick.reset();
                bomb.pulsado = false;
                break;
        }
        return true;
    }
}
