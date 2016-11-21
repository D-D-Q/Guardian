package com.guardian.game.skills;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.game.core.skill.Skill;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.tools.MapperTools;

/**
 * 普通攻击
 * 
 * @author D
 * @date 2016年11月3日
 */
public class NormalAttack extends Skill {
	
	private static NormalAttack instance;
	
	private NormalAttack() {
		super(SkillType.attack, EffectType.active);
	}

	/**
	 * 使用非线程安全方式
	 * @return
	 */
	public static Skill getInstance(){
		
		if(instance == null)
			instance = new NormalAttack();
		
		return instance;
	}
	
	@Override
	public float execute(Entity entity, Entity target, float damage) {
		
		AttributesComponent entityAttributesComponent = MapperTools.attributesCM.get(entity);
		AttributesComponent targetAttributesComponent = MapperTools.attributesCM.get(target);
		
		// 计算命中
		float hit = Math.max(0.2f, (entityAttributesComponent.AGI + 1)/(targetAttributesComponent.AGI + 1) * 0.6f); // 最低命中0.2
		if(hit >= MathUtils.random()){ // 命中
			
			// 伤害浮动
			float atk = entityAttributesComponent.ATK * MathUtils.random(0.9f, 1f);
			float def = targetAttributesComponent.DEF * MathUtils.random(0.9f, 1f);
			
//			damage = Math.max(1, atk * ((atk + 1)/(atk + def + 1))); // 最低伤害 1
			damage = atk * ((atk + 1)/(atk + def + 1));
		}
		
		return damage;
	}
}
