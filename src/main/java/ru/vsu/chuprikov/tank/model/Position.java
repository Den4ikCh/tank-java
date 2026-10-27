package ru.vsu.chuprikov.tank.model;

/**
 * Координата на сетке поля.<br>
 * Неизменяемое значение: {@code equals/hashCode} генерируются автоматически.
 *
 * @param x координата по горизонтали (0 — левый край)
 * @param y координата по вертикали (0 — верхний край)
 */
public record Position(int x, int y) { //record вместо класса для инкапсуляции, чтобы не утекло состояние

    public Position(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(
                    "Координаты не могут быть отрицательными: (" + x + ", " + y + ")");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает новую позицию, сдвинутую на {@code (dx, dy)}.<br>
     * Исходная позиция не меняется.
     */
    public Position move(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}
