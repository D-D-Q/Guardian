package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.guardian.game.components.CombatComponent;
import com.guardian.game.components.MessageComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.State;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * 战斗系统
 * 	ContactListener
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
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(stateComponent.state != State.attack) // 不是战斗状态。最好能角色进入战斗状态才进入这个系统的entities里
			return;
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 vector = new Vector2(transformComponent.getMapPosition()); 
		vector.add(stateComponent.orientation.vector); // 根据面向的方向，计算被攻击的位置
		
		
		for(Entity targetEntity : getEntities()){
			TransformComponent targetTransformComponent = MapperTools.transformCM.get(targetEntity);
			if(vector.equals(targetTransformComponent.getMapPosition())){ // 目标所在位置等于被攻击的位置，是被攻击的目标
				// TODO处理被攻击
				
//				return;
			}
		}
//		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//		
//		MessageComponent messageComponent = MapperTools.messageCM.get(entity);
//		MessageComponent targetMessageComponent = MapperTools.messageCM.get(combatComponent.entity);
//		
//		MessageHandlingSystem.getMessageManager().dispatchMessage(messageComponent, targetMessageComponent, MessageHandlingSystem.MSG_ATTACK, entity);
	}
}
