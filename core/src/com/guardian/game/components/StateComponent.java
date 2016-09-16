package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 精灵状态组件
 * 
 * @author D
 * @date 2016年8月28日 下午2:32:17
 */
public class StateComponent implements Component, Poolable  {

	/**
	 * 状态
	 */
	//TODO 精灵状态写死了
	public int state = 0;
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		state = 0;
	}
}
