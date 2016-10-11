package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 战斗检查组件
 * 
 * @author D
 * @date 2016年9月20日 下午8:50:01
 */
public class CombatComponent implements Component, Poolable{
	
	/**
	 * 目标
	 */
	public Entity entity;
	
	// 警戒范围
	
	// 攻击范围

	@Override
	public void reset() {
		entity = null;
	}
}
