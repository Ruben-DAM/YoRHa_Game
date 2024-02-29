package com.example.yorha;

import android.graphics.Canvas;

public class PowerUp extends Bullet{

    public PowerUp(Juego juego, int posX, int posY, double vel, double angle, int color, int radius, int frame) {
        super(juego, posX, posY, vel, angle, color, radius, frame);
    }
    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(super.juego.powerup,super.posX,super.posY,null);
    }
}
