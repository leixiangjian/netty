package com.netty.study.pattern.promise.datasyn;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FtpClientUtil {
	private static final Logger logger = Logger.getLogger(FtpClientUtil.class.getName());

	private volatile static ThreadPoolExecutor threadPool;

	static {
		threadPool = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(10),new ThreadFactory() {
					
					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setDaemon(true);
						return thread;
					}
				},new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 模式角色：Promise.Promisor.compute 返回约定的凭据
	 * 
	 * @param ftpServer
	 * @param userName
	 * @param password
	 * @return
	 */
	public static Future<FtpClientUtil> newInstance(final String ftpServer, final String userName,
			final String password) {

		// 使用callable来获取返回的FtpClientUtil对象
		Callable<FtpClientUtil> callable = new Callable<FtpClientUtil>() {
			@Override
			public FtpClientUtil call() throws Exception {
				// 初始化FTP客户端
				FtpClientUtil ftpClientUtil = null;
				ftpClientUtil = new FtpClientUtil();
				boolean isRetry = true;
				int retryNum = 0;
				while (isRetry) {
					try {
						ftpClientUtil.init(ftpServer, userName, password);
						isRetry = false;
					} catch (Exception e) {
						logger.log(Level.WARNING, "================>需要重试" + retryNum, e);
						retryNum++;
					}
					if (retryNum == 300000000) {
						isRetry = false;
					}
				}
				return ftpClientUtil;
			}

		};
		// FutureTask是Future的实现类相当于模型角色中的Promise.promise
		final FutureTask<FtpClientUtil> task = new FutureTask<FtpClientUtil>(callable);

		// 模式角色：Promise。taskExecutor
//		new Thread(task).start();
		threadPool.execute(task);

		return task;
	}

	private void init(String ftpServer, String userName, String password) {
		logger.info("================>FtpClientUtil.init");
		throw new RuntimeException("retry");
	}

	public void upload(File file) {
		logger.info("================>FtpClientUtil.upload");
	}

	public void disConnect() {
		logger.info("================>FtpClientUtil.disConnect");
	}
}
