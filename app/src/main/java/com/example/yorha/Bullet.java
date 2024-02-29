package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {
    public int posX, posY;
    public Juego juego;
    protected double vel;
    protected double angle;
    private int color;
    private int radius;
    private int creation_frame;

    public Bullet(Juego juego,int posX, int posY, double vel, double angle,int color, int radius, int frame) {
        this.posX = posX;
        this.posY = posY;
        this.juego = juego;
        this.creation_frame = frame;
        this.vel = vel;
        this.angle = angle;
        this.color = color;
        this.radius = radius;

    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        canvas.drawCircle(posX,posY,radius+3,p);
        p.setColor(color);
        canvas.drawCircle(posX,posY,radius,p);
    }

    public void move(int actual_frame){
        if(creation_frame+30 <= actual_frame) {
            posX += vel * Math.cos(angle);
            posY += vel * Math.sin(angle);
        } else {
            posY -= vel;
        }
    }

    public boolean outOfBounds() {
        return (posY < 0 || posY > juego.screenH-400-(radius*2) || posX < 0 || posX > juego.screenW);
    }

    public int getRadius() {
        return radius;
    }

    public double getVel() {
        return vel;
    }
}
