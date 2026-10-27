package ru.vsu.chuprikov.tank.model;

import java.util.Objects;

/**
 * Препятствие на поле: стена, укрытие или вода.<br>
 * Тип определяет, блокирует ли препятствие движение и линию огня.
 */
public class Obstacle extends Entity {

    private final ObstacleType type;

    public Obstacle(String id, Position position, ObstacleType type) {
        super(id, position);
        this.type = Objects.requireNonNull(type, "type");
    }

    public ObstacleType type() {
        return type;
    }
}
