package ru.vsu.chuprikov.tank.model;

public class Tank extends Entity implements Movable {

    public Tank(double x, double y) {
        super(x, y);
    }

    @Override
    public void move(double x, double y) {
        super.x += x;
        super.y += y;
    }
}
