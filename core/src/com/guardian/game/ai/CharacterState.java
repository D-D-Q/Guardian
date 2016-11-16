package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.game.core.GlobalInline;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.manager.MsgManager;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.tools.MessageType;

/**
 * 分层有限状态机(HFSM), 角色使用
 * 
 * @author D
 * @date 2016年11月16日
 */
public enum CharacterState implements State<Entity>{
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	idle(IdleState.idle, IdleState.global), 

	/**
	 * 攻击 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:44
	 */
	combat(CombatState.idle, CombatState.global){
		
		@Override
		public void exit(Entity entity) {
			super.exit(entity);
			
			// 退出战斗状态，清空被攻击记录
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			combatComponent.attackerDamage.clear(); // 清除被攻击记录
		}
	},
	
	/**
	 * 全局，每个状态之前都执行
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:58:59
	 */
	global(){
		@Override
		public void update(Entity entity) {
			if(entity.flags == 0) // 被回收的entity
				return;
		}
	};
	
	/**
	 * 子状态机
	 */
	public final DefaultStateMachine<Entity, StateAdapter> subState;
	
	/**
	 * 进入状态的初始子状态
	 */
	public final StateAdapter initialState;
	
	private CharacterState() {
		subState = null;
		initialState = null;
	}
	
	/**
	 * @param initialState 子状态的初始状态
	 * @param globalState 子状态的全局状态
	 */
	private CharacterState(StateAdapter initialState, StateAdapter globalState) {
		subState = new DefaultStateMachine<>(null, initialState, globalState);
		this.initialState = initialState;
	}
	
	@Override
	public void enter(Entity entity) {
		subState.setOwner(entity);
		subState.setInitialState(initialState);
		subState.getCurrentState().enter(entity);
	}
	
	@Override
	public void update(Entity entity) {
		subState.update();
	}
	
	@Override
	public void exit(Entity entity) {
		subState.getCurrentState().exit(entity);
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
	
	/**
	 * 空闲子状态
	 * @author D
	 * @date 2016年11月16日
	 */
	private enum IdleState implements StateAdapter {
		
		/**
		 * 空闲 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:59
		 */
		idle(){
			@Override
			public void update(Entity entity) {
				
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null && combatComponent.target != null){
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					stateComponent.entityState.changeState(CharacterState.combat);
				}
			}
		}, 
		
		/**
		 * 移动
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:52
		 */
		run(){
			@Override
			public void update(Entity entity) {
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				if(characterComponent != null){
					AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					
					// 移动精灵的动态刚体。方向乘以速度
					characterComponent.dynamicBody.setLinearVelocity(stateComponent.moveOrientationVector.nor().scl(attributesComponent.moveSpeed));
				}
			}
			
			@Override
			public void exit(Entity entity) {
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				if(characterComponent != null)
					characterComponent.dynamicBody.setLinearVelocity(Vector2.Zero);
			}
		},

		/**
		 * 全局，每个状态之前都执行
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:58:59
		 */
		global(){
			@Override
			public void update(Entity entity) {
				
			}
		};

		@Override
		public void enter(Entity entity) {
		}

		@Override
		public void update(Entity entity) {
		}

		@Override
		public void exit(Entity entity) {
		}

		@Override
		public boolean onMessage(Entity entity, Telegram telegram) {
			return false;
		}
	}
	
	/**
	 * 战斗子状态
	 * @author D
	 * @date 2016年11月16日
	 */
	private enum CombatState implements StateAdapter {
		
		/**
		 * 空闲 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:59
		 */
		idle(){
			
			private float deltaTime; // 空闲状态持续时间
			
			@Override
			public void update(Entity entity) {
				
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				
				if(deltaTime >= 3){ // 持续时间3秒不攻击， 退出战斗
					stateComponent.entityState.changeState(CharacterState.idle);
					return ;
				}
				deltaTime += Gdx.graphics.getDeltaTime();
				
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null){
					if(combatComponent.IsDistanceTarget()){ // 攻击目标
						stateComponent.entityState.getCurrentState().subState.changeState(CombatState.attack);
					}
					else if(combatComponent.isCampTarget()){ // 移动到目标
						stateComponent.entityState.getCurrentState().subState.changeState(CombatState.run);
					}
				}
			}
		}, 
		
		/**
		 * 寻路移动
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:52
		 */
		run(){
			@Override
			public void update(Entity entity) {
				
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null){
					if(combatComponent.IsDistanceTarget()){ // 攻击目标
						stateComponent.entityState.getCurrentState().subState.changeState(CombatState.attack);
					}
					else if(combatComponent.isCampTarget()){ // 寻路
						PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
						if(pathfindingComponent != null){
							pathfindingComponent.position.set(MapperTools.transformCM.get(combatComponent.target).position);
							pathfindingComponent.isPathfinding = true;
						}
					}
				}
			}
			
			@Override
			public void exit(Entity entity) {
				
				PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
				if(pathfindingComponent != null)
					pathfindingComponent.isPathfinding = true;
				
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				if(characterComponent != null)
					characterComponent.stopMove();
			}
		},

		/**
		 * 攻击 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:44
		 */
		attack(){
			@Override
			public void update(Entity entity) {
				
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				if(animationComponent.stateTime == 0){ // 动画播放结束才转身切换目标
					
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					CombatComponent combatComponent = MapperTools.combatCM.get(entity);
					
					if(combatComponent == null || combatComponent.seekTarget() != 2)
						stateComponent.entityState.getCurrentState().subState.changeState(CombatState.idle);
					else if(combatComponent.target != null)
						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
				}
			}
		},
		
		/**
		 * 玩完 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:34
		 */
		death(){
			@Override
			public void enter(Entity entity) {
				
				// 给所有攻击过的实体结算
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null){
					for(Entity attacker : combatComponent.attackerDamage.keys())
						MsgManager.instance.sendMessage(entity, attacker, MessageType.MSG_DEATH, null, false);// 发送角色销毁消息
				}
				
				GlobalInline.instance.getAshleyManager().engine.removeEntity(entity);
			}
		},
		
		/**
		 * 全局，每个状态之前都执行
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:58:59
		 */
		global(){
			@Override
			public void update(Entity entity) {
				
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				
				AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
				if(attributesComponent.curVit <= 0 && entity != GlobalInline.instance.get("hero")){ // TODO 英雄没死
					stateComponent.entityState.getCurrentState().subState.changeState(CombatState.death);
				}
			}
		};
		
		@Override
		public void enter(Entity entity) {
		}
		
		@Override
		public void update(Entity entity) {
		}
		
		@Override
		public void exit(Entity entity) {
		}
		
		@Override
		public boolean onMessage(Entity entity, Telegram telegram) {
			return false;
		}
	}
}


