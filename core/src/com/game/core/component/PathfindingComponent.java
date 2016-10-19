package com.game.core.component;

import java.security.acl.Owner;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.components.CharacterComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.tools.MapperTools;

/**
 * 寻路组件
 *  QueryCallback 获取躲避的对象的接口
 *  Steerable AI主体接口，忽略角速度
 * 
 * @author D
 * @date 2016年10月18日
 */
public class PathfindingComponent implements QueryCallback, Component, Steerable<Vector2>, Poolable {
	
	/**
	 * 该组件的实体
	 */
	public Entity entity;
	
	/**
	 * 是否寻路
	 */
	public boolean isPathfinding;
	
	/**
	 * 保存上次计算时候的角色位置
	 */
	public final Vector2 oldposition = new Vector2();
	
	/**
	 * AI行为
	 */
	public SteeringBehavior<Vector2> steeringBehavior;
	
	/**
	 * 判断躲避其他AI主体
	 */
	public Box2dRadiusProximity box2dRadiusProximity;
	
	/**
	 * 保存AI行为计算结果
	 */
	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
	
	public PathfindingComponent() {
		box2dRadiusProximity = new Box2dRadiusProximity(this, 10); // 10开始躲避的距离，从两个AI的边缘算起
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
		
//		agents.add(pathfindingComponent); // 直接添加泛型编译不通过
		((Array<PathfindingComponent>)box2dRadiusProximity.getAgents()).add(pathfindingComponent);
		
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
		
		steeringBehavior.calculateSteering(steeringOutput);
		
		// 取直线加速度的方向，移动角色
		if (!steeringOutput.linear.isZero()) {
			
			if(characterComponent.dynamicBody.getPosition().dst2(oldposition) < characterComponent.radius){ // 移动距离太近，可能不挡住暂停
				oldposition.setZero();
				
				StateComponent stateComponent = MapperTools.stateCM.get(entity);
				stateComponent.entityState.changeState(States.idle);
				stateComponent.look(steeringOutput.linear.nor());
			}
			else{
				oldposition.set(characterComponent.dynamicBody.getPosition()); // 记录位置
				characterComponent.move(steeringOutput.linear.nor());
			}
		}
	}

	@Override
	public Vector2 getPosition() {
		return MapperTools.characterCM.get(entity).dynamicBody.getPosition();
	}

	@Override
	public float getOrientation() {
		return MapperTools.characterCM.get(entity).dynamicBody.getAngle();
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
		return MapperTools.attributesCM.get(entity).speed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
	}

	@Override
	public float getMaxLinearAcceleration() {
		return MapperTools.attributesCM.get(entity).speed;
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
		steeringBehavior = null;
	}
	
	/**
	 * 实现box2的判断躲避其他AI主体
	 * 
	 * @author D
	 * @date 2016年10月19日
	 */
	private static class Box2dRadiusProximity extends RadiusProximity<Vector2>{
		
		public Box2dRadiusProximity(PathfindingComponent owner, float radius) {
			super(owner, new Array<PathfindingComponent>(false, 8), radius);
		}
		
		/**
		 * 两个角色之间的距离 < 躲避距离 + 两个角色大小的半径。就要躲避了
		 */
		@Override
		public int findNeighbors(ProximityCallback<Vector2> callback) {
			
			agents.clear();
			
			// 寻找躲避对象
			Vector2 position = owner.getPosition();
			float findRadius = owner.getBoundingRadius() + radius;
			PhysicsManager.world.QueryAABB((QueryCallback)owner, 
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
