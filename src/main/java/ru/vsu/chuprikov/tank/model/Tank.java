package ru.vsu.chuprikov.tank.model;

import ru.vsu.chuprikov.tank.exception.InvalidMoveException;

import java.util.Objects;

public class Tank extends Entity implements Movable {

    private final int maxHealth;
    private int health;
    private final int armor;
    private int ammunition;
    private final int speed;
    private Direction turretDirection; // Направление башни (куда стреляем)

    public Tank(String id, Position position, Direction direction, int maxHealth, int armor, int ammunition, int speed) {
        super(id, position);
        this.turretDirection = Objects.requireNonNull(direction, "turretDirection");
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

    public Direction turretDirection() {
        return turretDirection;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public int shotDamage() {
        return 10;
    }

    /** Поворот башни в новое направление. */
    public void rotateTurret(Direction newDirection) {
        this.turretDirection = Objects.requireNonNull(newDirection, "turretDirection");
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
        if ((Math.abs(delta.x()) + Math.abs(delta.y())) > speed) {
            throw new InvalidMoveException("Превышение максимальной скорости");
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
