package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.game.core.component.PathfindingComponent;
import com.game.core.script.EntityScript;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.CombatComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 寒冰卫士脚本
 * 
 * @author D
 * @date 2016年10月13日 下午10:13:52
 */
public class HbwsScript extends EntityScript {
	
	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		
		super.message(messageType, sender, extraInfo);
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
		AttributesComponent senderAttributesComponent = MapperTools.attributesCM.get(sender);
		
		Log.info(this, attributesComponent.name + ": 我" + senderAttributesComponent.name + "被攻击到了。还有体力:" + attributesComponent.VIT);
		
		return true;
	}

	@Override
	public void update(float deltaTime) {
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		
		if(combatComponent.target == null){
			stateComponent.entityState.changeState(States.idle);
			return;
		}
		
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		
		if(combatComponent.IsDistanceTarget()){
			pathfindingComponent.isPathfinding = false;
			stateComponent.entityState.changeState(States.attack);
		}
		else if(combatComponent.isCampTarget()){
			pathfindingComponent.position.set(MapperTools.transformCM.get(combatComponent.target).position);
			pathfindingComponent.isPathfinding = true;
		}
	}
	
	@Override
	public boolean enterATKRange(Contact contact, Entity target) {
		return false;
	}
	
	@Override
	public boolean leaveATKRange(Contact contact, Entity target) {
		return false;
	}
}
