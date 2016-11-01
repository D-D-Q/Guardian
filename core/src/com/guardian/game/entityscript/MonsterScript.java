package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.script.EntityScript;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.tools.MapperTools;

/**
 * 怪物脚本
 * 
 * @author D
 * @date 2016年10月13日 下午10:13:52
 */
public class MonsterScript extends EntityScript {
	
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