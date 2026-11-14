package ru.vsu.chuprikov.tank.model;

public final class MediumTank extends Tank {
    /**
     * Создает средний танк с базовыми характеристиками:
     * 40 HP, 3 броня, 15 патронов, скорость 2.
     * Урон выстрела: 10.
     */
    public MediumTank(String id, Position position, Direction direction) {
        super(id, position, direction, 40, 3, 15, 2);
    }

    @Override public int shotDamage() {
        return 10;
    }
}