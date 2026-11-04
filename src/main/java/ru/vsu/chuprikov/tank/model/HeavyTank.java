package ru.vsu.chuprikov.tank.model;

public final class HeavyTank extends Tank {
    public HeavyTank(String id, Position position, Direction direction) {
        super(id, position, direction, 50, 5, 5, 1);
    }

    @Override
    public int shotDamage() {
        return 20;
    }
}