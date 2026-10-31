package ru.vsu.chuprikov.tank.event;

public sealed interface BattleEvent permits
        TankMoved, TankRotated, ShellFired, TankDamaged, TankDestroyed, GameOver {
    int turn();
}
