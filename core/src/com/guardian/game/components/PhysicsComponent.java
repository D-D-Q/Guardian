package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.physics.PhysicsManager;

/**
 * 物理刚体。
 * 精灵本身的刚体，通常和精灵一样大小
 * 暂时只支持圆形吧
 * 
 * @author D
 * @date 2016年10月12日
 */
public class PhysicsComponent  implements Component, Poolable {
	
	/**
	 * 刚体，系统赋值
	 */
	public Body body;
	
	/**
	 * 刚体类型
	 */
	public BodyType bodyType;
	
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
		PhysicsManager.disposeBody(body);
		bodyType = null;
		radius = 0f;
	}
}
