package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.ai.StateAdapter;
import com.guardian.game.tools.MapperTools;

/**
 * 精灵状态组件
 * 
 * @author D
 * @date 2016年8月28日 下午2:32:17
 */
public class StateComponent implements Component, Poolable  {
	
//	/**
//	 * 状态
//	 * 数字是方向，按照小键盘数字方位
//	 * 
//	 * @author D
//	 * @date 2016年9月16日 下午5:24:09
//	 */
//	public static enum States implements State<Entity>{
//		
//		/**
//		 * 空闲 
//		 * 
//		 * @author D
//		 * @date 2016年10月16日 下午7:59:59
//		 */
//		idle(0){
//			@Override
//			public void update(Entity entity) {
//				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//				if(combatComponent != null){
//					StateComponent stateComponent = MapperTools.stateCM.get(entity);
//					if(combatComponent.IsDistanceTarget()){
//						stateComponent.entityState.changeState(States.attack);
//					}
//					else if(combatComponent.target != null)
//						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
//				}
//			}
//		}, 
//		
//		/**
//		 * 移动
//		 * 
//		 * @author D
//		 * @date 2016年10月16日 下午7:59:52
//		 */
//		run(1){
//			@Override
//			public void update(Entity entity) {
//				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
//				if(characterComponent != null){
//					AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
//					StateComponent stateComponent = MapperTools.stateCM.get(entity);
//					
//					// 移动精灵的动态刚体。方向乘以速度
//					characterComponent.dynamicBody.setLinearVelocity(stateComponent.moveOrientationVector.nor().scl(attributesComponent.moveSpeed));
//				}
//			}
//			
//			@Override
//			public void exit(Entity entity) {
//				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
//				if(characterComponent != null)
//					characterComponent.dynamicBody.setLinearVelocity(Vector2.Zero);
//			}
//		},
//
//		/**
//		 * 攻击 
//		 * 
//		 * @author D
//		 * @date 2016年10月16日 下午7:59:44
//		 */
//		attack(2){
//			@Override
//			public void update(Entity entity) {
//				
//				AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
//				if(animationComponent.stateTime == 0){ // 动画播放结束才转身切换目标
//					
//					StateComponent stateComponent = MapperTools.stateCM.get(entity);
//					CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//					
//					if(combatComponent == null || combatComponent.seekTarget() != 2)
//						stateComponent.entityState.changeState(States.idle);
//					else if(combatComponent.target != null)
//						stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
//				}
//			}
//			
//			@Override
//			public void exit(Entity entity) {
//				
//				// TODO 不能清
//				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//				combatComponent.attackerDamage.clear(); // 清除被攻击记录
//			}
//		},
//		
//		/**
//		 * 玩完 
//		 * 
//		 * @author D
//		 * @date 2016年10月16日 下午7:59:34
//		 */
//		death(3){
//			@Override
//			public void enter(Entity entity) {
//				
//				// TODO 结算修改成combatComponent.attackerDamage
//				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//				if(combatComponent != null)
//					MsgManager.instance.sendMessage(entity, combatComponent.target, MessageType.MSG_DEATH, null, false);// 发送角色销毁消息
//				
//				GlobalInline.instance.getAshleyManager().engine.removeEntity(entity);
//			}
//		},
//		
//		/**
//		 * 全局，每个状态之前都执行
//		 * 
//		 * @author D
//		 * @date 2016年10月16日 下午7:58:59
//		 */
//		global(100){
//			@Override
//			public void update(Entity entity) {
//				
//				if(entity.flags == 0)
//					return;
//				
//				AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
//				if(attributesComponent.curVit <= 0 && entity != GlobalInline.instance.get("hero"))
//					MapperTools.stateCM.get(entity).entityState.changeState(States.death);
//			}
//		};
//		
//		public int value;
//		
//		private States(int value) {
//			this.value = value;
//		}
//
//		@Override
//		public void enter(Entity entity) {
//		}
//
//		@Override
//		public void update(Entity entity) {
//		}
//
//		@Override
//		public void exit(Entity entity) {
//		}
//
//		@Override
//		public boolean onMessage(Entity entity, Telegram telegram) {
//			return false;
//		}
//	}
	
	/**
	 * 方位按小键盘数字, 5是中心0,0。
	 * 动画数组索引是从2(下)开始顺时针定义
	 * 
	 * @author D
	 * @date 2016年9月16日 下午6:52:37
	 */
	public static enum Orientation{
		
		d2(0, 0, -1), d1(1, -1, -1), d4(2, -1, 0), d7(3, -1, 1), 
		d8(4, 0, 1), d9(5, 3, 1, 1), d6(6, 2, 1, 0), d3(7, 1, 1, -1);
		
		/**
		 * 动画数组的索引
		 */
		public int value; 
		
		/**
		 * 需要翻转的数组索引, 不需要-1
		 */
		public int flipValue;
		
		/**
		 * 方向向量
		 */
		public Vector2 vector;
		
		private Orientation(int value, int x, int y){
			this(value, -1, x, y);
		}
		
		private Orientation(int value, int flipValue, int x, int y){
			this.value = value;
			this.flipValue = flipValue;
			vector = new Vector2(x, y).nor();
		}
		
		/**
		 * 返回向量对应的角度
		 * 
		 * @param vector2
		 * @return
		 */
		public static Orientation getOrientation(Vector2 vector){
			return getOrientation(vector.angle());
		}
		
		/**
		 * 返回vector2在vector1的方向
		 * 
		 * @param vector1 当前位置
		 * @param vector2 目标位置
		 * @return
		 */
		public static Orientation getOrientation(Vector2 vector1, Vector2 vector2){
			return getOrientation(new Vector2(vector1).angle(vector2));
		}
		
		/**
		 * 角度判断方向
		 * 例:67.5度到112.5度之间是朝上
		 * 
		 * @param angle
		 * @return
		 */
		private static Orientation getOrientation(float angle){
			
			angle -= 22.5f; // 方便判断，例:45度到90度之间是朝上
			if(angle < 0)
				angle += 360; // 负数变正
			
			switch ((int)(angle/45)) {
			case 0:
				return d9;
			case 1:
				return d8;
			case 2:
				return d7;
			case 3:
				return d4;
			case 4:
				return d1;
			case 5:
				return d2;
			case 6:
				return d3;
			case 7:
				return d6;
				
			default:
				return d2;
			}
		}
	}
	
	/**
	 * 拥有该组件的实体
	 */
	public Entity entity;
	
	/**
	 * 状态机
	 * gdx-ai状态机不调用初始状态的enter方法，所以在创建状态机时初始状态传null
	 */
	public DefaultStateMachine<Entity, StateAdapter> entityState;
	
	/**
	 * 人物方向, 只有8个
	 */
	public Orientation orientation;
	
	/**
	 * 人物移动方向, 不限制
	 * 如果这个值限制成Orientation.vector的取值，就是只能8方向行走
	 */
	public final Vector2 moveOrientationVector = new Vector2(Orientation.d2.vector);
	
	public StateComponent() {
		orientation = Orientation.d2;
	}
	
	/**
	 * 朝向
	 */
	public void look(Vector2 vector){
		orientation = Orientation.getOrientation(vector);
		moveOrientationVector.set(vector.nor());
	}
	
	/**
	 * 朝向某点
	 */
	public void lookAt(Vector2 position){
		
		Vector2 position2 = MapperTools.transformCM.get(entity).position;
		if(position.epsilonEquals(position2, 0))
			orientation = Orientation.d2;
		else{
			Vector2 vector = position.cpy().sub(position2);
			orientation = Orientation.getOrientation(vector);
			moveOrientationVector.set(vector.nor());
		}
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		entity = null;
		entityState = null;
		orientation = Orientation.d2;
	}
}
