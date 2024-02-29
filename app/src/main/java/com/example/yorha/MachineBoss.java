package com.example.yorha;

import android.graphics.Canvas;

public class MachineBoss extends Machine {
    public MachineBoss(int x, int y, Juego juego) {
        super(x,y,juego);
        this.centerX = posX + juego.boss.getWidth()/2;
        this.centerY = posY + juego.boss.getHeight()/2;
        this.radius = juego.boss.getHeight()/2;
        this.life = 300;
    }

    @Override
    public void draw(Canvas c){
        c.drawBitmap(juego.boss,posX,posY,null);
    }
}
