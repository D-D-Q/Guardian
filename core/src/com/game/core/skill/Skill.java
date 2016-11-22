package com.game.core.skill;

import com.badlogic.ashley.core.Entity;

/**
 * 技能基类
 * 普通攻击也划为技能的一种
 * 延迟类
 * 定时重复类
 * 固定增加类
 * 被动触发类
 * 主动触发类
 * 
 * @author D
 * @date 2016年11月3日
 */
public abstract class Skill implements Comparable<Skill>{
	
	/**
	 * 技能类型
	 */
	public SkillType skillType;
	
	/**
	 * 效果类型
	 */
	public EffectType effectType;
	
	/**
	 * 优先级, 默认100 以大优先
	 */
	public int priority;
	
	public Skill(SkillType skillType, EffectType effectType) {
		this(skillType, effectType, 100);
	}
	
	public Skill(SkillType skillType, EffectType effectType, int priority) {
		this.skillType = skillType;
		this.effectType = effectType;
		this.priority = priority;
	}
	
	/**
	 * 获得技能
	 * @param entity 技能拥有者
	 */
	public void add(Entity entity){}
	
	/**
	 * 技能执行之前
	 * @param entity 技能拥有者
	 * @param target 目标
	 */
	public void befor(Entity entity, Entity target){}
	
	/**
	 * 执行技能
	 * @param entity 技能拥有者
	 * @param target 目标
	 * @param damage 伤害值，0是未命中
	 * @return 新伤害值
	 */
	public float execute(Entity entity, Entity target, float damage){
		return damage;
	}
	
	/**
	 * 技能执行之后
	 * @param entity 技能拥有者
	 * @param target 目标
	 */
	public void after(Entity entity, Entity target){}
	
	/**
	 * 移出技能
	 * @param entity 技能拥有者
	 */
	public void remove(Entity entity){}
	
	/**
	 * 优先级
	 */
	@Override
	public int compareTo(Skill o) {
		return priority - o.priority;
	}
	
	/**
	 * 技能类型
	 * @author D
	 * @date 2016年11月3日
	 */
	public enum EffectType{
		
		/**
		 * 攻击类
		 * 例如增加伤害
		 */
		attack, 
		
		/**
		 * 防御类
		 * 例如抵消伤害
		 */
		defense,
		
		/**
		 * 增益类
		 * 例如增加攻击力属性
		 */
		buff
	}
	
	/**
	 * 触发类型
	 * @author D
	 * @date 2016年11月3日
	 */
	public enum TriggerType{
		
		/**
		 * 主动触发
		 */
		active,
		
		/**
		 * 被动触发
		 */
		passive
	}
}
