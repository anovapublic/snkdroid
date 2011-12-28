/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

import java.util.List;

public interface Delegate {
    void drawSnake(final List<Point> snake);

    void drawFood(final Point food);

    void gameOver(final List<Point> snake);

    void eatFood(final Point food);

    void nextTick(final List<Point> snake);
}
