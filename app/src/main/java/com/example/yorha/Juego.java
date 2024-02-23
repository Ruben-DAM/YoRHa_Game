package com.example.yorha;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Juego extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    public BucleJuego bucle;
    private Bitmap fondo;
    private int anchoPantalla;
    private int altoPantalla;
    private int anchoMapa;
    private int altoMapa;

    public Juego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        cargar();
    }

    private void incializar() {
        Canvas canvas = getHolder().lockCanvas();
        anchoPantalla = canvas.getWidth();
        altoPantalla = canvas.getHeight();
        getHolder().unlockCanvasAndPost(canvas);

        altoMapa = fondo.getHeight();
        anchoMapa = fondo.getWidth();
    }

    private void cargar() {
        fondo = BitmapFactory.decodeResource(getResources(),R.drawable.bg_moon);
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

    }

    public void renderizar(Canvas canvas){
        Rect rctSrcMap = new Rect(0,0,anchoMapa,altoMapa);
        Rect rctDstMap = new Rect(0,0,anchoPantalla,altoPantalla-400);
        canvas.drawBitmap(fondo, rctSrcMap, rctDstMap, null);

    }
}
