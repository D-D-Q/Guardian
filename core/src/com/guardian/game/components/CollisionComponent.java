package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;

/**
 * 碰撞检测, 不包含物理属性。
 * 和精灵本身的刚体分开的原因是，可以实现进入攻击范围或警戒范围等功能
 * 暂时只支持圆形吧
 * 
 * @author D
 * @date 2016年9月17日 上午8:01:16
 */
public class CollisionComponent implements Component, Poolable{

	/**
	 * 刚体，系统赋值
	 */
	public Body rigidBody;
	
	/**
	 * 半径
	 */
	public float radius;
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		PhysicsManager.disposeBody(rigidBody);
		radius = 0f;
	}
}
