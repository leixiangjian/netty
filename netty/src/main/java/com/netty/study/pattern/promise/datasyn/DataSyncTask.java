package com.netty.study.pattern.promise.datasyn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSyncTask implements Runnable {
	private static final Logger logger = Logger.getLogger(DataSyncTask.class.getName());
	private final Map<String,String> taskParams;
	
	public DataSyncTask(Map<String,String> taskParams){
		this.taskParams = taskParams;
	}

	@Override
	public void run() {
		String ftpServer =null;
		String userName =null;
		String password =null;

		//先初始化FTP客户端实例
		Future ftpClientUtilPromise = FtpClientUtil.newInstance(ftpServer, userName, password);
		
		//获取初始化完毕的客户端实例
		try {
			if(ftpClientUtilPromise.isDone()){
				FtpClientUtil ftpClientUtil = (FtpClientUtil) ftpClientUtilPromise.get();
				logger.info(""+ftpClientUtil);
			}
			
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "error", e);
		} catch (ExecutionException e) {
			logger.log(Level.WARNING, "error", e);
		}
	}
	
	public static void main(String[] args) {
		Map<String,String> taskParams = new HashMap<String,String>();
		Thread DataSyncTask = new Thread(new DataSyncTask(taskParams));
		DataSyncTask.start();
	}

}
