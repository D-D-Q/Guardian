package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.game.core.GlobalInline;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CombatComponent;
import com.game.core.manager.MsgManager;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
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
	idle(){
		public DefaultStateMachine<Entity, IdleState> subState = new DefaultStateMachine<>(null, IdleState.idle, IdleState.global);
		
		@Override
		public void enter(Entity entity) {
			subState.setOwner(entity);
			subState.setInitialState(IdleState.idle);
		}
		
		@Override
		public void update(Entity entity) {
			subState.update();
		}
	}, 

	/**
	 * 攻击 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:44
	 */
	combat(){
		public DefaultStateMachine<Entity, CombatState> subState = new DefaultStateMachine<>(null, CombatState.idle, CombatState.global);
		
		@Override
		public void enter(Entity entity) {
			subState.setOwner(entity);
			subState.setInitialState(CombatState.idle);
		}
		
		@Override
		public void update(Entity entity) {
			subState.update();
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
			if(entity.flags == 0)
				return;
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
	
	/**
	 * 空闲子状态
	 * @author D
	 * @date 2016年11月16日
	 */
	private enum IdleState implements State<Entity>{
		
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
				if(combatComponent != null){
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					if(combatComponent.IsDistanceTarget()){
						stateComponent.entityState.changeState(CharacterState.combat);
					}
					else if(combatComponent.target != null)
						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
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
	private enum CombatState implements State<Entity>{
		
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
				if(combatComponent != null){
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					if(combatComponent.IsDistanceTarget()){
						stateComponent.entityState.changeState(CombatState.attack);
					}
					else if(combatComponent.target != null)
						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
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
						stateComponent.entityState.changeState(States.idle);
					else if(combatComponent.target != null)
						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
				}
			}
			
			@Override
			public void exit(Entity entity) {
				
				// TODO 不能清
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				combatComponent.attackerDamage.clear(); // 清除被攻击记录
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
				
				// TODO 结算修改成combatComponent.attackerDamage
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null)
					MsgManager.instance.sendMessage(entity, combatComponent.target, MessageType.MSG_DEATH, null, false);// 发送角色销毁消息
				
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
				
				AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
				if(attributesComponent.curVit <= 0 && entity != GlobalInline.instance.get("hero"))
					MapperTools.stateCM.get(entity).entityState.changeState(States.death);
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


