package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.CombatComponent;
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
			
			// TODO 伤害公式未定
			attributesComponent.VIT -= senderAttributesComponent.ATK - attributesComponent.DEF;
		}
		
		return true;
	}
	
	
	/**
	 * CollisionComponent碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 */
	public void beginContact(Contact contact, Entity target){
	}
	
	
	/**
	 * CollisionComponent结束碰撞事件
	 * 
	 * @param contact 碰撞类
	 * @param target 碰撞目标
	 */
	public void endContact(Contact contact, Entity target){
	}
	
	/**
	 * 进入攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void enterATKRange(Contact contact, Entity target) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent != null){
			combatComponent.rangeTargets.add(target);
			
			if(combatComponent.target == null) // 设置为当前目标
				combatComponent.target = target;
		}
	}
	
	/**
	 * 离开攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void leaveATKRange(Contact contact, Entity target) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent != null){
			combatComponent.rangeTargets.removeValue(target, true);
			
			// 设置新目标
			if(combatComponent.target == target){
				combatComponent.target = combatComponent.distanceTargets.size == 0 ? 
						(combatComponent.rangeTargets.size == 0 ? null : combatComponent.rangeTargets.first()) : 
							combatComponent.distanceTargets.first(); 
			}
		}
	}
	
	/**
	 * 进入攻击距离
	 * 
	 * @param contact
	 * @param target
	 */
	public void enterATKDistance(Contact contact, Entity target) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent != null){
			combatComponent.distanceTargets.add(target);

			if(combatComponent.target == null || !combatComponent.IsdistanceTarget()){ // 更换攻击目标，谁直接能打到优先打谁，先不考虑追击
				combatComponent.target = target;
			}
		}
	}
	
	/**
	 * 离开攻击距离
	 * 
	 * @param contact
	 * @param target
	 */
	public void leaveATKDistance(Contact contact, Entity target) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent != null){
			combatComponent.distanceTargets.removeValue(target, true);
			
			// 设置新目标
			if(combatComponent.target == target){
				combatComponent.target = combatComponent.distanceTargets.size == 0 ? target : combatComponent.distanceTargets.first(); 
			}
		}
	}
	
	/**
	 * 每帧调用
	 * 
	 * @param deltaTime
	 */
	public abstract void update(float deltaTime);
}
