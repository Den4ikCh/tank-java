package ru.vsu.chuprikov.tank.event;

public record ShellFired(int turn, String tankId, String shellId)
        implements BattleEvent {}