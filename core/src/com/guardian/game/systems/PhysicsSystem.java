package com.guardian.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.core.physics.PhysicsManager;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.PhysicsComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * 物理系统，碰撞检测系统
 * 
 * @author Administrator
 * @date 2016年10月12日
 */
public class PhysicsSystem extends IteratingSystem implements EntityListener, ContactListener{
	
	/**
	 * 更新物理引擎的时间量
	 */
	private float accumulator = 0;

	/**
	 * 物理引擎debug
	 */
	public Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public PhysicsSystem(int priority) {
		super(FamilyTools.physicsF, priority);
		
		PhysicsManager.world.setContactListener(this); // 碰撞监听
		if(GameConfig.physicsdebug)
			debugRenderer = new Box2DDebugRenderer();
	}
	
	/**
	 * 比父类额外添加监听含有物理组件的实体添加到实体组件引擎，好做出初始化
	 * @param engine
	 */
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(FamilyTools.physicsF, this);
	}

	@Override
	public void removedFromEngine (Engine engine) {
		super.removedFromEngine(engine);
		engine.removeEntityListener(this);
	}

	/**
	 * 给包含物理组件的实体创建刚体
	 * 
	 * @param entity
	 */
	@Override
	public void entityAdded(Entity entity) {
		
		// 添加物理刚体
		if(MapperTools.physicsCM.get(entity) != null)
			PhysicsManager.addCharacterRigidBody(entity);
		
		// 添加碰撞检测
		if(MapperTools.collisionCM.get(entity) != null)
			PhysicsManager.addCollision(entity);
	}

	@Override
	public void entityRemoved(Entity entity) {
	}

	/**
	 * 更新物理世界
	 * 物理引擎需要固定时间更新，不能跟随帧数，因为帧数不稳定
	 */
	@Override
	public void update(float deltaTime) {
		
		float frameTime = Math.min(deltaTime, 0.25f); // 最大帧间隔时间0.25，防止死亡螺旋（spiral of death）
	    accumulator += frameTime;
	    
	    while (accumulator >= PhysicsManager.TIME_STEP) {
	    	PhysicsManager.world.step(PhysicsManager.TIME_STEP, PhysicsManager.VELOCITY_ITERATIONS, PhysicsManager.POSITION_ITERATIONS); // 更新
	    	accumulator -= PhysicsManager.TIME_STEP;
	    }
	    
	    super.update(deltaTime); // 更新精灵实体
		
	    if(GameConfig.physicsdebug){
	    	CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
	    	cameraComponent.camera.update();
	    	debugRenderer.render(PhysicsManager.world, cameraComponent.camera.combined);
	    }
	}
	
	/**
	 * 跟随刚体更新位置
	 */
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		// 获得刚体位置，更新精灵位置
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		if(physicsComponent != null){
			Vector2 position = physicsComponent.dynamicBody.getPosition();
			transformComponent.position.x = position.x;
			transformComponent.position.y = position.y;
			
			// 更新静态刚体位置
			if(physicsComponent.staticBody != null){
				physicsComponent.staticBody.setTransform(position.x, position.y, 
						physicsComponent.staticBody.getAngle());
			}
		}
		
		// 获得精灵位置，更新碰撞检测位置
		CollisionComponent collisionComponent = MapperTools.collisionCM.get(entity);
		if(collisionComponent != null)
			collisionComponent.rigidBody.setTransform(transformComponent.position.x, transformComponent.position.y, 
					collisionComponent.rigidBody.getAngle());
	}
	
	@Override
	public void beginContact(Contact contact) {
		Log.info(this, "beginContact");
	}

	@Override
	public void endContact(Contact contact) {
		Log.info(this, "endContact");
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
//		Log.info(this, "preSolve");
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
//		Log.info(this, "postSolve");
	}
}
