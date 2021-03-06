package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 实体脚本的基类
 * 
 * @author D
 * @date 2016年10月14日
 */
public class EntityScript  {

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
	public boolean message(int messageType, Entity sender, Object extraInfo){
		
		return true;
	}
	
	
	/**
	 * CollisionComponent碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 * @return true 继续执行默认组件的操作
	 */
	public boolean beginContact(Contact contact, Entity target){
		return true;
	}
	
	
	/**
	 * CollisionComponent结束碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 * @return true 继续执行默认组件的操作
	 */
	public boolean endContact(Contact contact, Entity target){
		return true;
	}
	
	/**
	 * 进入攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 * @return true 继续执行默认组件的操作
	 */
	public boolean enterATKRange(Contact contact, Entity target) {
		return true;
	}
	
	/**
	 * 离开攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 * @return true 继续执行默认组件的操作
	 */
	public boolean leaveATKRange(Contact contact, Entity target) {
		return true;
	}
	
	/**
	 * 进入攻击距离
	 * 
	 * @param contact
	 * @param target
	 * @return true 继续执行默认组件的操作
	 */
	public boolean enterATKDistance(Contact contact, Entity target) {
		return true;
	}
	
	/**
	 * 离开攻击距离
	 * 
	 * @param contact
	 * @param target
	 * @return true 继续执行默认组件的操作
	 */
	public boolean leaveATKDistance(Contact contact, Entity target) {
		return true;
	}
	
	/**
	 * 每帧调用
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime){}
	
}
