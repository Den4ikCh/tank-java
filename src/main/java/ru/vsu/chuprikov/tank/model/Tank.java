package ru.vsu.chuprikov.tank.model;

public class Tank extends Entity implements Movable {
    private int health;
    private int armor;
    private int ammunition;
    private int speed;

    public Tank(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(Position delta) {
        setPosition(delta);
    }
}
