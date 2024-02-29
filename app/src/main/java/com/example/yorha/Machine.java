package com.example.yorha;

import android.graphics.Canvas;

public class Machine {
    protected int f_to_shoot = 5;
    protected int cooldown;
    protected int n_bullets = 0;
    protected  int posX;
    protected  int posY;
    protected  Juego juego;
    public int centerX;
    public int centerY;
    protected int radius;
    public int life;
    public Machine(int posX, int posY, Juego juego) {
        this.posX = posX;
        this.posY = posY;
        this.juego = juego;
        this.centerX = posX + juego.machine.getWidth()/2;
        this.centerY = posY + juego.machine.getHeight()/2;
        this.radius = juego.machine.getHeight()/2;
        this.life = 100;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(juego.machine,posX,posY,null);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getRadius() {
        return radius;
    }

    public boolean damage() {
        life--;
        return life <= 0;
    }
}
