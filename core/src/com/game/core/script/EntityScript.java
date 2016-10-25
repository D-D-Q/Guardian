package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.tools.MessageType;

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
	public boolean message(int messageType, Entity sender, Object extraInfo){
		
		if(messageType == MessageType.MSG_ATTACK){
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			AttributesComponent senderAttributesComponent = MapperTools.attributesCM.get(sender);
			
			// TODO 伤害公式和技能计算
			float damage = senderAttributesComponent.ATK * (senderAttributesComponent.ATK / (senderAttributesComponent.ATK + attributesComponent.DEF));
			attributesComponent.VIT -= Math.max(1, damage); // 最低伤害 1
		}
		if(messageType == MessageType.MSG_DEATH){
			
			// TODO 加经验升级
			Log.info(this, "干掉一个");
		}
		
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
	public abstract void update(float deltaTime);
}
