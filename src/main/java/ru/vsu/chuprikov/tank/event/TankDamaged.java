package ru.vsu.chuprikov.tank.event;

public record TankDamaged(int turn, String tankId, int amount, int remainingHp)
        implements BattleEvent {}