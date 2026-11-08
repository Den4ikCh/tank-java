# Танки (вариант 24)

Пошаговая механика танкового боя на сетке.

## Модель
- `Position` — координата (record)
- `Direction` — 4 направления с dx/dy
- `Entity` → `Tank` (LightTank, HeavyTank), `Obstacle`
- `ObstacleType` — WALL / COVER / WATER
- `Battlefield` — сетка, проверка границ, линия огня
- `BattleEvent` (sealed) — 6 событий боя
- `BattleService` — правила: движение, поворот, выстрел, конец игры

## Архитектура
- **Механика не печатает** — `BattleService` возвращает `List<BattleEvent>`.
- **Вывод отдельно** — `ConsoleRenderer`.
- **Воспроизводимость** — `Random` с зерном в конструкторе `BattleService`.

## Запуск
`mvn compile exec:java`

## Тесты
`mvn test`

## Управление
`Demo` — полный бой: движение, выстрел, урон, уничтожение, GameOver.