package ru.vsu.chuprikov.tank.model;

/**
 * Тип препятствия.
 */
public enum ObstacleType {
    /** Стена: блокирует движение и линию огня. */
    WALL(true, true),
    /** Укрытие: проходимо, но блокирует линию огня. */
    COVER(false, true),
    /** Вода: блокирует движение, не блокирует линию огня. */
    WATER(true, false);

    private final boolean blocksMovement;
    private final boolean blocksLineOfFire;

    ObstacleType(boolean blocksMovement, boolean blocksLineOfFire) {
        this.blocksMovement = blocksMovement;
        this.blocksLineOfFire = blocksLineOfFire;
    }

    public boolean blocksMovement() {
        return blocksMovement;
    }

    public boolean blocksLineOfFire() {
        return blocksLineOfFire;
    }
}