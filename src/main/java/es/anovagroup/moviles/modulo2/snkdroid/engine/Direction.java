/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

public enum Direction {
	RIGHT(1, 0),
	LEFT(-1, 0),
	DOWN(0, 1),
	UP(0, -1);

	final Point direction;

	Direction(final int x, final int y) {
		this.direction = new Point(x, y);
	}
	
	Point point() {
		return direction;
	}
}