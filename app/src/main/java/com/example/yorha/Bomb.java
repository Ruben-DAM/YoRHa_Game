package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Bomb {
    boolean pulsado;
    boolean charged = true;
    private Juego juego;
    public int posX,posY,ancho,alto,centerX,centerY,estado,expW,expH;
    public Bomb(Juego j){
        this.juego = j;
        ancho = juego.pod.getWidth();
        alto = juego.pod.getHeight();
        expW = juego.bomb_sheet.getWidth()/9;
        expH = juego.bomb_sheet.getHeight();
        posX = ancho+ juego.screenW/100;
        posY = (int)(juego.screenH-alto*1.5);
        centerX = posX + ancho/2;
        centerY = posY + alto/2;
        estado = -1;
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setAlpha(50);
        if(charged)
            canvas.drawBitmap(juego.pod,posX,posY, null);
        else
            canvas.drawBitmap(juego.pod,posX,posY, p);
        p.setAlpha(100);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        p.setStrokeWidth(10);
        canvas.drawCircle(centerX,centerY,ancho,p);
    }

    public boolean estaPulsado(int x, int y) {
        int distanceX = centerX - x;
        int distanceY = centerY - y;
        double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        return distance <= ancho;
    }

    public void drawExplosion(Canvas canvas) {
        Log.d("EXP",estado+"");
        canvas.drawBitmap(juego.bomb_sheet,
                new Rect( (estado*expW),0,
                         (expW + estado*expW),expH),
                new Rect(0,0,juego.screenW,juego.screenH-400),
                null);
    }
}
