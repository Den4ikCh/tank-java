package ru.vsu.chuprikov.tank.event;

import ru.vsu.chuprikov.tank.model.Direction;

public record TankRotated(int turn, String tankId, Direction direction)
        implements BattleEvent {}