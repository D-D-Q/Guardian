package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;

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
	 * 精灵的动态刚体，系统赋值
	 * 用于精灵的移动，和CollisionComponent的碰撞检测，和staticBody的碰撞
	 * dynamicBody保证了精灵的物理移动能力和碰撞监测功能
	 */
	public Body dynamicBody;
	
	/**
	 * 精灵的静态刚体，系统赋值
	 * 用于精力的固定，和dynamicBody的碰撞
	 * staticBody保证了精灵不会被其他精灵撞动
	 */
	public Body staticBody;
	
	/**
	 * 半径
	 */
	public float radius;

	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		PhysicsManager.disposeBody(dynamicBody);
		PhysicsManager.disposeBody(staticBody);
		radius = 0f;
	}
}
