package ru.vsu.chuprikov.tank.demo;

import ru.vsu.chuprikov.tank.event.BattleEvent;
import ru.vsu.chuprikov.tank.exception.InvalidMoveException;
import ru.vsu.chuprikov.tank.model.*;
import ru.vsu.chuprikov.tank.service.BattleService;
import ru.vsu.chuprikov.tank.view.ConsoleRenderer;

import java.util.List;

public class Demo {
    public static void main(String[] args) throws InvalidMoveException {
        Battlefield field = new Battlefield(8, 8);
        field.addObstacle(new Obstacle("O1", new Position(5, 5), ObstacleType.WATER));
        field.addObstacle(new Obstacle("O2", new Position(2, 2), ObstacleType.WALL));

        Tank t1 = new HeavyTank("T1", new Position(0, 0), Direction.RIGHT);
        Tank t2 = new LightTank("T2", new Position(7, 0), Direction.LEFT);
        field.addTank(t1);
        field.addTank(t2);

        BattleService battle = new BattleService(field, 42L);
        ConsoleRenderer renderer = new ConsoleRenderer();

        System.out.println("=== Старт ===");
        System.out.print(renderer.renderField(field));


        System.out.println("--- Ход T1 ---");
        System.out.print(renderer.renderEvents(battle.moveTank("T1", new Position(1, 0))));
        System.out.print(renderer.renderEvents(battle.moveTank("T1", new Position(1, 0))));

        System.out.println("--- Поворот T1 ---");
        System.out.print(renderer.renderEvents(battle.rotateTankTurret("T1", Direction.DOWN)));

        System.out.print(renderer.renderEvents(battle.rotateTankTurret("T1", Direction.RIGHT)));
        System.out.println("--- Бой ---");
        while (!battle.isGameOver()) {
            try {
                List<BattleEvent> ev = battle.fire("T1");
                System.out.print(renderer.renderEvents(ev));
            } catch (IllegalStateException e) {
                System.out.println("Нет патронов: " + e.getMessage());
                break;
            }
        }

        System.out.println("=== После ===");
        System.out.print(renderer.renderField(field));
        System.out.println("Игра окончена? " + battle.isGameOver());
        System.out.println("Победитель: " + battle.winnerId().orElse("нет"));

        try {
            battle.moveTank("T1", new Position(100, 0));
        } catch (InvalidMoveException e) {
            System.out.println("Поймано: " + e.getMessage());
        }
    }
}