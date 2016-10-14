package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.script.EntityScript;

/**
 * 脚本组件抽象类
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public class ScriptComponent implements Component, Telegraph, ContactListener, Poolable{

	/**
	 * 脚本对象。
	 */
	public EntityScript script;
	

	@Override
	public boolean handleMessage(Telegram msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 进入碰撞
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void beginContact(Contact contact) {
	}

	/**
	 * 离开碰撞
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

	@Override
	public void reset() {
		script = null;
	}
}
