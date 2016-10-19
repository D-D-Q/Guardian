package com.game.core.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.PathfindingComponent.Box2dLocation;
import com.guardian.game.GAME;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

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
	public void update(float deltaTime) {
		GdxAI.getTimepiece().update(deltaTime);
		super.update(GdxAI.getTimepiece().getDeltaTime());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		MapperTools.pathfindingCM.get(entity).update(deltaTime);
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(this.getFamily(), this);
	}

	@Override
	public void removedFromEngine (Engine engine) {
		super.removedFromEngine(engine);
		engine.removeEntityListener(this);
	}
	
	@Override
	public void entityAdded(Entity entity) {
		
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get(entity);
		pathfindingComponent.entity = entity;
		
		PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(pathfindingComponent, 0.0001f);
	
		CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(pathfindingComponent, pathfindingComponent.box2dRadiusProximity);
		prioritySteeringSB.add(collisionAvoidanceSB);
		
		Seek<Vector2> seekSB = new Seek<Vector2>(pathfindingComponent, new Box2dLocation(MapperTools.transformCM.get(GAME.hero).position)); //TODO 暂时写死
		prioritySteeringSB.add(seekSB);
		
		pathfindingComponent.steeringBehavior = prioritySteeringSB;
	}

	@Override
	public void entityRemoved(Entity entity) {
		
	}
	
}
