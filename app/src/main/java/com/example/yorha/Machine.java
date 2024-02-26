package com.example.yorha;

import android.graphics.Canvas;

public class Machine {
    private int posX;
    private int posY;
    private Juego juego;
    public int life = 10;
    public Machine(int posX, int posY, Juego juego) {
        this.posX = posX;
        this.posY = posY;
        this.juego = juego;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(juego.machine,posX,posY,null);
    }
}
