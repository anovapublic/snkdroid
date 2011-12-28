/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo2.snkdroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SnakeActivity extends Activity {

    private static final String TAG = "snkdroid";

    /**
     * Called when the activity is first created.
     * 
     * @param savedState
     *            If the activity is being re-initialized after previously being
     *            shut down then this Bundle contains the data it most recently
     *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
     *            is null.</b>
     */
    @Override
    public void onCreate(final Bundle savedState) {
        super.onCreate(savedState);
        Log.i(TAG, "onCreate");
        setContentView(new SnakeView(this));
    }

}
