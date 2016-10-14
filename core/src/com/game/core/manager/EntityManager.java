package com.game.core.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.InputProcessor;
import com.game.core.component.ScriptComponent;
import com.guardian.game.components.MessageComponent;
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
		
		// 添加角色刚体
		if(MapperTools.characterCM.get(entity) != null)
			PhysicsManager.addCharacterRigidBody(entity);
		
		// 添加碰撞检测
		if(MapperTools.collisionCM.get(entity) != null)
			PhysicsManager.addCollision(entity);
		
		// 消息组件添加监听
		MessageComponent messageComponent = MapperTools.messageCM.get(entity);
		if(messageComponent != null){
			messageComponent.entity = entity;
			MsgManager.messageManager.addListeners(messageComponent, messageComponent.message);
		}
		
		// 脚本组件
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null){
			scriptComponent.script.entity = entity;
			if(scriptComponent.script instanceof InputProcessor)
				InputManager.addProcessor((InputProcessor)scriptComponent.script);
		}
	}

	@Override
	public void entityRemoved(Entity entity) {
		
		// 脚本组件，移出输入监听
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null && scriptComponent.script instanceof InputProcessor)
			InputManager.removeProcessor((InputProcessor)scriptComponent.script);
	}
}
