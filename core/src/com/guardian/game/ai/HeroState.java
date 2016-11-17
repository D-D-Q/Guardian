package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.game.core.ai.StateAdapter;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CombatComponent;
import com.guardian.game.animation.CharacterAnimation;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.util.MoveUtil;

/**
 * 分层有限状态机(HFSM), 角色使用
 * 
 * @author D
 * @date 2016年11月16日
 */
public enum HeroState implements StateAdapter{
	
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
			if(entity.flags == 0) // TODO 被回收的entity
				return;
			
			// 初始状态。 gdx-ai状态机不调用初始状态的enter方法，所以在创建状态机时初始状态传null
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			if(stateComponent.entityState.getCurrentState() == null){
				stateComponent.entityState.changeState(HeroState.idle);
			}
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
	
	private HeroState() {
		subState = null;
		initialState = null;
	}
	
	/**
	 * @param initialState 子状态的初始状态
	 * @param globalState 子状态的全局状态
	 */
	private HeroState(StateAdapter initialState, StateAdapter globalState) {
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
		subState.setOwner(entity);
		subState.update();
	}
	
	@Override
	public void exit(Entity entity) {
		subState.setOwner(entity);
		subState.getCurrentState().exit(entity);
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
	
	@Override
	public DefaultStateMachine<Entity, StateAdapter> getSubState() {
		return subState;
	}
	
	/**
	 * 空闲子状态
	 * @author D
	 * @date 2016年11月16日
	 */
	public enum IdleState implements StateAdapter {
		
		/**
		 * 空闲 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:59
		 */
		idle(){
			
			@Override
			public void enter(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				animationComponent.curAnimation = CharacterAnimation.idle;
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
			public void enter(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				animationComponent.curAnimation = CharacterAnimation.run;
			}
			
			@Override
			public void update(Entity entity) {
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				characterComponent.move();
			}
			
			@Override
			public void exit(Entity entity) {
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				characterComponent.stopMove();
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
				
				Vector2 move = MoveUtil.getMove();
				if(move.isZero()){
					
					stateComponent.entityState.getCurrentState().getSubState().changeState(HeroState.IdleState.idle);
					
					// 有目标就切合战斗状态
					CombatComponent combatComponent = MapperTools.combatCM.get(entity);
					if(combatComponent != null && combatComponent.target != null){
						stateComponent.entityState.changeState(HeroState.combat);
						return;
					}
				}
				else{
					stateComponent.orientation = Orientation.getOrientation(move);
					stateComponent.entityState.getCurrentState().getSubState().changeState(HeroState.IdleState.run);
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
		
		@Override
		public DefaultStateMachine<Entity, StateAdapter> getSubState() {
			return null;
		}
	}
	
	/**
	 * 战斗子状态
	 * @author D
	 * @date 2016年11月16日
	 */
	public enum CombatState implements StateAdapter {
		
		/**
		 * 空闲 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:59
		 */
		idle(){
			
			private float deltaTime; // 空闲状态持续时间
			
			@Override
			public void enter(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				animationComponent.curAnimation = CharacterAnimation.idle;
				deltaTime = 0;
			}
			
			@Override
			public void update(Entity entity) {
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				
				if(deltaTime >= 3){ // 持续时间3秒不攻击， 退出战斗
					stateComponent.entityState.changeState(HeroState.idle);
					return ;
				}
				deltaTime += Gdx.graphics.getDeltaTime();
				
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null){
					if(combatComponent.IsDistanceTarget()){ // 攻击目标
						stateComponent.entityState.getCurrentState().getSubState().changeState(CombatState.attack);
					}
					else if(combatComponent.target != null){
						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
					}
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
			public void enter(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				animationComponent.curAnimation = CharacterAnimation.run;
			}
			
			@Override
			public void update(Entity entity) {
				MapperTools.characterCM.get(entity).move();
			}
			
			@Override
			public void exit(Entity entity) {
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
			public void enter(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				animationComponent.curAnimation = CharacterAnimation.attack;
			}
			
			@Override
			public void update(Entity entity) {
				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
				if(animationComponent.stateTime == 0){ // 动画播放结束才转身切换目标
					
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					CombatComponent combatComponent = MapperTools.combatCM.get(entity);
					
					if(combatComponent == null || combatComponent.seekTarget() != 2){
						stateComponent.entityState.getCurrentState().getSubState().changeState(CombatState.idle);
					}
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
				
				// TODO 游戏结束
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
				if(attributesComponent.curVit <= 0){ 
					stateComponent.entityState.getCurrentState().getSubState().changeState(CombatState.death);
				}
				
				Vector2 move = MoveUtil.getMove();
				if(move.isZero()){
					stateComponent.entityState.getCurrentState().getSubState().changeState(HeroState.CombatState.idle);
				}
				else{
					stateComponent.orientation = Orientation.getOrientation(move);
					stateComponent.entityState.getCurrentState().getSubState().changeState(HeroState.CombatState.run);
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

		@Override
		public DefaultStateMachine<Entity, StateAdapter> getSubState() {
			return null;
		}
	}
}


