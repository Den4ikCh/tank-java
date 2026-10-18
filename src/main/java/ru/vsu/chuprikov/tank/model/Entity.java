package ru.vsu.chuprikov.tank.model;

public abstract class Entity {
    protected double x;
    protected double y;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double[] getPosition() {
        return new double[] {x, y};
    }
}
