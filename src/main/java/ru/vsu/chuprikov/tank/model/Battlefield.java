package ru.vsu.chuprikov.tank.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Battlefield {
    private final int width;
    private final int height;
    private final Map<Position, Obstacle> obstacles;
    private final Map<String, Tank> tanks;
    private final List<Shell> shells;

    public Battlefield(int width, int height, Map<Position, Obstacle> obstacles, Map<String, Tank> tanks, List<Shell> shells) {
        if (width <= 0) {
            throw new IllegalArgumentException("width должен быть больше 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height должен быть больше 0");
        }
        this.width = width;
        this.height = height;
        this.obstacles = Objects.requireNonNull(obstacles, "obstacles");
        this.tanks = Objects.requireNonNull(tanks, "tanks");
        this.shells = Objects.requireNonNull(shells, "shells");
    }

    public boolean inBounds(Position p) {
        boolean inBounds = false;
        if (0 <= p.x() && p.x() < width && 0 <= p.y() && p.y() < height) {
            inBounds = true;
        }
        return inBounds;
    }

    public boolean isFree(Position p) {
        boolean free = !obstacles.containsKey(p);
        boolean inBounds = inBounds(p);
        return free && inBounds;
    }

    public Optional<Tank> tankAt(Position p) {
        return Optional.ofNullable(tanks.get(p));
    }

    public Optional<Obstacle> obstacleAt(Position p) {
        return Optional.ofNullable(obstacles.get(p));
    }

    public Optional<Tank> tankById(String id) {
        return Optional.ofNullable(tanks.get(id));
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.put(obstacle.position(), obstacle);
    }

    public void addTank(Tank tank) {
        tanks.put(tank.id(), tank);
    }

    public void removeTank(String id) {
        tanks.remove(id);
    }

    public void addShell(Shell shell) {
        shells.add(shell);
    }

    public void removeShell(Shell shell) {
        shells.remove(shell);
    }

    public List<Tank> aliveTanks() {
        return tanks.values().stream().
                filter(x -> !x.isDestroyed()).
                toList();
    }

    public List<Shell> shells() {
        return List.copyOf(shells);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
