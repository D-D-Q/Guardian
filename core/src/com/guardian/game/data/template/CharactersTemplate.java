package com.guardian.game.data.template;

/**
 * 角色数据模板
 * 
 * @author D
 * @date 2016年9月19日 下午9:40:31
 */
public class CharactersTemplate {
	
	/**
	 * 文件名
	 */
	public String fileName;
	
	/**
	 * 空闲动画帧数
	 */
	public int idleFrames;
	
	/**
	 * 移动动画帧数
	 */
	public int runFrames;
	
	/**
	 * 攻击动画帧数
	 */
	public int attackFrames;
	
	/**
	 * 触发攻击事件的攻击帧
	 */
	public int attackFrameIndex;
	
	/**
	 * 角色站立点x,y偏移
	 * 精灵定位锚点，绘制显示时，会把该点显示在系统所指的位置上
	 */
	public float offsetX = 128;
	public float offsetY = 128;
	
	/**
	 * 刚体大小。表示物体
	 */
	public float physicsRadius;
	
	/**
	 * 精灵刚体大小。表示角色
	 */
	public float characterRadius;
	
	/**
	 * 精灵碰撞大小
	 */
	public float collisionRadius;
	
	/**
	 * 名称
	 */
	public String name;
	
	/**
	 * 等级
	 */
	public int Lv = 1;
	
	/**
	 * 攻击
	 */
	public int ATK;
	
	/**
	 * 防御
	 */
	public int DEF;
	
	/**
	 * 命中
	 */
	public int HIT;
	
	/**
	 * 闪避
	 */
	public int AVD;
	
	/**
	 * 体力
	 */
	public int VIT;
	
	/**
	 * 需要接受的消息
	 */
	public int[] message;
	
	/**
	 * 脚本类名。包名.类名
	 */
	public String script;
}
