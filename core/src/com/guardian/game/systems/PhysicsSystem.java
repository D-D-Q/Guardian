package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.core.component.ScriptComponent;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.components.CharacterComponent;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * 物理系统，碰撞检测系统
 * 
 * @author Administrator
 * @date 2016年10月12日
 */
public class PhysicsSystem extends IteratingSystem implements ContactListener{
	
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
		
		// TODO 移动速度 要从角色获取
		Vector2 speed = Vector2.Zero;
		
		// 获得刚体位置，更新精灵位置
		CharacterComponent physicsComponent = MapperTools.characterCM.get(entity);
		if(physicsComponent != null){
			
			Vector2 position = physicsComponent.dynamicBody.getPosition();
			transformComponent.position.x = position.x;
			transformComponent.position.y = position.y;
			
			// 更新静态刚体位置
			if(physicsComponent.staticBody != null)
				physicsComponent.staticBody.setTransform(position.x, position.y, physicsComponent.staticBody.getAngle());
			
			speed = physicsComponent.dynamicBody.getLinearVelocity();
		}
		
		// 更新碰撞检测的位置。即以相同速度跟随实体移动
		// TODO 应该放在物理引擎step之前，否则collision会比实体慢一步少更新一次step
		if(speed != Vector2.Zero){
			CollisionComponent collisionComponent = MapperTools.collisionCM.get(entity);
			if(collisionComponent != null)
				collisionComponent.rigidBody.setLinearVelocity(speed);
		}
	}
	
	/**
	 * 进入碰撞
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void beginContact(Contact contact) {
		
		ContactEntit ContactEntit = getEntityAndTarget(contact);
		
		// 没有碰撞检测刚体参与碰撞。普通刚体碰撞，不用给实体转发碰撞事件
		if(ContactEntit == null)
			return;
		
		// 转发事件
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(ContactEntit.entity);
		if(scriptComponent != null)
			scriptComponent.script.beginContact(contact, ContactEntit.target);
	}

	/**
	 * 离开碰撞
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void endContact(Contact contact) {
		
		ContactEntit ContactEntit = getEntityAndTarget(contact);
		
		// 没有碰撞检测刚体参与碰撞。普通刚体碰撞，不用给实体转发碰撞事件
		if(ContactEntit == null)
			return;
		
		// 转发事件
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(ContactEntit.entity);
		if(scriptComponent != null)
			scriptComponent.script.endContact(contact, ContactEntit.target);
	}

	/**
	 * 新碰撞点
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	/**
	 * 碰撞点产生力
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	
	/**
	 * 保存检测碰撞的实体和碰撞的目标实体
	 * 
	 * @author D
	 * @date 2016年10月14日
	 */
	private class ContactEntit{
		
		/**
		 * 碰撞检测刚体的实体
		 */
		public Entity entity;
		
		/**
		 * 碰撞的目标实体
		 */
		public Entity target;
		
		public ContactEntit(Entity entity, Entity target) {
			this.entity = entity;
			this.target = target;
		}
	}
	
	/**
	 * 获得碰撞检测实体和碰撞目标
	 * 
	 * @param contact 碰撞类
	 * @return null 没有碰撞检测刚体参与碰撞或碰撞自己
	 */
	private ContactEntit getEntityAndTarget(Contact contact){
		
		ContactEntit contactEntit = null;
		
		// 碰撞检测刚体的isSensor是true。另一个就是碰撞的目标
		if(contact.getFixtureA().isSensor())
			contactEntit = new ContactEntit((Entity) contact.getFixtureA().getBody().getUserData(), (Entity) contact.getFixtureB().getBody().getUserData());
		else if(contact.getFixtureB().isSensor())
			contactEntit = new ContactEntit((Entity) contact.getFixtureB().getBody().getUserData(), (Entity) contact.getFixtureA().getBody().getUserData());
		
		if(contactEntit == null || contactEntit.entity == contactEntit.target)
			return null;
		
		return contactEntit;
	}
}
