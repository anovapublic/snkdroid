/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

public final class Point {
    final int x;
    final int y;

    Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object aThat) {
        boolean ret;

        if (this == aThat) {
            ret = true;
        } else if (aThat instanceof Point) {
            final Point that = (Point) aThat;

            ret = this.x == that.x && this.y == that.y;
        } else {
            ret = false;
        }

        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 17;
        ret = 31 * ret + x;
        ret = 31 * ret + y;
        return ret;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}