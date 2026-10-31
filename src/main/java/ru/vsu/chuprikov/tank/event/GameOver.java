package ru.vsu.chuprikov.tank.event;

public record GameOver(int turn, String winnerId)
        implements BattleEvent {}