package com.game.core.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.PhysicsComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.tools.MapperTools;

/**
 * box2d物理引擎管理器
 * 
 * @author D
 * @date 2016年10月12日
 */
public class PhysicsManager {
	
	/**
	 * 物理世界更新频率
	 */
	public static final float TIME_STEP = 1/60f;
	
	/**
	 * 物理世界速度和位置的计算。数值越大，效果越细腻，计算量也就越大，最高不要超过10 
	 */
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	/**
	 * box2d的物理世界
	 */
	public static World world = new World(new Vector2(0, 0), true); // 参数：无重力和休眠;
	
	/**
	 * 创建刚体
	 * 
	 * @param entity
	 * @return
	 */
	public static Body createRigidBody(Entity entity){
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		CircleShape circle = new CircleShape(); // 圆形
		circle.setRadius(physicsComponent.radius);
		
		Body body = create(physicsComponent.bodyType, circle, false, 
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		body.setUserData(entity); // 刚体携带实体
		
		return body;
	}
	
	/**
	 * 创建碰撞检测刚体
	 * 
	 * @param entity
	 * @return
	 */
	public static Body createCollision(Entity entity){
		
		CollisionComponent collisionComponent = MapperTools.collisionCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		CircleShape circle = new CircleShape(); // 圆形
		circle.setRadius(collisionComponent.radius);
		
		// 可移动的静态刚体
		Body body = create(BodyType.KinematicBody, circle, true, 
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		body.setUserData(entity); // 刚体携带实体
		
		return body;
	}
	
	/**
	 * 生成刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param isSensor true 不做物理反应，只是检测碰撞
	 * @param position 生成位置
	 * @return
	 */
	private static Body create(BodyType bodyType, Shape shape, boolean isSensor, Vector2 position){
		
		BodyDef bodyDef = new BodyDef(); // 刚体属性
		bodyDef.type = bodyType; // 刚体类型
		bodyDef.position.set(position); // 生成位置
		
		Body body = world.createBody(bodyDef); // 生成刚体
		
		FixtureDef fixtureDef = new FixtureDef(); // 物理属性
		fixtureDef.shape = shape; // 图形
		fixtureDef.density = 1f; // 密度
		fixtureDef.friction = 0f; // 摩擦力
		fixtureDef.restitution = 0f; // 弹力（还原力）
		fixtureDef.isSensor = isSensor; // true 不做物理反应，只是检测碰撞
		
		body.createFixture(fixtureDef); // 生成Fixture，直接设置了不用接受返回值
		
		return body;
	}
	
	/**
	 * 销毁刚体
	 * 
	 * @param body
	 */
	public static void disposeBody(Body body){
		
		for(Fixture fixture : body.getFixtureList())
			fixture.getShape().dispose(); // 图形需要销毁
		
		world.destroyBody(body);
	}
	
	/**
	 * 销毁
	 */
	public static void dispose(){
		
		while(!world.isLocked()){ // 物理世界锁的时候不能操作。step的时候会
			world.dispose();
			break;
		}
	}
}
