package com.guardian.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 实体的动画集合组件
 * 
 * @author D
 * @date 2016年8月28日 上午10:16:43
 */
public class AnimationComponent implements Component, Poolable {

	private HashMap<Integer, Animation> animations = new HashMap<>(8, 1);
	
	public AnimationComponent addAnimation(Integer stateName, Animation animation){
        this.animations.put(stateName, animation);
        return this;
	}
	
	public Animation getAnimation(Integer stateName){
		return animations.get(stateName);
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		
	}
}
