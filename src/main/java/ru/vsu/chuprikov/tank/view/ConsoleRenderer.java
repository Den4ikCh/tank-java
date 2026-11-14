package ru.vsu.chuprikov.tank.view;

import ru.vsu.chuprikov.tank.event.*;
import ru.vsu.chuprikov.tank.model.*;

import java.util.List;

/** Печатает поле и события. Только вывод, никакой логики. */
public class ConsoleRenderer {

    /** Карта поля битвы. T — танк, # — стена, ○ — вода, | — укрытие, · — пусто. */
    public String renderField(Battlefield field) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < field.height(); y++) {
            for (int x = 0; x < field.width(); x++) {
                Position p = new Position(x, y);
                if (field.tankAt(p).isPresent()) {
                    sb.append('T');
                } else {
                    sb.append(field.obstacleAt(p)
                            .map(o -> switch (o.type()) {
                                case WALL -> '#';
                                case WATER -> '○';
                                case COVER -> '|';
                            })
                            .orElse('·'));
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /** Одна строка на событие. */
    public String renderEvent(BattleEvent e) {
        return switch (e) {
            case TankMoved m -> "T" + m.turn() + ": " + m.tankId() + " " + m.from() + " -> " + m.to();
            case TankRotated r -> "T" + r.turn() + ": " + r.tankId() + " повернул башню на " + r.direction();
            case ShellFired f -> "T" + f.turn() + ": " + f.tankId() + " выстрелил (" + f.shellId() + ")";
            case TankDamaged d -> "T" + d.turn() + ": " + d.tankId() + " получил " + d.amount() + " урона, HP=" + d.remainingHp();
            case TankDestroyed d -> "T" + d.turn() + ": " + d.tankId() + " уничтожен";
            case GameOver g -> "T" + g.turn() + ": игра окончена. Победил: " + g.winnerId();
        };
    }

    public String renderEvents(List<BattleEvent> events) {
        StringBuilder sb = new StringBuilder();
        for (BattleEvent e : events) sb.append(renderEvent(e)).append('\n');
        return sb.toString();
    }
}