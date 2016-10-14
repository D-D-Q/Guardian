package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 实体脚本的基类
 * 
 * @author D
 * @date 2016年10月14日
 */
public abstract class EntityScript  {

	/**
	 * 该脚本属于的实体
	 */
	public Entity entity;
	
	/**
	 * 消息事件
	 * 
	 * @param messageType 消息类型
	 * @param sender 发送者
	 * @param extraInfo 携带对象，可能是null
	 * @return true处理完成
	 */
	public abstract boolean message(int messageType, Entity sender, Object extraInfo);
	
	
	/**
	 * CollisionComponent碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 */
	public abstract void beginContact(Contact contact, Entity target);
	
	/**
	 * CollisionComponent结束碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 */
	public abstract void endContact(Contact contact, Entity target);
}
