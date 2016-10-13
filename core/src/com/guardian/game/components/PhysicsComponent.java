package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;

/**
 * 物理组件。
 * 精灵本身的刚体，通常和精灵一样大小
 * 
 * @author D
 * @date 2016年10月13日 下午10:28:35
 */
public class PhysicsComponent  implements Component, Poolable {
	
	/**
	 * 刚体
	 */
	public Body rigidBody;
	
	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		PhysicsManager.disposeBody(rigidBody);
	}
}	
