package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.tools.MapperTools;

/**
 * 寻路组件
 * 	RadiusProximity 实现躲避其他AI主体
 *  QueryCallback 获取躲避的对象的接口
 *  Steerable AI主体接口
 * 
 * @author D
 * @date 2016年10月18日
 */
public class PathfindingComponent extends RadiusProximity<Vector2> implements QueryCallback, Component, Steerable<Vector2>, Poolable {
	
	public PathfindingComponent() {
		super(null, new Array<PathfindingComponent>(false, 8), 0);
		setOwner(this);
	}
	
	@Override
	public int findNeighbors(com.badlogic.gdx.ai.steer.Proximity.ProximityCallback<Vector2> callback) {
		
		agents.clear();
		
		// 寻找躲避对象
		Vector2 position = owner.getPosition();
		PhysicsManager.world.QueryAABB(this, position.x - radius, position.y - radius, position.x + radius, position.y + radius);
		
		return super.findNeighbors(callback);
	}
	
	/** 
	 * 添加躲避对象
	 */
	@Override
	public boolean reportFixture(Fixture fixture) {
		
		if(fixture.isSensor() || fixture.getBody().getType() != BodyType.DynamicBody )
			return true;
		
		PathfindingComponent pathfindingComponent = MapperTools.pathfindingCM.get((Entity)fixture.getBody().getUserData());
		
//		agents.add(pathfindingComponent); // 直接添加泛型编译不通过
		((Array<PathfindingComponent>)agents).add(pathfindingComponent);
		
		return true;
	}
	

	@Override
	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOrientation(float orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location<Vector2> newLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxLinearSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxLinearAcceleration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxAngularSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxAngularAcceleration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2 getLinearVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngularVelocity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTagged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTagged(boolean tagged) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void reset() {
	}
}
