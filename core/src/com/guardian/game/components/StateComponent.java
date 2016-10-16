package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.AshleyManager;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.util.VectorUtil;

/**
 * 精灵状态组件
 * 
 * @author D
 * @date 2016年8月28日 下午2:32:17
 */
public class StateComponent implements Component, Poolable  {
	
	/**
	 * 状态
	 * 数字是方向，按照小键盘数字方位
	 * 
	 * @author D
	 * @date 2016年9月16日 下午5:24:09
	 */
	public static enum States implements State<Entity>{
		
		/**
		 * 空闲 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:59
		 */
		idle(0){
			@Override
			public void update(Entity entity) {
				
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				if(combatComponent != null && combatComponent.IsdistanceTarget()){
					
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					stateComponent.entityState.changeState(States.attack);
				}
			}
		}, 
		
		/**
		 * 移动
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:52
		 */
		run(1){
			@Override
			public void update(Entity entity) {
				
				CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
				if(characterComponent != null){
					AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
					StateComponent stateComponent = MapperTools.stateCM.get(entity);
					
					// 移动精灵的动态刚体。方向乘以速度
					characterComponent.dynamicBody.setLinearVelocity(stateComponent.moveVector.nor().scl(attributesComponent.speed));
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
		attack(2){
			@Override
			public void update(Entity entity) {
				
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				CombatComponent combatComponent = MapperTools.combatCM.get(entity);
				
				if(combatComponent == null || !combatComponent.IsdistanceTarget())
					stateComponent.entityState.changeState(States.idle);
				else
					stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
			}
		},
		
		/**
		 * 玩完 
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:59:34
		 */
		death(3){
			@Override
			public void enter(Entity entity) {
				
				// TODO 销毁实体操作
				AshleyManager.engine.removeEntity(entity);
			}
		},
		
		/**
		 * 全局，每个状态之前都执行
		 * 
		 * @author D
		 * @date 2016年10月16日 下午7:58:59
		 */
		global(100){
			@Override
			public void update(Entity entity) {
				
				AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
				if(attributesComponent.VIT <= 0){
					MapperTools.stateCM.get(entity).entityState.changeState(States.death);;
				}
					
			}
		};
		
		public int value;
		
		private States(int value) {
			this.value = value;
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
	}
	
	/**
	 * 方位按小键盘数字, 5是中心0,0。
	 * 动画数组索引是从2(下)开始顺时针定义
	 * 
	 * @author D
	 * @date 2016年9月16日 下午6:52:37
	 */
	public static enum Orientation{
		
		d2(0, 0, -1), d1(1, -1, -1), d4(2, -1, 0), d7(3, -1, 1), 
		d8(4, 0, 1), d9(3, true, 1, 1), d6(2, true, 1, 0), d3(1, true, 1, -1);
		
		public int value; // 动画数组的索引
		
		public boolean isFlip = false; // 是否需要翻转
		
		/**
		 * 方向向量
		 */
		public Vector2 vector;
		
		private Orientation(int value, int x, int y){
			this(value, false, x, y);
		}
		
		private Orientation(int value, boolean isFlip, int x, int y){
			this.value = value;
			this.isFlip = isFlip;
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
		 * 返回向量对应的角度
		 * 
		 * @param vector3
		 * @return
		 */
		public static Orientation getOrientation(Vector3 vector){
			return getOrientation(VectorUtil.toVector2(vector));
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
	 */
	public StateMachine<Entity, States> entityState;
	
	/**
	 * 人物方向, 只有8个
	 */
	public Orientation orientation;
	
	/**
	 * 人物移动方向, 不限制
	 * 如果这个值限制成Orientation.vector的取值，就是只能8方向行走
	 */
	public Vector2 moveVector;
	
	public StateComponent() {
		entityState = new DefaultStateMachine<Entity, StateComponent.States>(null, States.idle, States.global); // 第一个参数(owner)在该组件添加到实体的时候赋值, 查看EntityManager类
		orientation = Orientation.d2;
	}
	
	/**
	 * 朝向
	 */
	public void lookAt(Vector2 position){
		
		Vector2 position2 = VectorUtil.toVector2(MapperTools.transformCM.get(entity).position);
		if(position.epsilonEquals(position2, 0))
			orientation = Orientation.d2;
		else
			orientation = Orientation.getOrientation(position.sub(position2));
	}
	
	/**
	 * 朝向
	 */
	public void lookAt(Vector3 position){
		lookAt(VectorUtil.toVector2(position));
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		entityState = null;
		orientation = null;
	}
}
