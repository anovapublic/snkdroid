/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid;

import java.util.List;

import es.anovagroup.moviles.modulo2.snkdroid.engine.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;


public class SnakeView extends View implements Delegate {
    private final Snake engine;
    private final transient RefreshHandler refreshHandler = 
            new RefreshHandler();
    private List<Point> snake;

    private int topX;
    private int topY;
    private int blockSize;

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            SnakeView.this.invalidate();
            if (dead) {
                postDelayed(new Runnable() {
                    public void run() {
                        engine.start();
                        dead = false;
                        SnakeView.this.update();
                    }

                }, 500);
            } else {
                engine.tick();
                SnakeView.this.update();
            }
        }

        public void sleep(final long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    private Point food;
    private boolean dead;

    public SnakeView(final Context context) {
        super(context);
        setFocusable(true);

        this.dead = false;
        this.engine = new Snake(new Snake.Options(this));
        this.engine.start();
        this.engine.tick();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        topX = (getWidth() - 256) / 2;
        topY = topX;
        blockSize = (getWidth() - topX - topY) / 16;

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        drawBgDroid(canvas, paint);
        drawSnakeDroid(canvas, paint);
        drawFoodDroid(canvas, paint);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent msg) {
        boolean ret = true;

        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            engine.move(Direction.UP);
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            engine.move(Direction.DOWN);
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            engine.move(Direction.LEFT);
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            engine.move(Direction.RIGHT);
            break;
        default:
            ret = super.onKeyDown(keyCode, msg);
            break;
        }

        return ret;
    }

    private void drawBgDroid(final Canvas canvas, final Paint paint) {
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(topX, topY, topX + blockSize * 16, topY + blockSize
                * 16, paint);
    }

    private void drawSnakeDroid(final Canvas canvas, final Paint paint) {
        if (snake == null) {
            return;
        }

        paint.setColor(dead ? Color.RED : Color.BLACK);

        for (Point p : snake) {
            canvas.drawRect(topX + p.getX() * blockSize, topY + p.getY()
                    * blockSize, topX + (p.getX() + 1) * blockSize,
                    topY + (p.getY() + 1) * blockSize, paint);
        }
    }

    private void drawFoodDroid(final Canvas canvas, final Paint paint) {
        if (food == null) {
            return;
        }

        paint.setColor(dead ? Color.RED : Color.BLACK);
        canvas.drawCircle(topX + food.getX() * blockSize + blockSize / 2, topY
                + food.getY() * blockSize + blockSize / 2, blockSize / 2, paint);
    }

    public void update() {
        refreshHandler.sleep(350 - snake.size() * 5);
    }

    public void drawSnake(final List<Point> snake) {
    }

    public void drawFood(final Point food) {
        this.food = food;
    }

    public void gameOver(final List<Point> snake) {
        this.snake = snake;
        dead = true;
        refreshHandler.sleep(0);
    }

    public void eatFood(final Point food) {
    }

    public void nextTick(final List<Point> snake) {
        this.snake = snake;
        update();
    }
}