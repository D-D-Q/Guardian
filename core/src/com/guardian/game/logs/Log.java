package com.guardian.game.logs;

import com.badlogic.gdx.Gdx;

/**
 * 方便日志记录
 * 
 * @author D
 * @date 2016年8月28日 上午11:56:42
 */
public class Log {

	/**
	 * Application.LOG_DEBUG
	 * @param logLevel
	 */
	public static void setLogLevel(int logLevel){
		Gdx.app.setLogLevel(logLevel);
	}
	
	public static void info(String tag, String message){
		Gdx.app.log(tag, message);
	}
	
	public static void debug(String tag, String message){
		Gdx.app.debug(tag, message);
	}
	
	public static void error(String tag, String message){
		Gdx.app.error(tag, message);
	}
	
	public static void error(Object object, String message){
		Gdx.app.error(object.getClass().getSimpleName(), message);
	}
	
	public static <T> void info(Object object, Object message){
		Gdx.app.log(object.getClass().getSimpleName(), message.toString());
	}
	
	public static void debug(Object object, Object message){
		Gdx.app.debug(object.getClass().getSimpleName(), message.toString());
	}
	
	public static void printStackTrace(){
		for(StackTraceElement s : Thread.currentThread().getStackTrace())
			Gdx.app.log(s.getClassName(), s.getMethodName());
	}
}
