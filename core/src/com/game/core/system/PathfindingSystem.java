package com.game.core.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guardian.game.tools.FamilyTools;

/**
 * AI系统
 * 寻路目标，躲避同类碰撞
 * 
 * @author D
 * @date 2016年10月18日
 */
public class PathfindingSystem extends IteratingSystem implements EntityListener{
	
	public PathfindingSystem(int priority) {
		super(FamilyTools.AIF, priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
//		float deltaTime = GdxAI.getTimepiece().getDeltaTime();
//		for (int i = 0; i < characters.size; i++) {
//			characters.get(i).update(deltaTime);
//		}
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(this);
	}

	@Override
	public void removedFromEngine (Engine engine) {
		super.removedFromEngine(engine);
		engine.removeEntityListener(this);
	}
	
	@Override
	public void entityAdded(Entity entity) {
		
//		CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(character, proximity);
//		Seek<Vector2> seekSB = new Seek<Vector2>(character, target);
		
//		PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f);
//		prioritySteeringSB.add(collisionAvoidanceSB);
//		prioritySteeringSB.add(seekSB);
	}

	@Override
	public void entityRemoved(Entity entity) {
		
	}
}
