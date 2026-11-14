package ru.vsu.chuprikov.tank.model;

public final class LightTank extends Tank {
    /**
     * Создает легкий танк с базовыми характеристиками:
     * 20 HP, 1 броня, 10 патронов, скорость 3.
     * Урон выстрела: 5.
     */
    public LightTank(String id, Position position, Direction direction) {
        super(id, position, direction, 20, 1, 10, 3);
    }

    @Override
    public int shotDamage() {
        return 5;
    }
}