/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class Snake {
    public static class Options {
        protected Delegate delegate;
        protected int rows;
        protected int columns;
        protected int size;
        protected Direction ahead;

        public Options(final Delegate delegate) {
            this.rows = 16;
            this.columns = 16;
            this.size = 3;
            this.ahead = Direction.RIGHT;
            this.delegate = delegate;
        }

        public Options setRows(final int rows) {
            this.rows = rows;
            return this;
        }

        public Options setColumns(final int columns) {
            this.columns = columns;
            return this;
        }

        public Options setSize(final int size) {
            this.size = size;
            return this;
        }

        public Options setDirection(final Direction ahead) {
            this.ahead = ahead;
            return this;
        }

        public Options setDelegate(final Delegate delegate) {
            this.delegate = delegate;
            return this;
        }
    }

    private LinkedList<Point> snake;
    private Point direction;
    private Point food;

    private final Options params;
    private final Random rndGenerator;

    public Snake(final Options params) {
        this.params = params;
        this.rndGenerator = new Random(new Date().getTime());
        this.snake = new LinkedList<Point>();
    }

    public Snake() {
        this(new Options(new DelegateAdapter()));
    }

    public final void start() {
        final int initX = (int) Math.floor(params.columns / 2.0);
        final int initY = (int) Math.floor(params.rows / 2.0);

        snake.clear();
        for (int i = 0; i < params.size; i++) {
            snake.add(new Point(initX - i, initY));
        }

        direction = params.ahead.point();
        food = createFood();

        params.delegate.drawFood(food);
        params.delegate.drawSnake(snake);
    }

    private Point rndCoord() {
        return new Point(rndGenerator.nextInt(params.rows),
                rndGenerator.nextInt(params.columns));
    }

    private boolean isInSnake(final Point pos) {
        boolean ret = false;
        for (Point s : snake) {
            if (s.equals(pos)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    private boolean isOutLimits(final Point pos) {
        return pos.x < 0 || pos.x > params.columns - 1 || pos.y < 0
                || pos.y > params.rows - 1;
    }

    private Point nextMove() {
        final Point head = snake.get(0);
        return new Point(head.x + direction.x, head.y + direction.y);
    }

    public void tick() {
        if (snake.isEmpty()) {
            return;
        }

        final Point newPos = nextMove();

        if (isOutLimits(newPos) || isInSnake(newPos)) {
            params.delegate.gameOver(Collections.unmodifiableList(snake));
        } else {
            snake.addFirst(newPos);

            if (food.equals(snake.get(0))) {
                params.delegate.eatFood(food);
                food = createFood();
            } else {
                snake.removeLast();
            }

            params.delegate.nextTick(Collections.unmodifiableList(snake));
        }

        params.delegate.drawSnake(Collections.unmodifiableList(snake));
        params.delegate.drawFood(food);
    }

    public void move(final Point pos) {
        if (direction != null && pos.x != -direction.x 
                && pos.y != -direction.y) {
            direction = pos;
        }
    }

    public void move(final Direction dir) {
        move(dir.point());
    }

    protected Point createFood() {
        Point foodPos;
        do {
            foodPos = rndCoord();
        } while (isInSnake(foodPos));

        return foodPos;
    }

    public Options getOptions() {
        return params;
    }
}
