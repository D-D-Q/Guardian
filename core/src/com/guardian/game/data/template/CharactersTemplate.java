package com.guardian.game.data.template;

/**
 * 角色数据模板
 * 
 * @author D
 * @date 2016年9月19日 下午9:40:31
 */
public class CharactersTemplate{
	
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
	 * 触发攻击事件的攻击帧, 从0算
	 */
	public int attackFrameIndex;
	
	/**
	 * 角色站立点x,y偏移
	 * 精灵定位锚点，绘制显示时，会把该点显示在系统所指的位置上
	 */
	public float offsetX = 128;
	public float offsetY = 128;
	
	/**
	 * 精灵身高
	 */
	public float spriteHeight;
	
	/**
	 * 精灵身宽
	 */
	public float spriteWidth;
	
	/**
	 * 物体刚体大小半径。
	 */
	public float physicsRadius;
	
	/**
	 * 角色刚体大小半径。如果不配置，默认取值spriteWidth/2
	 */
	public float characterRadius;
	
	/**
	 * 精灵攻击范围
	 * CombatComponent
	 */
	public float ATKRange = 300;

	/**
	 * 精灵攻击距离
	 * CombatComponent
	 */
	public float ATKDistance = 0;
	
	/**
	 * 碰撞检测范围
	 * CollisionComponent
	 */
	public float collisionRadius = 0;
	
	/**
	 * 阵营
	 * 取值 1, 2, 4, 8, ...。默认1
	 */
	public int campBits = 1;
	
	/**
	 * 可攻击的阵营的和, 默认所有
	 */
	public int campMaskBits = -1;
	
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
	public int ATK = 10;
	
	/**
	 * 攻击速度
	 * 默认 1次/秒，极限5次/秒
	 */
	public float ASPD = 1;
	
	/**
	 * 防御
	 */
	public int DEF = 10;
	
	/**
	 * 敏捷
	 */
	public int AGI = 10;
	
	/**
	 * 体力
	 */
	public int VIT = 10;
	
	/**
	 * 移动速度
	 */
	public float speed = 60;
	
	/**
	 * 需要接受的群发消息
	 */
	public int[] message;
	
	/**
	 * 脚本类名。包名.类名
	 */
	public String script = "com.game.core.script.EntityScript";
}
