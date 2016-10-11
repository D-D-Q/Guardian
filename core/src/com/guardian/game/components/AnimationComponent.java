package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.components.StateComponent.State;

/**
 * 实体的动画集合组件
 * 
 * @author D
 * @date 2016年8月28日 上午10:16:43
 */
public class AnimationComponent implements Component, Poolable {

	private Animation[][] animations;
	
	public AnimationComponent addAnimation(State state, Animation[] animations){

		this.animations[state.value] = animations;
        return this;
	}
	
	public AnimationComponent() {
		animations = new Animation[State.values().length][8];
	}
	
	/**
	 * 获得动画
	 * 
	 * @param state 状态
	 * @param direction 方向
	 * @returncombat system
	 */
	public Animation getAnimation(State state, Orientation direction){
		return animations[state.value][direction.value];
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		animations = null;
	}
}
