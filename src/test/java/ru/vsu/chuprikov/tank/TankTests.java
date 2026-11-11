package ru.vsu.chuprikov.tank;

import org.junit.jupiter.api.Test;
import ru.vsu.chuprikov.tank.event.*;
import ru.vsu.chuprikov.tank.exception.InvalidMoveException;
import ru.vsu.chuprikov.tank.model.*;
import ru.vsu.chuprikov.tank.service.BattleService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTests {

    private Battlefield emptyField() {
        return new Battlefield(10, 10);
    }

    private Tank light(String id, int x, int y, Direction d) {
        return new LightTank(id, new Position(x, y), d);
    }

    private Tank heavy(String id, int x, int y, Direction d) {
        return new HeavyTank(id, new Position(x, y), d);
    }

    @Test void position_move_returnsNew() {
        Position p = new Position(1, 2);
        assertEquals(new Position(4, 6), p.move(3, 4));
    }

    @Test void position_equals_byValue() {
        assertEquals(new Position(1, 2), new Position(1, 2));
        assertEquals(new Position(1, 2).hashCode(), new Position(1, 2).hashCode());
    }

    @Test void position_negativeAllowed() {
        assertEquals(new Position(-1, 0), new Position(0, 0).move(-1, 0));
    }

    @Test void direction_opposite() {
        assertEquals(Direction.DOWN, Direction.UP.opposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
    }

    @Test void direction_step() {
        assertEquals(new Position(1, 0),
                new Position(0, 0).move(Direction.RIGHT.dx(), Direction.RIGHT.dy()));
    }

    @Test void tank_initialState() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        assertEquals(20, t.health());
        assertEquals(Direction.RIGHT, t.direction());
        assertFalse(t.isDestroyed());
    }

    @Test void tank_move_forward() throws Exception {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        t.move(new Position(1, 0));
        assertEquals(new Position(1, 0), t.position());
    }

    @Test void tank_move_zero_throws() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        assertThrows(InvalidMoveException.class, () -> t.move(new Position(0, 0)));
    }

    @Test void tank_move_tooFast_throws() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        assertThrows(InvalidMoveException.class, () -> t.move(new Position(4, 0)));
    }

    @Test void tank_takeDamage_reducesHp() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        t.takeDamage(10);
        assertEquals(20 - 9, t.health());
    }

    @Test void tank_destroyed_atZeroHp() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        t.takeDamage(100);
        assertTrue(t.isDestroyed());
        assertEquals(0, t.health());
    }

    @Test void tank_move_destroyed_throws() {
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        t.takeDamage(100);
        assertThrows(InvalidMoveException.class, () -> t.move(new Position(1, 0)));
    }

    @Test void tank_equals_byId() {
        Tank a = light("T1", 0, 0, Direction.RIGHT);
        Tank b = light("T1", 5, 5, Direction.LEFT);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test void tank_polymorphism_heavyStrongerShot() {
        Tank l = light("L", 0, 0, Direction.RIGHT);
        Tank h = heavy("H", 0, 0, Direction.RIGHT);
        assertTrue(h.shotDamage() > l.shotDamage());
    }

    @Test void battlefield_inBounds() {
        Battlefield f = emptyField();
        assertTrue(f.inBounds(new Position(0, 0)));
        assertTrue(f.inBounds(new Position(9, 9)));
        assertFalse(f.inBounds(new Position(10, 0)));
    }

    @Test void battlefield_addTank_duplicateId_throws() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> f.addTank(light("T1", 5, 5, Direction.RIGHT)));
    }

    @Test void battlefield_addTank_occupiedCell_throws() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> f.addTank(light("T2", 0, 0, Direction.RIGHT)));
    }

    @Test void battlefield_lineOfFire_stopsAtWall() {
        Battlefield f = emptyField();
        f.addObstacle(new Obstacle("O1", new Position(3, 0), ObstacleType.WALL));
        List<Position> line = f.lineOfFire(new Position(0, 0), Direction.RIGHT);
        assertEquals(List.of(
                new Position(1, 0), new Position(2, 0), new Position(3, 0)), line);
    }

    @Test void battlefield_lineOfFire_stopsAtTank() {
        Battlefield f = emptyField();
        f.addTank(light("T2", 4, 0, Direction.LEFT));
        List<Position> line = f.lineOfFire(new Position(0, 0), Direction.RIGHT);
        assertEquals(List.of(
                new Position(1, 0), new Position(2, 0),
                new Position(3, 0), new Position(4, 0)), line);
    }

    @Test void battlefield_lineOfFire_leftEdge_doesNotThrow() {
        Battlefield f = emptyField();
        List<Position> line = f.lineOfFire(new Position(0, 0), Direction.LEFT);
        assertTrue(line.isEmpty());
    }

    @Test void service_moveTank_createsEvent() throws Exception {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        BattleService s = new BattleService(f, 42L);
        List<BattleEvent> ev = s.moveTank("T1", new Position(1, 0));
        assertEquals(1, ev.size());
        assertInstanceOf(TankMoved.class, ev.getFirst());
    }

    @Test void service_moveTank_unknownId_throws() {
        BattleService s = new BattleService(emptyField(), 42L);
        assertThrows(IllegalArgumentException.class,
                () -> s.moveTank("X", new Position(1, 0)));
    }

    @Test void service_rotateTank_changesDirection() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        BattleService s = new BattleService(f, 42L);

        List<BattleEvent> ev = s.rotateTank("T1", Direction.DOWN);

        assertEquals(1, ev.size());
        assertInstanceOf(TankRotated.class, ev.get(0));
        assertEquals(Direction.DOWN, f.tankById("T1").orElseThrow().direction());
    }

    @Test void service_rotateTank_destroyed_throws() {
        Battlefield f = emptyField();
        Tank t = light("T1", 0, 0, Direction.RIGHT);
        t.takeDamage(100);
        f.addTank(t);
        BattleService s = new BattleService(f, 42L);

        assertThrows(IllegalStateException.class, () -> s.rotateTank("T1", Direction.UP));
    }

    @Test void service_fire_hitsTank() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        Tank t2 = light("T2", 3, 0, Direction.LEFT);
        f.addTank(t2);
        BattleService s = new BattleService(f, 42L);
        int hpBefore = t2.health();
        s.fire("T1");
        assertTrue(t2.health() < hpBefore);
    }

    @Test void service_fire_noAmmo_throws() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        f.addTank(light("T2", 5, 5, Direction.LEFT));
        BattleService s = new BattleService(f, 42L);
        for (int i = 0; i < 10; i++) s.fire("T1");
        assertThrows(IllegalStateException.class, () -> s.fire("T1"));
    }

    @Test void service_gameOver_oneTankAlive() {
        Battlefield f = emptyField();
        f.addTank(light("T1", 0, 0, Direction.RIGHT));
        BattleService s = new BattleService(f, 42L);
        assertTrue(s.isGameOver());
        assertEquals("T1", s.winnerId().orElseThrow());
    }

    @Test void service_sameSeed_sameEventsCount() {
        Battlefield f1 = emptyField();
        f1.addTank(light("T1", 0, 0, Direction.RIGHT));
        f1.addTank(light("T2", 5, 0, Direction.LEFT));

        Battlefield f2 = emptyField();
        f2.addTank(light("T1", 0, 0, Direction.RIGHT));
        f2.addTank(light("T2", 5, 0, Direction.LEFT));

        BattleService s1 = new BattleService(f1, 42L);
        BattleService s2 = new BattleService(f2, 42L);

        for (int i = 0; i < 5; i++) {
            s1.fire("T1");
            s2.fire("T1");
        }
        assertEquals(s1.events().size(), s2.events().size());
    }
}