package com.game.core.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.game.core.component.CharacterComponent;
import com.game.core.component.ScriptComponent;
import com.game.core.system.PhysicsSystem;
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
public class AshleyManager{
	
//	public static AshleyManager instance = new AshleyManager();
	
	/**
	 * ashley组件实体系统引擎
	 */
	public PooledEngine engine;
	
	/**
	 * 实体生产
	 */
	// TODO 不用多个 一个就行
	public EntityDao entityDao;
	
	private boolean isCopy = false;
	
	public AshleyManager() {
		engine = new PooledEngine();
		engine.addEntityListener(new AshleyManagerEntityListener());
		entityDao = new EntityDao();
	}
	
	/**
	 * 添加removeForCopy方法移出的Entity
	 * @param entity
	 */
	public void addCopy(Entity entity){
		isCopy = true;
		engine.addEntity(entity);
		
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null){ // 添加物理
			
			// 角色刚体
			if(MapperTools.characterCM.get(entity) != null){
				physicsSystem.physicsManager.addCharacterRigidBody(entity);
			}
			
			// 战斗组件刚体
			if(MapperTools.combatCM.get(entity) != null)
				physicsSystem.physicsManager.addCombatRigidBody(entity);
			
			// 碰撞检测
			if(MapperTools.collisionCM.get(entity) != null)
				physicsSystem.physicsManager.addCollisionRigidBody(entity);
		}
		
		isCopy = false;
	}
	
	/**
	 * 移出ECS, 但是不销毁。只有new的Entity有效
	 * @param entity
	 */
	public void removeForCopy(Entity entity){
		isCopy = true;
		engine.removeEntity(entity);
		
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null){ // 移除物理
			
			// 角色刚体
			if(MapperTools.characterCM.get(entity) != null){
				physicsSystem.physicsManager.removeCharacterRigidBody(entity);
			}
			
			// 战斗组件刚体
			if(MapperTools.combatCM.get(entity) != null)
				physicsSystem.physicsManager.removeCombatRigidBody(entity);
			
			// 碰撞检测
			if(MapperTools.collisionCM.get(entity) != null)
				physicsSystem.physicsManager.removeCollisionRigidBody(entity);
		}
		
		isCopy = false;
	}
	
	/**
	 * 销毁
	 */
	public void disabled(){
		
		// 必须回收Entity，在销毁System
		engine.removeAllEntities();
		engine.clearPools();
		
		// 销毁物理引擎
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null)
			physicsSystem.physicsManager.dispose();
	}
	
	/**
	 * 组件监听
	 * 
	 * @author D
	 * @date 2016年11月14日
	 */
	private class AshleyManagerEntityListener implements EntityListener{
		
		@Override
		public void entityAdded(Entity entity) {
			
			if(isCopy)
				return;
			
			PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
			
			// 添加角色刚体
			CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
			if(characterComponent != null)
				physicsSystem.physicsManager.addCharacterRigidBody(entity);
			
			// 战斗组件刚体
			if(MapperTools.combatCM.get(entity) != null)
				physicsSystem.physicsManager.addCombatRigidBody(entity);
			
			// 添加碰撞检测
			if(MapperTools.collisionCM.get(entity) != null)
				physicsSystem.physicsManager.addCollisionRigidBody(entity);
			
			// 状态组件设置状态机的参数(owner)为实体
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			if(stateComponent != null){
				if(stateComponent.entityState instanceof DefaultStateMachine)
					((DefaultStateMachine<Entity, States>)stateComponent.entityState).setOwner(entity);
			}
			
			// 消息组件添加监听
//			MessageComponent messageComponent = MapperTools.messageCM.get(entity);
//			if(messageComponent != null){
//				messageComponent.entity = entity;
//				if(messageComponent.message != null)
//					MsgManager.instance.addListeners(messageComponent, messageComponent.message);
//			}
			
			// 脚本组件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
			if(scriptComponent != null){
				if(scriptComponent.message != null)
					MsgManager.instance.addListeners(scriptComponent, scriptComponent.message); // 消息
				
				if(scriptComponent.script instanceof InputProcessor)
					InputManager.instance.addProcessor((InputProcessor)scriptComponent.script); // 输入事件
			}
		}

		@Override
		public void entityRemoved(Entity entity) {
			
			if(isCopy)
				return;
			
			// 脚本组件，移出输入监听
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
			if(scriptComponent != null){
				if(scriptComponent.message != null)
					MsgManager.instance.removeListener(scriptComponent, scriptComponent.message);
				if(scriptComponent.script instanceof InputProcessor)
					InputManager.instance.removeProcessor((InputProcessor)scriptComponent.script);
			}
			
			// 消息组件，移出消息监听
//			MessageComponent messageComponent = MapperTools.messageCM.get(entity);
//			if(messageComponent != null && messageComponent.message != null)
//				MsgManager.instance.removeListener(messageComponent, messageComponent.message);
		}
	}
	
}
