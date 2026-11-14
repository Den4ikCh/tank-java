package ru.vsu.chuprikov.tank.model;

import java.util.*;

public class Battlefield {
    private final int width;
    private final int height;
    private final Map<Position, Obstacle> obstacles;
    private final Map<String, Tank> tanks;

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
    }

    public boolean inBounds(Position p) {
        return 0 <= p.x() && p.x() < width && 0 <= p.y() && p.y() < height;
    }

    /**
     * Свободна ли клетка для танка: в границах, нет танка,
     * нет препятствия с {@code blocksMovement}.
     */
    public boolean isFree(Position p) {
        if (!inBounds(p)) return false;
        if (tankAt(p).isPresent()) return false;
        return obstacleAt(p)
                .map(o -> !o.type().blocksMovement())
                .orElse(true);
    }

    /**
     * Клетки от {@code from} в направлении {@code dir} до первого препятствия,
     * блокирующего линию огня, или до первого танка (включительно).
     */
    public List<Position> lineOfFire(Position from, Direction dir) {
        List<Position> result = new ArrayList<>();
        Position current = from;
        while (true) {
            current = current.move(dir.dx(), dir.dy());
            if (!inBounds(current)) {
                break;
            }
            result.add(current);
            if (obstacleAt(current).map(o -> o.type().blocksLineOfFire()).orElse(false)) {
                break;
            }
            if (tankAt(current).isPresent()) {
                break;
            }
        }
        return result;
    }

    /**
     * Находит танк на указанной клетке.
     *
     * @param p координата на поле
     * @return {@code Optional} с танком, если клетка занята, иначе пустой {@code Optional}
     */
    public Optional<Tank> tankAt(Position p) {
        return tanks.values().stream()
                .filter(t -> t.position().equals(p))
                .findFirst();
    }

    /**
     * Возвращает препятствие в указанной клетке.
     *
     * @param p координата на поле
     * @return {@code Optional} с препятствием, если оно там есть, иначе пустой {@code Optional}
     */
    public Optional<Obstacle> obstacleAt(Position p) {
        return Optional.ofNullable(obstacles.get(p));
    }

    /**
     * Находит танк по его уникальному идентификатору.
     *
     * @param id идентификатор танка
     * @return {@code Optional} с танком, если он существует, иначе пустой {@code Optional}
     */
    public Optional<Tank> tankById(String id) {
        return Optional.ofNullable(tanks.get(id));
    }

    /**
     * Добавляет препятствие. Клетка должна быть в границах и свободна.
     * @throws IllegalArgumentException если позиция вне поля или занята
     */
    public void addObstacle(Obstacle obstacle) {
        Objects.requireNonNull(obstacle, "obstacle");
        checkCell(obstacle.position());
        obstacles.put(obstacle.position(), obstacle);
    }

    /**
     * Добавляет танк. {@code id} должен быть уникален, клетка свободна.
     * @throws IllegalArgumentException если {@code id} занят, позиция вне поля или занята
     */
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

    /**
     * Возвращает список всех живых (не уничтоженных) танков на поле.
     * Возвращаемый список является неизменяемой копией.
     *
     * @return список живых танков
     */
    public List<Tank> aliveTanks() {
        return tanks.values().stream().
                filter(x -> !x.isDestroyed()).
                toList();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
