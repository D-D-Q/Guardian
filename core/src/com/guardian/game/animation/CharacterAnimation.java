package com.guardian.game.animation;

import com.game.core.AnimationKey;

/**
 * 角色动画key
 * 
 * @author D
 * @date 2016年11月17日
 */
public enum CharacterAnimation implements AnimationKey {

	/**
	 * 站立
	 */
	idle, 
	
	/**
	 * 跑 
	 */
	run, 
	
	/**
	 * 攻击
	 */
	attack;
}
