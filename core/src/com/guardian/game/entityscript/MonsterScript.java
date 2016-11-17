package com.guardian.game.entityscript;

import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.script.EntityScript;
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
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		
		if(combatComponent.target == null){
			pathfindingComponent.position.set(1040, 480);
			pathfindingComponent.isPathfinding = true;
		}
	}
}
