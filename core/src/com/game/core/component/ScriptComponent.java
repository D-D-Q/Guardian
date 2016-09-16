package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 脚本组件抽象类
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public abstract class ScriptComponent implements Component, Poolable{

	abstract public void update(float delta); 
	
	@Override
	public void reset() {
	}
}
