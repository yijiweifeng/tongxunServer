package com.lt.nettyServer.service;

import com.lt.nettyServer.bean.MessageHolder;
import com.lt.nettyServer.queue.TaskQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息转发分配任务
 *
 * @author sj.
 */
public class Service {
    private static Logger logger = LogManager.getLogger(Service.class);

    public static AtomicBoolean shutdown = new AtomicBoolean(false);

    // 任务队列
    private BlockingQueue<MessageHolder> taskQueue;
    // 阻塞式地从taskQueue取MessageHolder
    private ExecutorService takeExecutor;
    // 执行业务的线程池
    private ExecutorService taskExecutor;

    public void initAndStart() {
        init();
        start();
    }

    private void init() {
        takeExecutor = Executors.newSingleThreadExecutor();
        taskExecutor = Executors.newFixedThreadPool(10);
        taskQueue = TaskQueue.getQueue();
        logger.info("初始化服务完成");
    }

    private void start() {
        takeExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!shutdown.get()) {
                    try {
                        MessageHolder messageHolder = taskQueue.take();
                        logger.info("TaskQueue取出任务: taskQueue=" + taskQueue.size());
                        startTask(messageHolder);
                    } catch (InterruptedException e) {
                        logger.warn("receiveQueue take", e);
                    }
                }
            }

            private void startTask(MessageHolder messageHolder) {
                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("开始执行取出的任务 messageHolder=" + messageHolder);
                        Dispatcher.dispatch(messageHolder);
                    }
                });
            }
        });
        logger.info("启动队列任务服务完成");
    }
}
