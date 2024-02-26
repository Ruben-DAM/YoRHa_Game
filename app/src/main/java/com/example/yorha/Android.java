package com.example.yorha;

import android.graphics.Canvas;
import android.util.Log;

public class Android {
    private double maxVelX;
    private double maxVelY;
    private int posX;
    private int posY;
    private double velX;
    private double velY;
    private Juego juego;
    public void special(){    }
    public void move(Joystick j){
        velX = j.moveX * maxVelX;
        velY = j.moveY * maxVelY;
        posX += velX;
        posY += velY;
        if(posX < 1)
            posX=1;
        if(posX > juego.screenW - juego.character.getWidth())
            posX = juego.screenW - juego.character.getWidth();
        if(posY < 100)
            posY = 100;
        if(posY > juego.screenH - 400 - juego.character.getHeight())
            posY = juego.screenH - 400 - juego.character.getHeight();
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(juego.character,posX,posY,null);
    }
    public Android(Juego j){
        juego = j;
        posX = juego.screenW/2 - juego.character.getWidth()/2;
        posY = juego.screenH - 400 - juego.character.getHeight();
        maxVelX = juego.screenW/3/BucleJuego.MAX_FPS;
        maxVelY = juego.screenH/4.5/BucleJuego.MAX_FPS;
    }
}
