package ru.vsu.chuprikov.tank.model;

public class Shell {
    String id;
    String ownerId;
    Position position;
    Direction direction;
    int damage;

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
}
