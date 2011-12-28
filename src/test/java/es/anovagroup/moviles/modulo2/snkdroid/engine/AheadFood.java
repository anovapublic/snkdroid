/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

class AheadFood extends Snake {
    protected int aheadX;
    final protected int aheadY;

    /** Posición del primer alimento, suponiendo 16 filas y columnas */
    protected AheadFood(final Options options) {
        super(options);
        this.aheadX = 9;
        this.aheadY = 8;
    }

    @Override
    protected Point createFood() {
        /* Siempre hacia la derecha */
        return new Point(aheadX++, aheadY);
    }
}
