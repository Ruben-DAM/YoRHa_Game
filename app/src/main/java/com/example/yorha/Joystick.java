package com.example.yorha;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Joystick {
    private int outCirclePosX;
    private int outCirclePosY;
    private int inCirclePosX;
    private int inCirclePosY;
    private int centerPosX;
    private int centerPosY;
    private int outCircleRadius;
    private int inCircleRadius;
    private final Paint outPaint;
    private final Paint inPaint;
    public boolean pulsado;
    Juego juego;
    private double distance;
    private double distanceX;
    private double distanceY;
    public double moveX;
    public double moveY;

    public Joystick(Juego j){
        juego = j;
        centerPosY = juego.screenH - 200;
        centerPosX = juego.screenW - 200;
        outCirclePosX = centerPosX;
        outCirclePosY = centerPosY;
        inCirclePosX = centerPosX;
        inCirclePosY = centerPosY;

        outCircleRadius = 150;
        inCircleRadius = 50;

        outPaint = new Paint();
        outPaint.setColor(Color.GRAY);
        outPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        inPaint = new Paint();
        inPaint.setColor(Color.parseColor("#52677a"));
        inPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        inPaint.setStrokeWidth(25);
    }
    public void draw(Canvas canvas) {
        canvas.drawCircle(outCirclePosX,outCirclePosY,outCircleRadius,outPaint);
        canvas.drawCircle(inCirclePosX,inCirclePosY,inCircleRadius,inPaint);
        canvas.drawLine(inCirclePosX,inCirclePosY,outCirclePosX,outCirclePosY,inPaint);
    }

    public void move() {
        inCirclePosX = (int) (outCirclePosX + moveX*outCircleRadius);
        inCirclePosY = (int) (outCirclePosY + moveY*outCircleRadius);
        Log.d("Joystick","Posicion joystick: "+inCirclePosX+","+inCirclePosY);
    }

    public boolean estaPulsado(int x, int y) {
        distance = calculateDistance(x,y);
        //Solo devuelve true si se pulsa dentro del radio
        return distance < outCircleRadius;
    }


    public void actualizar(int x, int y) {
        distance = calculateDistance(x,y);
        Log.d("Joystick","Distancia "+distance);
        if(distance < outCircleRadius){
            moveX = distanceX/outCircleRadius;
            moveY = distanceY/outCircleRadius;
        } else {
            moveX = distanceX/distance;
            moveY = distanceY/distance;
        }
    }

    /**
     * Calcula la distancia entre un punto y el centro del joystick usando el teorema de Pitagoras
     * @param x int Posicion x del punto
     * @param y int Posicion y del punto
     * @return double Distancia entre el punto y el centro del joystick
     */
    public double calculateDistance(int x, int y){
        distanceX = x - outCirclePosX;
        distanceY = y - outCirclePosY;
        return Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
    }

    public void reset() {
        moveX = 0;
        moveY = 0;
    }
}
