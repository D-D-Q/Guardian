package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.components.StateComponent.States;

/**
 * 实体的动画集合组件
 * 
 * @author D
 * @date 2016年8月28日 上午10:16:43
 */
public class AnimationComponent implements Component, Poolable {

	/**
	 * 根据状态和方向存储的动画数组
	 */
	public ObjectMap<States, Animation[]> animations;
	
	/**
	 * 存储已翻转的帧
	 */
	public ObjectMap<TextureRegion, Boolean> flipFrame;
	
	/**
	 * 当前动画已播放帧时间
	 * TODO 每次切换动画的时候其实应该重新置0，否则可能不从第一帧播放。暂不做
	 */
	public float stateTime;
	
	public AnimationComponent addAnimation(States state, Animation[] animations){
		this.animations.put(state, animations);
        return this;
	}
	
	public AnimationComponent() {
		animations = new ObjectMap<>(8, 1);
		flipFrame = new ObjectMap<>(8, 1);
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		animations.clear();
		flipFrame.clear();
		stateTime = 0;
	}
}
