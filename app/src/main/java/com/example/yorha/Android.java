package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Android {
    private double maxVelX;
    private double maxVelY;
    private int posX;
    private int posY;
    private int centerX;
    private int centerY;
    private double velX;
    private double velY;
    private Juego juego;
    private Paint p;
    public void special(){    }
    public void move(Joystick j){
        velX = j.moveX * maxVelX;
        velY = j.moveY * maxVelY;
        posX += velX;
        posY += velY;
        centerX = posX + juego.character.getWidth()/2;
        centerY = posY + juego.character.getHeight()/2;
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
        p = new Paint();
        p.setColor(Color.YELLOW);
        canvas.drawCircle(centerX,centerY,20,p);
    }
    public Android(Juego j){
        juego = j;
        posX = juego.screenW/2 - juego.character.getWidth()/2;
        posY = juego.screenH - 400 - juego.character.getHeight();
        centerX = posX + juego.character.getWidth()/2;
        centerY = posY + juego.character.getHeight()/2;
        maxVelX = juego.screenW/3/BucleJuego.MAX_FPS;
        maxVelY = juego.screenH/4.5/BucleJuego.MAX_FPS;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }
}
