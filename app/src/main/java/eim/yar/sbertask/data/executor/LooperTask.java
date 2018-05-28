package eim.yar.sbertask.data.executor;

import android.os.Looper;

/**
 * Base class for tasks requiring {@link Looper}.
 */
public class LooperTask {

    protected LooperTask() {
        //empty
    }

    /**
     * Prepare Looper.
     */
    protected void prepareLooper() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    /**
     * Quit Looper.
     */
    protected void quitLooper() {
        if (Looper.myLooper() != null && Looper.myLooper() != Looper.getMainLooper()) {
            Looper.myLooper().quit();
        }
    }
}
