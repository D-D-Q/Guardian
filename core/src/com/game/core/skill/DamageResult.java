package com.game.core.skill;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.AnimationKey;
import com.badlogic.gdx.utils.Pools;

/**
 * 技能执行结果
 * 
 * @author D
 * @date 2016年11月21日
 */
public class DamageResult implements Poolable {
	
	/**
	 * 池化
	 */
	public static Pool<DamageResult> pools = Pools.get(DamageResult.class, 9);

	/**
	 * 主要目标
	 */
	public Entity target;
	
	/**
	 * 主要目标的伤害
	 */
	public float damage;
	
	/**
	 * 范围目标和伤害
	 */
	public ArrayMap<Entity, Float> rangeTarget;
	
	/**
	 * 伤害动画
	 */
	public ArrayMap<Skill, AnimationKey> animationKey;
	
	public DamageResult() {
		target = null;
		damage = 0f;
		rangeTarget = new ArrayMap<>(false, 8);
		animationKey = new ArrayMap<>(2);
	}

	@Override
	public void reset() {
		target = null;
		damage = 0f;
		rangeTarget.clear();
		animationKey.clear();
	}
}
