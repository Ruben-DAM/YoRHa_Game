package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {
    public int posX, posY;
    private Juego juego;
    private float velY;
    private float velX;
    private int creation_frame;
    public int damage;
    public int radius = 10;

    public Bullet(Juego juego,int posX, int posY, int frame, int damage) {
        this.posX = posX;
        this.posY = posY;
        this.juego = juego;
        this.creation_frame = frame;
        this.damage = damage;

        velX = (posX-(juego.android.getCenterX()))*juego.screenW/100/BucleJuego.MAX_FPS;
        velY = juego.screenH/2/BucleJuego.MAX_FPS;
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawCircle(posX,posY,radius,p);
    }

    public void move(int actual_frame){
        if(actual_frame >= creation_frame+15 ){
            posX += velX;
        }
        posY -= velY;
    }

    public boolean outOfBounds() {
        return (posY < 0 || posX < 0 || posX > juego.screenW);
    }
}
