package eim.yar.sbertask.data.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import eim.yar.sbertask.domain.executor.ThreadExecutor;

/**
 * Decorated {@link java.util.concurrent.ThreadPoolExecutor} Singleton class based on
 * 'Initialization on Demand Holder' pattern.
 */
public class JobExecutor implements ThreadExecutor {

    /**
     * Number of threads to keep in the pool.
     */
    private static final int INITIAL_POOL_SIZE = 3;

    /**
     * Maximum number of threads to allow in the pool.
     */
    private static final int MAX_POOL_SIZE = 5;

    /**
     * Amount of time an idle thread waits before terminating
     */
    private static final int KEEP_ALIVE_TIME = 10;

    /**
     * Pool containing working threads.
     */
    private final ThreadPoolExecutor threadPoolExecutor;

    /**
     * Lazy holder class for 'Initialization on Demand Holder' pattern.
     */
    private static class LazyHolder {
        /**
         * Instance of {@link JobExecutor}.
         */
        private static final JobExecutor INSTANCE = new JobExecutor();
    }

    /**
     * Get singleton instance of {@link JobExecutor}.
     * @return instance of {@link JobExecutor}
     */
    public static JobExecutor getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Construct a {@link JobExecutor}.
     */
    JobExecutor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        this.threadPoolExecutor.execute(runnable);
    }
}