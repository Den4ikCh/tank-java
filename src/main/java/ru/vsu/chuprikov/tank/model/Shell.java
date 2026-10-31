package ru.vsu.chuprikov.tank.model;

public class Shell {
    private String id;
    private String ownerId;
    private Position position;
    private Direction direction;
    private int damage;

    public Shell(String id, String ownerId, Position position, Direction direction, int damage) {
        this.id = id;
        this.ownerId = ownerId;
        this.position = position;
        this.direction = direction;
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage должен быть больше 0");
        }
        this.damage = damage;
    }

    public String id() {
        return id;
    }

    public String ownerId() {
        return ownerId;
    }

    public Position position() {
        return position;
    }

    public Direction direction() {
        return direction;
    }

    public int damage() {
        return damage;
    }
}
