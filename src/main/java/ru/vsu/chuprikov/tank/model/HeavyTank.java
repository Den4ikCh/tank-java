package ru.vsu.chuprikov.tank.model;

public final class HeavyTank extends Tank {
    /**
     * Создает легкий танк с базовыми характеристиками:
     * 50 HP, 5 броня, 5 патронов, скорость 1.
     * Урон выстрела: 20.
     */
    public HeavyTank(String id, Position position, Direction direction) {
        super(id, position, direction, 50, 5, 5, 1);
    }

    @Override
    public int shotDamage() {
        return 20;
    }
}