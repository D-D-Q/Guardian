package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.game.core.GlobalInline;
import com.game.core.ai.StateAdapter;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.TransformComponent;
import com.game.core.manager.MsgManager;
import com.guardian.game.GAME;
import com.guardian.game.animation.CharacterAnimation;
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
public enum CharacterState implements StateAdapter{
	
	/**
	 * 顶层全局状态
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
	},
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	idle(null, CharacterState.global){
		@Override
		public void update(Entity entity) {
			
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			// 有目标就切合战斗状态
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			if(combatComponent.seekTarget() != 0){
				stateComponent.entityState.changeState(CharacterState.combat);
			}
			else{
				stateComponent.entityState.changeState(idle_idle);
			}
		}
	}, 
	
	/**
	 * 空闲子状态全局，每个状态之前都执行
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:58:59
	 */
	idle_global(){
		@Override
		public void update(Entity entity) {

			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			// 有目标就返回父状态
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			if(combatComponent.seekTarget() != 0){
				stateComponent.entityState.changeState(CharacterState.idle);
				return;
			}
		}
	},
	
	/**
	 * 空闲 
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:59
	 */
	idle_idle(CharacterState.idle, CharacterState.idle_global){
		
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.idle;
		}
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			if(!pathfindingComponent.position.isZero()){
				stateComponent.entityState.changeState(CharacterState.idle_run);
			}
		}
	}, 
	
	/**
	 * 移动
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:52
	 */
	idle_run(CharacterState.idle, CharacterState.idle_global){
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.run;
			
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			pathfindingComponent.isPathfinding = true;
		}
		
		@Override
		public void update(Entity entity) {
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			TransformComponent transformComponent = MapperTools.transformCM.get(entity);
			CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
			
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			if(pathfindingComponent.position.epsilonEquals(transformComponent.position, 1f)){ // 到达目标位置，误差1
				pathfindingComponent.position.setZero();
				stateComponent.entityState.changeState(CharacterState.idle_idle);
			}
			else{
				characterComponent.move();
			}
		}
		
		@Override
		public void exit(Entity entity) {
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			pathfindingComponent.isPathfinding = false;
			
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
	combat(null, CharacterState.global){
		
		@Override
		public void update(Entity entity) {
			
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			
			int seekTarget = combatComponent.seekTarget();
			if(seekTarget == 2){ // 攻击目标
				stateComponent.entityState.changeState(CharacterState.combat_attack);
			}
			else if(seekTarget == 1){ // 移动到目标
				stateComponent.entityState.changeState(CharacterState.combat_run);
			}
			else{
				stateComponent.entityState.changeState(CharacterState.idle);
			}
		}
		
		@Override
		public void exit(Entity entity) {
			super.exit(entity);
			
			// 退出战斗状态，清空被攻击记录
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			combatComponent.attackerDamage.clear(); // 清除被攻击记录
			
			// 重新寻路到目标地点
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			pathfindingComponent.position.set(GAME.position);
		}
	},
	
	/**
	 * 战斗子状态全局，每个状态之前都执行
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
				stateComponent.entityState.changeState(CharacterState.combat_death);
			}
		}
	},
	
	/**
	 * 移动到攻击目标
	 * 
	 * @author D
	 * @date 2016年10月16日 下午7:59:52
	 */
	combat_run(CharacterState.combat, CharacterState.combat_global){
		
		@Override
		public void enter(Entity entity) {
			AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
			animationComponent.curAnimation = CharacterAnimation.run;
			
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			pathfindingComponent.isPathfinding = true;
		}
		
		@Override
		public void update(Entity entity) {
			
			StateComponent stateComponent = MapperTools.stateCM.get(entity);
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			
			int seekTarget = combatComponent.seekTarget();
			if(seekTarget == 2){ // 攻击目标
				stateComponent.entityState.changeState(CharacterState.combat_attack);
			}
			else if(seekTarget == 1){ // 寻路
				PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
				pathfindingComponent.position.set(MapperTools.transformCM.get(combatComponent.target).position);
				
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				characterComponent.move();
			}
			else{ // 失去目标, 返回父状态
				stateComponent.entityState.changeState(CharacterState.combat);
			}
		}
		
		@Override
		public void exit(Entity entity) {
			PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
			pathfindingComponent.isPathfinding = false;
			
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
	combat_attack(CharacterState.combat, CharacterState.combat_global){
		
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
				
				int seekTarget = combatComponent.seekTarget();
				if(seekTarget == 2){
					stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
				}
				else if(seekTarget == 1){
					stateComponent.entityState.changeState(CharacterState.combat_run);
				}
				else{
					stateComponent.entityState.changeState(CharacterState.combat);
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
	combat_death(CharacterState.combat, CharacterState.combat_global){
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
	};
	
	/**
	 * 父状态
	 */
	private final StateAdapter parentState;
	
	/**
	 * 全局状态
	 */
	private final StateAdapter globalState;
	
	private CharacterState() {
		this(null, null);
	}
	
	private CharacterState(StateAdapter parentState) {
		this(parentState, null);
	}
	
	private CharacterState(StateAdapter parentState, StateAdapter globalState){
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


