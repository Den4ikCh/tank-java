package ru.vsu.chuprikov.tank.model;

public final class LightTank extends Tank {
    public LightTank(String id, Position position, Direction direction) {
        super(id, position, direction, 20, 1, 10, 3);
    }

    @Override
    public int shotDamage() {
        return 5;
    }
}