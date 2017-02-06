package com.work.xinlai.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;

/**
 * 封装异步任务队列-volley不需要
 **/

public abstract class BaseAsynTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    //cpu数量
    private static final int CPU = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_BASE_SIZE = CPU;
    private static final int THREAD_MAX_SIZE = 128;
    private static final int KEEP_ALIVE = 3;
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);


        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "QuickAsyncTask #" + mCount.getAndIncrement());
        }
    };
    //阻塞队列
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<Runnable>(128);

    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE2 = new LinkedBlockingQueue<Runnable>(128);

    public static final Executor SINGLE_EXECUTOR = new ThreadPoolExecutor(THREAD_BASE_SIZE, THREAD_MAX_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            POOL_WORK_QUEUE2, THREAD_FACTORY);

    public static final Executor SINGLE_EXECUTOR_LONG = new ThreadPoolExecutor(4, 128, 10, TimeUnit.SECONDS,
            POOL_WORK_QUEUE, THREAD_FACTORY, new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * Should only do quick tasks here. Starting from API 11, the tasks are
     * executed in a single thread by default, and it should be enough for quick
     * tasks. Network related tasks should be done with executeLong.
     */
    public AsyncTask<Params, Progress, Result> executeQuick(Params... params) {
        return executeOnExecutor(SINGLE_EXECUTOR, params);
    }

    /**
     * Tasks might take a long time should be done here (like network tasks).
     */
    public AsyncTask<Params, Progress, Result> executeLong(Params... params) {
        return executeOnExecutor(SINGLE_EXECUTOR_LONG, params);
    }
}
