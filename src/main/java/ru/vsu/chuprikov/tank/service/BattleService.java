package ru.vsu.chuprikov.tank.service;

import ru.vsu.chuprikov.tank.event.*;
import ru.vsu.chuprikov.tank.exception.InvalidMoveException;
import ru.vsu.chuprikov.tank.model.Battlefield;
import ru.vsu.chuprikov.tank.model.Position;
import ru.vsu.chuprikov.tank.model.Tank;

import java.util.*;

public class BattleService {
    private final Battlefield battlefield;
    private final Random random;
    private final List<BattleEvent> events;
    private int turn;

    public BattleService(Battlefield battlefield, long seed) {
        this.battlefield = Objects.requireNonNull(battlefield, "battlefield");
        this.random = new Random(seed);
        this.events = new ArrayList<>();
        this.turn = 0;
    }

    /** Возвращает копию журнала событий (наружу не утекает). */
    public List<BattleEvent> events() {
        return List.copyOf(events);
    }

    public int turn() {
        return turn;
    }

    /**
     * Перемещает танк на сдвиг {@code delta}.
     *
     * <p>Скорость ограничена манхэттен-расстоянием: {@code |dx| + |dy| ≤ speed}.
     * Проверки уничтожения, нулевого сдвига и скорости — в {@link Tank#move}.
     *
     * @param tankId id танка
     * @param delta  сдвиг по координатам
     * @return события, порождённые ходом (только новые)
     * @throws IllegalArgumentException если танк с таким id не найден
     * @throws InvalidMoveException     если ход недопустим (занята клетка, уничтожен, скорость)
     */
    public List<BattleEvent> moveTank(String tankId, Position delta) throws InvalidMoveException {
        Tank tank = battlefield.tankById(tankId)
                .orElseThrow(() -> new IllegalArgumentException("Танк с id " + tankId + " не найден"));

        Position from = tank.position();
        Position target = from.move(delta.x(), delta.y());
        if (!battlefield.isFree(target)) {
            throw new InvalidMoveException("Клетка " + target + " занята или вне поля");
        }

        tank.move(delta);

        int before = events.size();
        turn++;
        events.add(new TankMoved(turn, tankId, from, tank.position()));
        return List.copyOf(events.subList(before, events.size()));
    }

    /**
     * Выстрел танка в текущем направлении. Мгновенный: снаряд летит
     * до первого препятствия или танка.
     *
     * @param tankId id стреляющего танка
     * @return события выстрела (только новые)
     * @throws IllegalArgumentException если танк не найден
     * @throws IllegalStateException    если танк уничтожен или нет боезапаса
     */
    public List<BattleEvent> fire(String tankId) {
        Tank shooter = battlefield.tankById(tankId)
                .orElseThrow(() -> new IllegalArgumentException("Танк с id " + tankId + " не найден"));
        if (shooter.isDestroyed()) {
            throw new IllegalStateException("Танк " + tankId + " уничтожен");
        }

        int before = events.size();
        turn++;

        shooter.spendAmmo();
        String shellId = "S" + turn;
        events.add(new ShellFired(turn, tankId, shellId));

        List<Position> line = battlefield.lineOfFire(shooter.position(), shooter.direction());
        if (!line.isEmpty()) {
            Position last = line.get(line.size() - 1);
            battlefield.tankAt(last).ifPresent(target -> {
                int hpBefore = target.health();
                target.takeDamage(shooter.shotDamage());
                int dealt = hpBefore - target.health();
                events.add(new TankDamaged(turn, target.id(), dealt, target.health()));
                if (target.isDestroyed()) {
                    events.add(new TankDestroyed(turn, target.id()));
                }
            });
        }

        if (isGameOver()) {
            events.add(new GameOver(turn, winnerId().orElse("")));
        }
        return List.copyOf(events.subList(before, events.size()));
    }

    /** Игра окончена, если живых танков ≤ 1. */
    public boolean isGameOver() {
        return battlefield.aliveTanks().size() <= 1;
    }

    /** id победителя, если игра окончена. */
    public Optional<String> winnerId() {
        List<Tank> alive = battlefield.aliveTanks();
        return alive.size() == 1 ? Optional.of(alive.get(0).id()) : Optional.empty();
    }
}
