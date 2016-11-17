package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.GlobalInline;
import com.game.core.system.PhysicsSystem;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.tools.MapperTools;

/**
 * 物理刚体。
 * 精灵本身的刚体，通常和精灵一样大小
 * 
 * 包含一个动态刚体和一个静态刚体， 可以防止精灵互相碰撞的物理效果产生力的传导而改变精灵位置
 * 
 * 暂时只支持圆形吧
 * 
 * @author D
 * @date 2016年10月12日
 */
public class CharacterComponent  implements Component, Poolable {
	
	/**
	 * 该组件的实体
	 */
	public Entity entity;
	
	/**
	 * 精灵的动态刚体，系统赋值
	 * 用于精灵的移动，和CollisionComponent的碰撞检测，和精灵的staticBody的碰撞
	 * dynamicBody保证了精灵的物理移动能力和碰撞监测功能
	 */
	public Body dynamicBody;
	
	/**
	 * 精灵的静态刚体，系统赋值
	 * 用于精灵的固定，只和dynamicBody碰撞
	 * staticBody保证了精灵不会被其他精灵撞动
	 */
	public Body staticBody;
	
	/**
	 * 角色刚体大小半径。如果不配置，默认取值spriteWidth/2
	 */
	public float radius;
	
	/**
	 * 移动角色
	 * 向当前方向
	 */
	public void move(){
		
		// 设置方向和状态
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(stateComponent != null){
			stateComponent.moveOrientationVector.set(stateComponent.orientation.vector);
			
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			
			// 移动精灵的动态刚体。方向乘以速度
			dynamicBody.setLinearVelocity(stateComponent.moveOrientationVector.nor().scl(attributesComponent.moveSpeed));
		}
	}
	
	/**
	 * 移动角色
	 * 
	 * @param vector2 方向
	 */
	public void move(Vector2 vector2){
		
		// 设置方向和状态
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(stateComponent != null){
			stateComponent.orientation = Orientation.getOrientation(vector2);
			stateComponent.moveOrientationVector.set(vector2.nor()); // 保留方向
//			stateComponent.moveOrientationVector.set(stateComponent.orientation.vector);
//			stateComponent.entityState.changeState(States.run);
			
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			
			// 移动精灵的动态刚体。方向乘以速度
			dynamicBody.setLinearVelocity(stateComponent.moveOrientationVector.nor().scl(attributesComponent.moveSpeed));
		}
	}
	
	/**
	 * 移动角色
	 * 
	 * @param Vector3 位置
	 */
	public void moveTo(Vector2 position){
		
		Vector2 position2 = MapperTools.transformCM.get(entity).position;
		if(position.epsilonEquals(position2, 0))
			return;
		
		move(position.sub(position2));
	}
	
	/**
	 * 移动角色停止
	 * 
	 * @param vector2 速度和方向
	 */
	public void stopMove(){
		dynamicBody.setLinearVelocity(Vector2.Zero);
	}

	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		PhysicsSystem physicsSystem = GlobalInline.instance.getAshleyManager().engine.getSystem(PhysicsSystem.class);
		physicsSystem.physicsManager.disposeBody(dynamicBody);
		physicsSystem.physicsManager.disposeBody(staticBody);
		radius = 0f;
	}
}
