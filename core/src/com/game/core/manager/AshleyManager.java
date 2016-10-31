package com.game.core.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.MessageComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.ScriptComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.entity.dao.EntityDao;
import com.guardian.game.tools.MapperTools;

/**
 * 实体引擎
 * 
 * @author D
 * @date 2016年10月16日 下午8:22:07
 */
public class AshleyManager implements EntityListener{
	
	public static AshleyManager instance = new AshleyManager();
	
	/**
	 * ashley组件实体系统引擎
	 */
	public PooledEngine engine;
	
	/**
	 * 实体生产
	 */
	public EntityDao entityDao;
	
	public AshleyManager() {
		engine = new PooledEngine();
		engine.addEntityListener(this);
		entityDao = new EntityDao();
	}
	
	@Override
	public void entityAdded(Entity entity) {
		
		// 添加角色刚体
		CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
		if(characterComponent != null){
			characterComponent.entity = entity;
			PhysicsManager.instance.addCharacterRigidBody(entity);
		}
		
		// 战斗组件刚体
		if(MapperTools.combatCM.get(entity) != null)
			PhysicsManager.instance.addCombatRigidBody(entity);
		
		// 添加碰撞检测
		if(MapperTools.collisionCM.get(entity) != null)
			PhysicsManager.instance.addCollisionRigidBody(entity);
		
		// 状态组件设置状态机的参数(owner)为实体
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(stateComponent != null){
			stateComponent.entity = entity;
			if(stateComponent.entityState instanceof DefaultStateMachine)
				((DefaultStateMachine<Entity, States>)stateComponent.entityState).setOwner(entity);
		}
		
		// 消息组件添加监听
		MessageComponent messageComponent = MapperTools.messageCM.get(entity);
		if(messageComponent != null){
			messageComponent.entity = entity;
			if(messageComponent.message != null)
				MsgManager.instance.messageManager.addListeners(messageComponent, messageComponent.message);
		}
		
		// 脚本组件
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null){
			scriptComponent.script.entity = entity;
			if(scriptComponent.script instanceof InputProcessor)
				InputManager.instance.addProcessor((InputProcessor)scriptComponent.script);
		}
		
		// 动画组件
		AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
		if(animationComponent != null)
			animationComponent.entity = entity;
		
		// 寻路组件
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		if(pathfindingComponent != null)
			pathfindingComponent.entity = entity;
	}

	@Override
	public void entityRemoved(Entity entity) {
		
		// 脚本组件，移出输入监听
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null && scriptComponent.script instanceof InputProcessor)
			InputManager.instance.removeProcessor((InputProcessor)scriptComponent.script);
		
		// 消息组件，移出消息监听
		MessageComponent messageComponent = MapperTools.messageCM.get(entity);
		if(messageComponent != null && messageComponent.message != null)
			MsgManager.instance.messageManager.removeListener(messageComponent, messageComponent.message);
	}
}
