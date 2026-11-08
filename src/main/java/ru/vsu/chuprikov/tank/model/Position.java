package ru.vsu.chuprikov.tank.model;

/**
 * Координата на сетке поля.
 * Неизменяемое значение: {@code equals/hashCode} генерируются автоматически.
 *
 * <p>Границы поля проверяет {@link Battlefield#inBounds(Position)}, а не сама позиция:
 * отрицательные координаты допустимы как промежуточные значения (например,
 * при вычислении линии огня).
 *
 * @param x координата по горизонтали
 * @param y координата по вертикали
 */
public record Position(int x, int y) {

    /**
     * Возвращает новую позицию, сдвинутую на {@code (dx, dy)}.
     * Исходная позиция не меняется.
     */
    public Position move(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}