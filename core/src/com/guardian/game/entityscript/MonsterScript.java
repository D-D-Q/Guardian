package com.guardian.game.entityscript;

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
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		
		if(combatComponent.target == null){
			pathfindingComponent.position.set(1040, 480);
			pathfindingComponent.isPathfinding = true;
		}
		else if(combatComponent.IsDistanceTarget()){
			pathfindingComponent.isPathfinding = false;
			stateComponent.entityState.changeState(States.attack);
		}
		else if(combatComponent.isCampTarget()){
			pathfindingComponent.position.set(MapperTools.transformCM.get(combatComponent.target).position);
			pathfindingComponent.isPathfinding = true;
		}
	}
}
