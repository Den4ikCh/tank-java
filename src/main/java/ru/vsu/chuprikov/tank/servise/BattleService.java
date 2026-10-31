package ru.vsu.chuprikov.tank.servise;

import ru.vsu.chuprikov.tank.model.Battlefield;
import ru.vsu.chuprikov.tank.model.Tank;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BattleService {
    private final Battlefield battlefield;
    private final Random random;
    private final List<BattleEvent> events;
    private int turn;

    public BattleService(Battlefield battlefield, long seed) {
        this.battlefield = Objects.requireNonNull(battlefield);
        this.random = new Random(seed);
        this.events = new ArrayList<>();
        this.turn = 0;
    }
}
