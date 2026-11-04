package ru.vsu.chuprikov.tank.model;

public final class MediumTank extends Tank {
    public MediumTank(String id, Position position, Direction direction) {
        super(id, position, direction, 40, 3, 15, 2);
    }

    @Override public int shotDamage() {
        return 10;
    }
}