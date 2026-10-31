package ru.vsu.chuprikov.tank.event;

import ru.vsu.chuprikov.tank.model.Position;

public record TankMoved(int turn, String tankId, Position from, Position to)
        implements BattleEvent {}