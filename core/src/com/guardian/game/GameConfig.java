package com.guardian.game;

public class GameConfig {

	/**
	 * 游戏设计分辨率
	 */
	public final static int width = 540;
	public final static int hieght = 960;
	
	/**
	 * UI边距，不同手机屏幕比例不同，没有边距会显示不全
	 */
	public final static byte UIpad = 35;
	
	/**
	 * 地图图块大小
	 */
	public final static byte tileSize = 32; 
	
	/**
	 * 角色占用大小，4个图块
	 */
	public final static byte characterTileSize = 64; 
	
	
	/**
	 * 游戏速度
	 */
	public final static float gameSpeed = 2;
	
	/**
	 * UI的debug模式
	 */
	public final static boolean UIdebug = false;
	
	/**
	 * 物理引擎的debug模式
	 */
	public final static boolean physicsdebug = false;
	
	/**
	 * fps
	 */
	public final static boolean fpsDebug = false;
}
