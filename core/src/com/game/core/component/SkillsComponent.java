package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.Skill;
import com.game.core.Skill.EffectType;

/**
 * 技能组件
 * 
 * @author D
 * @date 2016年11月3日
 */
public class SkillsComponent implements Component, Poolable {
	
	public Entity entity;
	
	public Skill curSkill;

	/**
	 * 技能执行链
	 */
	private ObjectMap<EffectType, Array<Skill>> skillsPipelines;
	
	public SkillsComponent() {
		skillsPipelines = new ObjectMap<>(4);
	}
	
	/**
	 * 添加技能
	 * 
	 * @param skill
	 */
	public void addSkill(Skill skill){
		
		Array<Skill> array = skillsPipelines.get(skill.effectType);
		if(array == null)
			skillsPipelines.put(skill.effectType, (array = new Array<>(4)));
		
		array.add(skill);
		array.sort();
		skill.add(entity);
	}
	
	/**
	 * 移出技能
	 * 
	 * @param skill
	 * @return
	 */
	public void remove(Skill skill){
		
		Array<Skill> array = skillsPipelines.get(skill.effectType);
		if(array != null && array.removeValue(skill, true))
			skill.remove(entity);
	}
	
	/**
	 * 获得技能效果链
	 * @param effectType
	 * @return
	 */
	public Array<Skill> getSkillsPipeline(EffectType effectType){
		Array<Skill> array = skillsPipelines.get(effectType);
		if(array == null)
			return new Array<Skill>(0);
		return array;
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		entity = null;
		skillsPipelines.clear();
		curSkill = null;
	}
}
