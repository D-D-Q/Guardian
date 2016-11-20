package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.GlobalInline;
import com.game.core.system.PhysicsSystem;
import com.guardian.game.components.StateComponent;
import com.guardian.game.tools.MapperTools;

/**
 * 寻路组件
 *  QueryCallback 获取躲避的对象的会调接口
 *  RayCastCallback 射线碰撞检测回调接口
 *  Steerable AI主体接口，忽略角速度
 * 
 * @author D
 * @date 2016年10月18日
 */
public class PathfindingComponent implements QueryCallback, RayCastCallback, Component, Steerable<Vector2>, Poolable {
	
	/**
	 * 该组件的实体
	 */
	public Entity entity;
	
	/**
	 * 目标位置
	 */
	public final Vector2 position = new Vector2();
	
	/**
	 * 是否寻路
	 */
	public boolean isPathfinding;
	
	/**
	 * AI行为
	 */
	private SteeringBehavior<Vector2> steeringBehavior;
	
	/**
	 * 判断躲避其他AI主体, 一次循环只能计算一个角色所以静态就可以
	 */
	private static final Array<PathfindingComponent> agents = new Array<PathfindingComponent>(false, 8);
	
	/**
	 * 保存AI行为计算结果, 一次循环只能计算一个角色所以静态就可以
	 */
	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
	
	/**
	 * 临时变量，用于保存射线是否碰撞
	 */
	private boolean isCollided;
	
	public PathfindingComponent() {
		
		PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(this, 0.0001f);
		
		// 10开始躲避的距离，从两个AI的边缘算起
		CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(this, new Box2dRadiusProximity(this, agents, 10));
		prioritySteeringSB.add(collisionAvoidanceSB);
		
		Seek<Vector2> seekSB = new Seek<Vector2>(this, new Box2dLocation(position));
		prioritySteeringSB.add(seekSB);
		
		steeringBehavior = prioritySteeringSB;
	}
	
	/** 
	 * 添加躲避对象
	 */
	@Override
	public boolean reportFixture(Fixture fixture) {
		
		if(fixture.isSensor() || fixture.getBody().getType() != BodyType.DynamicBody )
			return true;
		
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get((Entity)fixture.getBody().getUserData());
		if(pathfindingComponent == null || pathfindingComponent == this) // 不是自己
			return true;
		
		agents.add(pathfindingComponent);
		
		return true;
	}
	
	/**
	 * 计算更新
	 * 忽略角度速度。不必改变刚体的方向，因为角色就8个的方向，移动时通过目标位置计算
	 *  
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		
		if (steeringBehavior == null)
			return;
		
		CharacterComponent characterComponent = MapperTools.characterCM.get(entity);
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		
		steeringBehavior.calculateSteering(steeringOutput);
		
		// 取直线加速度的方向，移动角色
		if (!steeringOutput.linear.isZero()) {
			
//			Log.info(this, stateComponent.moveOrientationVector.dot(steeringOutput.linear));
//			Orientation orientation = Orientation.getOrientation(steeringOutput.linear);
//			if(Math.abs(stateComponent.orientation.vector.angleRad() - orientation.vector.angleRad()) > Math.PI/2 ) 
////			if(stateComponent.moveOrientationVector.dot(steeringOutput.linear) < 0 ) // 转向大于90度就不移动
//				stateComponent.entityState.changeState(States.idle);
//			else{
//				isCollided = false;
//				Vector2 beginVector = characterComponent.dynamicBody.getPosition();
//				Vector2 endVector = VectorUtil.scl(steeringOutput.linear.nor(), getBoundingRadius() + 1).add(beginVector);
//				PhysicsManager.world.rayCast(this, beginVector, endVector); // 
////				Log.info(this, position.dst2(VectorUtil.scl(steeringOutput.linear.nor(), 1).add(position)));
//				if(isCollided)
//					stateComponent.entityState.changeState(States.idle);
//				else
//					characterComponent.move(steeringOutput.linear.nor());
//			}
			
//			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
//			if(combatComponent != null && combatComponent.target != null){
//				stateComponent.lookAt(MapperTools.transformCM.get(combatComponent.target).position);
//				steeringOutput.linear.add(stateComponent.moveOrientationVector); // 不走相反的方向
//			}
			
//			stateComponent.lookAt(position);
//			
//			characterComponent.move(steeringOutput.linear.nor());
			
			stateComponent.look(steeringOutput.linear); // 朝向当前要移动方向
		}
	}

	@Override
	public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		
		if(fixture.isSensor() || fixture.getBody().getUserData() == entity) // 自己
			return fraction;
		
		isCollided = true;
		return 0;
	}
	

	@Override
	public Vector2 getPosition() {
		
		if(entity == null)
			return Vector2.Zero;
		
		return MapperTools.transformCM.get(entity).position.cpy();
	}

	@Override
	public float getOrientation() {
		return 0f;
	}

	@Override
	public void setOrientation(float orientation) {
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return vector.angleRad();
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = (float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return new Box2dLocation();
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return 0.001f;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
	}

	@Override
	public float getMaxLinearSpeed() {
		return MapperTools.attributesCM.get(entity).moveSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
	}

	@Override
	public float getMaxLinearAcceleration() {
		return MapperTools.attributesCM.get(entity).moveSpeed;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
	}

	@Override
	public float getMaxAngularSpeed() {
		return 0f;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
	}

	@Override
	public float getMaxAngularAcceleration() {
		return 0f;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
	}

	@Override
	public Vector2 getLinearVelocity() {
		return MapperTools.characterCM.get(entity).dynamicBody.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return 0;
	}

	@Override
	public float getBoundingRadius() {
		return MapperTools.characterCM.get(entity).radius;
	}

	@Override
	public boolean isTagged() {
		return false;
	}

	@Override
	public void setTagged(boolean tagged) {
	}
	
	@Override
	public void reset() {
		entity = null;
		isPathfinding = false;
		position.setZero();
	}
	
	/**
	 * 实现box2的判断躲避其他AI主体
	 * 
	 * @author D
	 * @date 2016年10月19日
	 */
	private class Box2dRadiusProximity extends RadiusProximity<Vector2>{
		
		public Box2dRadiusProximity(PathfindingComponent owner, Array<PathfindingComponent> agents, float radius) {
			super(owner, agents, radius);
		}
		
		/**
		 * 两个角色之间的距离 < 躲避距离 + 两个角色大小的半径。就要躲避了
		 */
		@Override
		public int findNeighbors(ProximityCallback<Vector2> callback) {
			
			agents.clear();
			
			PhysicsSystem physicsSystem = GlobalInline.instance.getAshleyManager().engine.getSystem(PhysicsSystem.class);
			
			// 寻找躲避对象
			Vector2 position = owner.getPosition();
			float findRadius = owner.getBoundingRadius() + radius;
			physicsSystem.physicsManager.world.QueryAABB((QueryCallback)owner, 
					position.x - findRadius, position.y - findRadius, position.x + findRadius, position.y + findRadius);
			
			int agentCount = agents.size;
			int neighborCount = 0;
			
			float boundingRadius = owner.getBoundingRadius();
			Vector2 ownerPosition = owner.getPosition();
			
			for (int i = 0; i < agentCount; ++i) {
				Steerable<Vector2> currentAgent = agents.get(i);

				if (currentAgent != owner) { // 不是自己
					
					float squareDistance = ownerPosition.dst2(currentAgent.getPosition());
					float range = radius + boundingRadius + currentAgent.getBoundingRadius();

					if (squareDistance < range * range) {
						if (callback.reportNeighbor(currentAgent))
							neighborCount++;
					}
				}
			}

			return neighborCount;
		}
	}
	
	/**
	 * 位置变量类
	 * 
	 * @author D
	 * @date 2016年10月19日
	 */
	public static class Box2dLocation implements Location<Vector2> {

		Vector2 position;
		float orientation;

		public Box2dLocation () {
			this.position = new Vector2();
			this.orientation = 0;
		}
		
		public Box2dLocation (Vector2 position) {
			this.position = position;
			this.orientation = 0;
		}

		@Override
		public Vector2 getPosition () {
			return position;
		}

		@Override
		public float getOrientation () {
			return orientation;
		}

		@Override
		public void setOrientation (float orientation) {
			this.orientation = orientation;
		}

		@Override
		public Location<Vector2> newLocation () {
			return new Box2dLocation();
		}

		@Override
		public float vectorToAngle (Vector2 vector) {
			return vector.angleRad();
		}

		@Override
		public Vector2 angleToVector (Vector2 outVector, float angle) {
			outVector.x = (float)Math.sin(angle);
			outVector.y = (float)Math.cos(angle);
			return outVector;
		}
	}
}
