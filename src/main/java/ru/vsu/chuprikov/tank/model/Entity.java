package ru.vsu.chuprikov.tank.model;

import java.util.Objects;

/**
 * Базовая сущность на поле: имеет уникальный идентификатор и позицию.<br>
 * Поля закрыты.<br>
 * Наследники меняют позицию через {@link #setPosition(Position)}.
 */
public abstract class Entity {
    private final String id;
    private Position position;

    public Entity(String id, Position position) {
        this.id = Objects.requireNonNull(id, "id");
        this.position = Objects.requireNonNull(position, "position");
    }

    public String id() {
        return id;
    }

    public Position position() {
        return position;
    }

    protected void setPosition(Position p) {
        this.position = Objects.requireNonNull(p, "position");
    }
}