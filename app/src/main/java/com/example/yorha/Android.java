package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Android {
    private double maxVelX;
    private double maxVelY;
    private int posX;
    private int posY;
    public int centerX;
    public int centerY;
    public int radius = 20;
    private double velX;
    private double velY;
    private Juego juego;
    private Paint p;
    public int life = 3;
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
        if(posY < 400)
            posY = 400;
        if(posY > juego.screenH - 400 - juego.character.getHeight())
            posY = juego.screenH - 400 - juego.character.getHeight();
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(juego.character,posX,posY,paint);
        p = new Paint();
        p.setColor(Color.YELLOW);
        canvas.drawCircle(centerX,centerY,radius,p);
    }
    public Android(Juego j){
        juego = j;
        posX = juego.screenW/2 - juego.character.getWidth()/2;
        posY = juego.screenH - 400 - juego.character.getHeight();
        centerX = posX + juego.character.getWidth()/2;
        centerY = posY + juego.character.getHeight()/2;
        maxVelX = juego.screenW/2/BucleJuego.MAX_FPS;
        maxVelY = juego.screenH/4/BucleJuego.MAX_FPS;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public boolean damage() {
        life--;
        return life <= 0;
    }
}
