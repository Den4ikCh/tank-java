package ru.vsu.chuprikov.tank.exception;

/**
 * Недопустимое перемещение танка: выход за границы, препятствие,
 * нулевой сдвиг, попытка хода уничтоженным танком.
 * Проверяемое исключение: клиент обязан обработать недопустимый ход.
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}