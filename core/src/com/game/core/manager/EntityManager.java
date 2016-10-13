package com.game.core.manager;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.game.core.component.ScriptComponent;
import com.guardian.game.components.CharacterComponent;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.MessageComponent;
import com.guardian.game.systems.MessageHandlingSystem;
import com.guardian.game.tools.MapperTools;

/**
 * 进行一些实体添加到实体引擎后的操作
 * 
 * @author D
 * @date 2016年10月13日 下午10:21:37
 */
public class EntityManager implements EntityListener{

	@Override
	public void entityAdded(Entity entity) {
		
		for(Component component : entity.getComponents()){
			
			// 添加角色刚体
			if(component instanceof CharacterComponent){
				PhysicsManager.addCharacterRigidBody(entity);
			}
			// 添加碰撞检测
			else if(component instanceof CollisionComponent){
				PhysicsManager.addCollision(entity);
			}
			// 消息组件添加监听
			else if(component instanceof MessageComponent){
				MessageComponent messageComponent = (MessageComponent)component;
				messageComponent.entity = entity;
				MsgManager.messageManager.addListener(messageComponent, MessageHandlingSystem.MSG_ATTACK);
			}
			// 脚本组件赋值实体
			else if(component instanceof ScriptComponent){
				ScriptComponent scriptComponent = (ScriptComponent)component;
				scriptComponent.entity = entity;
			}
		}
		
//		// 添加角色刚体
//		if(MapperTools.physicsCM.get(entity) != null)
//			PhysicsManager.addCharacterRigidBody(entity);
//		
//		// 添加碰撞检测
//		if(MapperTools.collisionCM.get(entity) != null)
//			PhysicsManager.addCollision(entity);
//		
//		// 消息组件添加监听
//		MessageComponent messageComponent = MapperTools.messageCM.get(entity);
//		if(messageComponent != null){
//			messageComponent.entity = entity;
//			MsgManager.messageManager.addListener(messageComponent, MessageHandlingSystem.MSG_ATTACK);
//		}
//		
//		// 脚本组件赋值实体
//		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
//		if(scriptComponent != null)
//			scriptComponent.entity = entity;
	}

	@Override
	public void entityRemoved(Entity entity) {
		
	}
}
