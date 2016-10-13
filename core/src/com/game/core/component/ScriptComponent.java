package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 脚本组件抽象类
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public abstract class ScriptComponent implements Component, Telegraph, ContactListener, Poolable{

	public Entity entity;
	
	@Override
	public void reset() {
		entity = null;
		resetScript();
	}
	
	public abstract void resetScript();
}
