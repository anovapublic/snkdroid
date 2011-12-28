/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.anovagroup.moviles.modulo2.snkdroid.engine.Snake;
import es.anovagroup.moviles.modulo2.snkdroid.engine.Point;
import es.anovagroup.moviles.modulo2.snkdroid.engine.Direction;
import es.anovagroup.moviles.modulo2.snkdroid.engine.Snake.Options;

public class SnakeTest {
    static final class SnakeDead extends RuntimeException {
    }

    static final class SnakeHungry extends RuntimeException {
    }

    static final class SnakeBack extends RuntimeException {
    }

    static final class SnakeStarve extends RuntimeException {
    }

    static final class SnakeFull extends RuntimeException {
    }

    /** Regla 1: la serpiente muere al chocar con el muro */
    @Test
    public void testWall() {
        final Snake engine = new Snake(new Snake.Options(new DelegateAdapter() {
            public void gameOver(final List<Point> snake) {
                throw new SnakeDead();
            }
        }).setRows(16).setColumns(16));

        engine.start();
        try {
            for (int i = 1; i < 8; i++) {
                engine.tick();
            }
        } catch (SnakeDead ex) {
            fail("Murió antes de llegar al borde");
        }

        try {
            engine.tick();
            fail("Llegó al borde");
        } catch (SnakeDead ex) {
        }
    }

    /** Regla 2: la serpiente crece cada vez que come */
    @Test
    public void testEat() {
        final Snake engine = new AheadFood(new Snake.Options(
                new DelegateAdapter() {
                    int size = 3;

                    public void nextTick(final List<Point> snake) {
                        if (snake.size() != ++size) {
                            throw new SnakeHungry();
                        }
                    }
                }).setRows(16).setColumns(16).setDirection(Direction.RIGHT));

        engine.start();
        try {
            for (int i = 1; i < 8; i++) {
                engine.tick();
            }
        } catch (SnakeHungry ex) {
            fail("La serpiente no creció");
        }
    }

    /** Regla 3: la serpiente no puede volver sobre sus pasos */
    static class SnakeBackTest extends DelegateAdapter {
        private Point oldHead = null;
        Direction dir = Direction.RIGHT;

        public void nextTick(final List<Point> snake) {
            if (oldHead != null) {
                final Point head = snake.get(0);
                if (oldHead.x != head.x - dir.point().x
                        || oldHead.y != head.y - dir.point().y) {
                    throw new SnakeBack();
                }
            }
            oldHead = snake.get(0);
        }
    }

    @Test
    public void testBackward() {
        final SnakeBackTest adapter = new SnakeBackTest();
        final Snake engine = new FixFood(new Snake.Options(adapter).setRows(32)
                .setColumns(32).setDirection(Direction.RIGHT));

        // Lo comprobamos en las cuatro direcciones del avance
        engine.start();
        try {
            engine.tick();

            // intenta avanzar a izquierdas
            engine.move(Direction.LEFT);
            engine.tick();

            // encara hacia arriba
            engine.move(adapter.dir = Direction.UP);

            // intenta avanzar hacia abajo
            engine.move(Direction.DOWN);
            engine.tick();

            // encara a izquierdas
            engine.move(adapter.dir = Direction.LEFT);

            // intenta avanzar a derechas
            engine.move(Direction.RIGHT);
            engine.tick();

            // encara hacia abajo
            engine.move(adapter.dir = Direction.DOWN);

            // intenta avanzar hacia arriba
            engine.move(Direction.UP);
            engine.tick();
        } catch (SnakeBack ex) {
            fail("Retrocedió");
        }
    }

    /** Regla 4: la serpiente muere si se come a sí misma */
    @Test
    public void testHarakiri() {
        final Snake engine = new FixFood(new Snake.Options(
                new DelegateAdapter() {
                    public void gameOver(final List<Point> snake) {
                        throw new SnakeDead();
                    }
                }).setRows(32).setColumns(32).setSize(4)
                .setDirection(Direction.RIGHT));

        // Haremos un ciclo
        engine.start();
        try {
            engine.tick();
            engine.move(Direction.UP);
            engine.tick();
            engine.move(Direction.LEFT);
            engine.tick();
        } catch (SnakeDead ex) {
            fail("Murió antes de morderse");
        }

        try {
            engine.move(Direction.DOWN);
            engine.tick();
            fail("No se mordió");
        } catch (SnakeDead ex) {
        }
    }

    /** Regla 5: sólo cuando come aparece un nueva comida */
    static class CycleFood extends AheadFood {
        boolean clean;

        CycleFood(final Options options) {
            super(options);
            clean = true;
        }

        @Override
        protected Point createFood() {
            if (!clean) {
                throw new SnakeFull();
            }

            clean = false;
            return super.createFood();
        }
    }

    @Test
    public void testFoodCycle() {
        final CycleFood engine = new CycleFood(new Snake.Options(
                new DelegateAdapter()).setRows(16).setColumns(16)
                .setDirection(Direction.RIGHT).setSize(3));

        engine.getOptions().setDelegate(new DelegateAdapter() {
            public void eatFood(final Point food) {
                engine.clean = true;
            }
        });

        engine.start();
        try {
            for (int i = 1; i < 8; i++) {
                engine.tick();
                if (engine.clean) {
                    throw new SnakeStarve();
                }
            }
        } catch (SnakeStarve ex) {
            fail("La comida no se repuso");
        } catch (SnakeFull ex) {
            fail("Hay comida de más");
        }
    }
}
