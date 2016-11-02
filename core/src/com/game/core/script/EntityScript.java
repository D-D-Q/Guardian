package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.game.core.component.AnimationComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.tools.MessageType;

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
		
		if(messageType == MessageType.MSG_ATTACK){
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			AttributesComponent senderAttributesComponent = MapperTools.attributesCM.get(sender);
			
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			
			// 计算命中
			float hit = Math.max(0.2f, (senderAttributesComponent.AGI + 1)/(attributesComponent.AGI + 1) * 0.6f); // 最低命中0.2
			if(hit >= MathUtils.random()){
				
				// 伤害浮动
				float atk = senderAttributesComponent.ATK * MathUtils.random(0.9f, 1f);
				float def = attributesComponent.DEF * MathUtils.random(0.9f, 1f);
				
				float damage = Math.max(1, atk * ((atk + 1)/(atk + def + 1))); // 最低伤害 1
				
				attributesComponent.curVit -= damage; 
				
				animationComponent.addSubtitle(String.format("%.0f", damage));
			}
			else{
				// 未命中
				animationComponent.addSubtitle("miss");
			}
		}
		if(messageType == MessageType.MSG_DEATH){
			
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
	public void update(float deltaTime){}
	
}
