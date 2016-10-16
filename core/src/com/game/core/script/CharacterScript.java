package com.game.core.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 角色脚本
 * 额外提供一些的方法
 * 
 * @author D
 * @date 2016年10月15日 下午2:41:25
 */
public class CharacterScript extends EntityScript {
	
	/**
	 * 目标实体
	 */
	public Entity target;

	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		return false;
	}

	@Override
	public void beginContact(Contact contact, Entity target) {

	}

	@Override
	public void endContact(Contact contact, Entity target) {

	}
	
	/**
	 * 进入攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void enterATKRange(Contact contact, Entity target) {
		
	}
	
	/**
	 * 离开攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void leaveATKRange(Contact contact, Entity target) {
		
	}

	@Override
	public void update(float deltaTime) {
		
	}

}
