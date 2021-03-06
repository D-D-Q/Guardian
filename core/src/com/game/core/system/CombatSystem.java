package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.SkillsComponent;
import com.game.core.component.TextureComponent;
import com.game.core.manager.MsgManager;
import com.game.core.skill.Skill;
import com.game.core.skill.Skill.EffectType;
import com.game.core.skill.Skill.TriggerType;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.tools.MessageType;

/**
 * 战斗系统
 * 
 * @author D
 * @date 2016年9月18日 下午9:59:11
 */
public class CombatSystem extends IteratingSystem  {

	/**
	 * @param priority 系统调用优先级
	 */
	public CombatSystem(int priority) {
		super(FamilyTools.combatF, priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(!stateComponent.entityState.isInState(combatComponent.combatState)) // TODO 不是战斗状态。最好能角色进入战斗状态才进入这个系统的entities里
			return;
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		if(textureComponent.textureRegion != combatComponent.attackTextureRegion[stateComponent.orientation.value]){ // 不是攻击帧
			combatComponent.isSendAttackMessage = false;
			return;
		}
		// 已经触发过攻击事件了 || 失去目标了
		if(combatComponent.isSendAttackMessage || !combatComponent.IsDistanceTarget())
			return;
		combatComponent.isSendAttackMessage = true;
		
		SkillsComponent skillsComponent = MapperTools.skillsCM.get(entity);
		if(skillsComponent.curSkill == null)
			return;
			
		// 攻击方技能
		float damage = skillsComponent.curSkill.execute(entity, combatComponent.target, 0); // 伤害
		for(Skill skill : skillsComponent.getSkillsPipeline(EffectType.attack)){
			if(skill.triggerType == TriggerType.passive){
				damage = skill.execute(entity, combatComponent.target, damage);
			}
		}
		
		// 防御方技能
		SkillsComponent targetSkillsComponent = MapperTools.skillsCM.get(combatComponent.target);
		if(targetSkillsComponent != null){
			for(Skill targetSkill : targetSkillsComponent.getSkillsPipeline(EffectType.defense)){
				if(targetSkill.triggerType == TriggerType.passive){
					damage = targetSkill.execute(combatComponent.target, entity, damage);
				}
			}
		}
		
		AnimationComponent animationComponent = MapperTools.animationCM.get(combatComponent.target);
		
		if(damage > 0){
			if(damage >= 1){ // 低于1伤害忽略
				AttributesComponent targetAttributesComponent = MapperTools.attributesCM.get(combatComponent.target);
				targetAttributesComponent.curVit -= damage; // 减体力
				
				CombatComponent targetCombatComponent = MapperTools.combatCM.get(combatComponent.target);
				targetCombatComponent.attackerDamage.put(entity, damage); // 记录攻击者和伤害
				
				animationComponent.addSubtitle(String.format("%.0f", damage));
			}
		}
		else
			animationComponent.addSubtitle("miss"); // 未命中
		
//		根据tile计算目标的代码，不用tile方式了
//		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
//		Vector2 vector = new Vector2(transformComponent.getMapPosition()); 
//		vector.add(stateComponent.orientation.vector); // 根据面向的方向，计算被攻击的位置
//		
//		for(Entity targetEntity : getEntities()){
//			TransformComponent targetTransformComponent = MapperTools.transformCM.get(targetEntity);
//			if(vector.equals(targetTransformComponent.getMapPosition())){ // 目标所在位置等于被攻击的位置，是被攻击的目标
//				
//			}
//		}
		
		// 发送攻击消息
		MsgManager.instance.sendMessage(combatComponent.target, entity, MessageType.MSG_ATTACK, damage, false);
		MsgManager.instance.sendMessage(entity, combatComponent.target, MessageType.MSG_ATTACKED, damage, false);
	}
}
