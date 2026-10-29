package ru.vsu.chuprikov.tank.model;

import ru.vsu.chuprikov.tank.exception.InvalidMoveException;

import java.util.Objects;

public class Tank extends Entity implements Movable {

    private final int maxHealth;
    private int health;
    private final int armor;
    private int ammunition;
    private final int speed;
    private Direction direction;

    public Tank(String id, Position position, Direction direction, int maxHealth, int armor, int ammunition, int speed) {
        super(id, position);
        this.direction = Objects.requireNonNull(direction, "direction");
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("maxHealth должен быть > 0");
        }
        if (armor < 0) {
            throw new IllegalArgumentException("armor не может быть отрицательной");
        }
        if (ammunition < 0) {
            throw new IllegalArgumentException("ammunition не может быть отрицательной");
        }
        if (speed < 0) {
            throw new IllegalArgumentException("speed не может быть отрицательной");
        }
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.armor = armor;
        this.ammunition = ammunition;
        this.speed = speed;
    }

    public int maxHealth() {
        return maxHealth;
    }

    public int health() {
        return health;
    }

    public int armor() {
        return armor;
    }

    public int ammunition() {
        return ammunition;
    }

    public int speed() {
        return speed;
    }

    public Direction direction() {
        return direction;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    /** Поворот в новое направление. */
    public void rotate(Direction newDirection) {
        this.direction = Objects.requireNonNull(newDirection, "direction");
    }

    /** Уменьшает боезапас на один выстрел. */
    public void spendAmmo() {
        if (ammunition <= 0) {
            throw new IllegalStateException("Нет боезапаса");
        }
        ammunition--;
    }

    /**
     * Применяет урон с учётом брони. Минимальный урон — 1.
     * HP не опускается ниже нуля.
     */
    public void takeDamage(int rawDamage) {
        if (rawDamage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным");
        }
        int actual = Math.max(1, rawDamage - armor);
        this.health = Math.max(0, this.health - actual);
    }

    @Override
    public void move(Position delta) throws InvalidMoveException {
        if (isDestroyed()) {
            throw new InvalidMoveException("Танк " + id() + " уничтожен и не может двигаться");
        }
        if (delta.x() == 0 && delta.y() == 0) {
            throw new InvalidMoveException("Сдвиг не может быть нулевым");
        }
        setPosition(position().move(delta.x(), delta.y()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tank t)) return false;
        return id().equals(t.id());
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    @Override
    public String toString() {
        return "Tank{" + id() + ", pos=" + position() + ", hp=" + health + "/" + maxHealth + "}";
    }
}
