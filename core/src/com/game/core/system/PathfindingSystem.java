package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.game.core.component.PathfindingComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * AI执行系统
 * 
 * @author D
 * @date 2016年10月18日
 */
public class PathfindingSystem extends IntervalIteratingSystem{
	
	/**
	 * @param interval 计算间隔
	 * @param priority
	 */
	public PathfindingSystem(float interval, int priority) {
		super(FamilyTools.AIF, interval, priority);
	}

	@Override
	protected void processEntity(Entity entity) {
		
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		if(pathfindingComponent.isPathfinding)
			pathfindingComponent.update(GdxAI.getTimepiece().getDeltaTime());
	}
}
