package ru.vsu.chuprikov.tank.model;

import java.util.*;

public class Battlefield {
    private final int width;
    private final int height;
    private final Map<Position, Obstacle> obstacles;
    private final Map<String, Tank> tanks;
    private final List<Shell> shells;

    public Battlefield(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width должна быть больше 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height должна быть больше 0");
        }
        this.width = width;
        this.height = height;
        this.obstacles = new HashMap<>();
        this.tanks = new LinkedHashMap<>(); //порядок добавления сохраняется -> порядок хода тоже сохраняется
        this.shells = new ArrayList<>();
    }

    public boolean inBounds(Position p) {
        return 0 <= p.x() && p.x() < width && 0 <= p.y() && p.y() < height;
    }

    public boolean isFree(Position p) {
        if (!inBounds(p)) return false;
        if (tankAt(p).isPresent()) return false;
        return obstacleAt(p)
                .map(o -> !o.type().blocksMovement())
                .orElse(true);
    }

    public Optional<Tank> tankAt(Position p) {
        return tanks.values().stream()
                .filter(t -> t.position().equals(p))
                .findFirst();
    }

    public Optional<Obstacle> obstacleAt(Position p) {
        return Optional.ofNullable(obstacles.get(p));
    }

    public Optional<Tank> tankById(String id) {
        return Optional.ofNullable(tanks.get(id));
    }

    public void addObstacle(Obstacle obstacle) {
        Objects.requireNonNull(obstacle, "obstacle");
        checkCell(obstacle.position());
        obstacles.put(obstacle.position(), obstacle);
    }

    public void addTank(Tank tank) {
        Objects.requireNonNull(tank, "tank");
        if (tanks.containsKey(tank.id())) {
            throw new IllegalArgumentException("Танк с id " + tank.id() + " уже добавлен");
        }
        checkCell(tank.position());
        tanks.put(tank.id(), tank);
    }

    private void checkCell(Position position) {
        if (!inBounds(position)) {
            throw new IllegalArgumentException("Позиция вне поля: " + position);
        }
        if (tankAt(position).isPresent()) {
            throw new IllegalArgumentException("Клетка " + position + " уже занята танком");
        }
        if (obstacles.containsKey(position)) {
            throw new IllegalArgumentException("Клетка " + position + " занята препятствием");
        }
    }

    public void removeTank(String id) {
        tanks.remove(id);
    }

    public void addShell(Shell shell) {
        Objects.requireNonNull(shell, "shell");
        if (shells.stream().anyMatch(s -> s.id().equals(shell.id()))) {
            throw new IllegalArgumentException("Снаряд с id " + shell.id() + " уже добавлен");
        }
        shells.add(shell);
    }

    public void removeShellById(String id) {
        shells.removeIf(s -> s.id().equals(id));
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
