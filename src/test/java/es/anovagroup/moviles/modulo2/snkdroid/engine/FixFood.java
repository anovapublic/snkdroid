/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid.engine;

import es.anovagroup.moviles.modulo2.snkdroid.engine.Snake.Options;

class FixFood extends Snake {
    FixFood(final Options params) {
        super(params);
    }

    @Override
    protected Point createFood() {
        return new Point(1, 1);
    }
}
