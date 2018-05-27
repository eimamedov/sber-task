package eim.yar.sbertask.presentation.executor;

import android.os.Handler;
import android.os.Looper;

import eim.yar.sbertask.domain.executor.PostExecutionThread;

/**
 * MainThread (UI Thread) implementation based on a Handler instantiated with the main
 * application Looper.
 */
public class UIThread implements PostExecutionThread {

    /**
     * Handler to add runnable to the message queue.
     */
    private final Handler handler;

    /**
     * Lazy holder class for 'Initialization on Demand Holder' pattern.
     */
    private static class LazyHolder {
        /**
         * Instance of {@link UIThread}.
         */
        private static final UIThread INSTANCE = new UIThread();
    }

    /**
     * Get singleton instance of {@link UIThread}.
     * @return instance of {@link UIThread}
     */
    public static UIThread getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Construct a {@link UIThread}.
     */
    UIThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Causes the Runnable to be added to the message queue.
     * The runnable will be run on the main thread.
     * @param runnable {@link Runnable} to be executed.
     */
    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
