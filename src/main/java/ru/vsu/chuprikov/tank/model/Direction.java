package ru.vsu.chuprikov.tank.model;

/**
 * Направление на сетке. Каждая константа знает свой сдвиг {@code (dx, dy)}.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() { return dx; }
    public int dy() { return dy; }

    /** Возвращает сдвиг-позицию на один шаг в этом направлении. */
    public Position step() {
        return new Position(dx, dy);
    }

    /** Противоположное направление. */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}