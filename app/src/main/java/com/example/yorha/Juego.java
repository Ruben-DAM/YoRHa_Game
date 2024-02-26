package com.example.yorha;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener{
    private SurfaceHolder holder;
    public BucleJuego bucle;
    private Bitmap fondo;
    public Bitmap character;
    public Bitmap machine;
    private MediaPlayer music;
    private Context contexto;
    private Android android;
    private Joystick joystick;
    private boolean end = false;
    public int screenW;
    public int screenH;
    private int mapW;
    private int mapH;
    private int mapPosY;
    private int frame_count=0;
    private ArrayList<Machine> enemies = new ArrayList<>();
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
    }

    private void cargar() {
        music = MediaPlayer.create(contexto,R.raw.alien_manifestation);
        music.start();
        fondo = BitmapFactory.decodeResource(getResources(),R.drawable.bg_moon);
        character = BitmapFactory.decodeResource(getResources(),R.drawable.ho229);
        machine = BitmapFactory.decodeResource(getResources(),R.drawable.machine);
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
            frame_count++;
            //Move map
            mapPosY++;
            if(mapPosY >= mapH)
                mapPosY = 0;

            //Joystick movement
            joystick.move();

            //Move character
            android.move(joystick);
            
            //Spawn enemies
            if(frame_count == 30){
                enemies.add(new Machine((screenW-machine.getWidth())/2,100 + machine.getHeight(),this));
            }
        }
    }

    public void renderizar(Canvas canvas){
        canvas.drawColor(Color.WHITE);

        Rect rctSrcMap = new Rect(0,mapPosY, mapW, mapH+mapPosY);
        Rect rctDstMap = new Rect(0,0, screenW, screenH -400);
        canvas.drawBitmap(fondo, rctSrcMap, rctDstMap, null);

        joystick.draw(canvas);

        android.draw(canvas);

        for (Machine enemy:
             enemies) {
            enemy.draw(canvas);
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
                if(joystick.estaPulsado(x,y)){
                    joystick.pulsado = true;
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
                break;
        }
        return true;
    }
}
