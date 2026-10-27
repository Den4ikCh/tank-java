package ru.vsu.chuprikov.tank.model;

import ru.vsu.chuprikov.tank.exception.InvalidMoveException;

/**
 * Сущность, способная перемещаться по полю.
 */
public interface Movable {

    /**
     * Перемещает сущность на сдвиг {@code delta}.
     *
     * @param delta сдвиг по координатам
     * @throws InvalidMoveException если перемещение недопустимо
     *         (выход за границы, препятствие, уничтоженная техника)
     */
    void move(Position delta) throws InvalidMoveException;
}
