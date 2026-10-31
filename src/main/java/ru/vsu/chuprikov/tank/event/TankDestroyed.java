package ru.vsu.chuprikov.tank.event;

public record TankDestroyed(int turn, String tankId)
        implements BattleEvent {}