package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
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
	 * 全局
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:58:59
	 */
	global(){
		@Override
		public void update(Entity entity) {
			if(entity.flags == 0) // TODO 被回收的entity
				return;
		}
	},
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	idle(null, HeroState.global){
		@Override
		public void update(Entity entity) {
			
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
				
			// 有目标就切合战斗状态
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			if(combatComponent.seekTarget() != 0){
				stateComponent.entityState.changeState(HeroState.combat);
			}
			else{
				stateComponent.entityState.changeState(HeroState.idle_idle);
			}
		}
	}, 

	/**
	 * 全局，每个状态之前都执行
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:58:59
	 */
	idle_global(){
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			Vector2 move = MoveUtil.getMove();
			if(move.isZero()){
				
				// 有目标就切合战斗状态
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent.seekTarget() != 0){
					stateComponent.entityState.changeState(HeroState.idle);
				}
			}
		}
	},
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	idle_idle(HeroState.idle, HeroState.idle_global){
		
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.idle;
		}
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			Vector2 move = MoveUtil.getMove();
			if(!move.isZero()){
				stateComponent.entityState.changeState(HeroState.idle_run);
			}
		}
	}, 
	
	/**
	 * 移动
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:52
	 */
	idle_run(HeroState.idle, HeroState.idle_global){
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.run;
		}
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			Vector2 move = MoveUtil.getMove();
			if(move.isZero()){
				stateComponent.entityState.changeState(HeroState.idle_idle);
			}
			else{
				stateComponent.orientation = Orientation.getOrientation(move);
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				characterComponent.move();
			}
		}
		
		@Override
		public void exit(Entity entity) {
			CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
			characterComponent.stopMove();
		}
	},

	/**
	 * 战斗状态
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:44
	 */
	combat(null, HeroState.global){
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			stateComponent.entityState.changeState(HeroState.combat_idle);
		}
		
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
	combat_global(){
		@Override
		public void update(Entity entity) {
			
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			if(attributesComponent.curVit <= 0){ 
				stateComponent.entityState.changeState(HeroState.combat_death);
				return;
			}
			
			Vector2 move = MoveUtil.getMove();
			if(!move.isZero()){
				if(!stateComponent.entityState.isInState(HeroState.combat_run)){
					stateComponent.entityState.changeState(HeroState.combat_run);
				}
			}
		}
	},
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	combat_idle(HeroState.combat, HeroState.combat_global){
		
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
			
			// 有目标就切攻击
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			if(combatComponent.seekTarget() == 2){
				stateComponent.entityState.changeState(HeroState.combat_attack);
			}
		}
	}, 
	
	/**
	 * 移动
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:52
	 */
	combat_run(HeroState.combat, HeroState.combat_global){
		
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.run;
		}
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			Vector2 move = MoveUtil.getMove();
			if(move.isZero()){
				stateComponent.entityState.changeState(HeroState.combat_idle);
			}
			else{
				stateComponent.orientation = Orientation.getOrientation(move);
				MapperTools.characterCM.get(entity).move();
			}
		}
		
		@Override
		public void exit(Entity entity) {
			CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
			characterComponent.stopMove();
		}
	},

	/**
	 * 攻击 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:44
	 */
	combat_attack(HeroState.combat, HeroState.combat_global){
		
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
				
				if(combatComponent.seekTarget() == 2){
					stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
				}
				else{
					stateComponent.entityState.changeState(HeroState.combat_idle);
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
	combat_death(HeroState.combat, HeroState.combat_global){
		@Override
		public void enter(Entity entity) {
			
			// TODO 游戏结束
		}
	};
	
	/**
	 * 父状态
	 */
	private final StateAdapter parentState;
	
	/**
	 * 全局状态
	 */
	private final StateAdapter globalState;
	
	private HeroState() {
		this(null, null);
	}
	
	private HeroState(StateAdapter parentState) {
		this(parentState, null);
	}
	
	private HeroState(StateAdapter parentState, StateAdapter globalState){
		this.parentState = parentState;
		this.globalState = globalState;
	}
	
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
	public StateAdapter getParentState() {
		return parentState;
	}
	
	@Override
	public StateAdapter getGlobalState() {
		return globalState;
	}
}


