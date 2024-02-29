package com.example.yorha;

public class EnemyBullet extends Bullet {
    public EnemyBullet(Juego juego, int posX, int posY, double vel, double angle, int color, int radius, int frame) {
        super(juego, posX, posY, vel, angle, color, radius, frame);
    }

    @Override
    public void move(int frame){
        posX += vel * Math.sin(angle);
        posY += vel * Math.cos(angle);
    }
}
