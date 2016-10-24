package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CollisionComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.ScriptComponent;
import com.game.core.component.TransformComponent;
import com.game.core.manager.PhysicsManager;
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
	
	private ContactEntitPools contactEntitPools;

	public PhysicsSystem(int priority) {
		super(FamilyTools.physicsF, priority);
		
		PhysicsManager.world.setContactListener(this); // 碰撞监听
		
		contactEntitPools = new ContactEntitPools(64);
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
	}
	
	/**
	 * 跟随刚体更新位置
	 */
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		// 获得刚体位置，更新精灵位置
		CharacterComponent physicsComponent = MapperTools.characterCM.get(entity);
		if(physicsComponent != null){
			
			Vector2 position = physicsComponent.dynamicBody.getPosition();
			transformComponent.position.x = position.x;
			transformComponent.position.y = position.y;
			
			// 更新静态刚体位置
			if(physicsComponent.staticBody != null)
				physicsComponent.staticBody.setTransform(position.x, position.y, physicsComponent.staticBody.getAngle()); // setTransform更新不会触发碰撞事件
			
			// 更新攻击距离碰撞检测的位置。下一帧就追上精灵
			CombatComponent combatComponent = MapperTools.combatCM.get(entity);
			if(combatComponent != null){
				
				// 获得当前位置和目标位置的距离，当作速度。
				Vector2 destination = new Vector2(position);
				destination.sub(combatComponent.rangeBody.getPosition()); 
				destination.scl(1/PhysicsManager.TIME_STEP); // 速度单位是秒，要一帧追上，速度需要乘以帧数(60)倍
				
				combatComponent.rangeBody.setLinearVelocity(destination);
				combatComponent.distanceBody.setLinearVelocity(destination);
			}
			
			// 更新碰撞检测的位置。下一帧就追上精灵
			CollisionComponent collisionComponent = MapperTools.collisionCM.get(entity);
			if(collisionComponent != null){
				
				// 获得当前位置和目标位置的距离，当作速度。
				Vector2 destination = new Vector2(position);
				destination.sub(collisionComponent.rigidBody.getPosition());
				destination.scl(1/PhysicsManager.TIME_STEP); // 速度单位是秒，要一帧追上，速度需要乘以帧数(60)倍
				
				collisionComponent.rigidBody.setLinearVelocity(destination);
			}
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
		if(scriptComponent != null){
			
			CombatComponent combatComponent = MapperTools.combatCM.get(ContactEntit.entity);
			if(combatComponent != null && combatComponent.rangeBody == ContactEntit.entityFixture.getBody()){ 
				if(scriptComponent.script.enterATKRange(contact, ContactEntit.target)) // 攻击范围检测到
					combatComponent.enterATKRange(contact, ContactEntit.target);
			}
			else if(combatComponent != null && combatComponent.distanceBody == ContactEntit.entityFixture.getBody()){
				if(scriptComponent.script.enterATKDistance(contact, ContactEntit.target)) // 攻击范围检测到
					combatComponent.enterATKDistance(contact, ContactEntit.target);
			}
			else{
				scriptComponent.script.beginContact(contact, ContactEntit.target); // 普通碰撞检测事件
			}
		}
		
		contactEntitPools.free(ContactEntit);
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
		if(scriptComponent != null){
			
			CombatComponent combatComponent = MapperTools.combatCM.get(ContactEntit.entity);
			if(combatComponent != null && combatComponent.rangeBody == ContactEntit.entityFixture.getBody()){ 
				if(scriptComponent.script.leaveATKRange(contact, ContactEntit.target)) // 攻击范围检测到
					combatComponent.leaveATKRange(contact, ContactEntit.target);
			}
			else if(combatComponent != null && combatComponent.distanceBody == ContactEntit.entityFixture.getBody()){
				if(scriptComponent.script.leaveATKDistance(contact, ContactEntit.target)) // 攻击距离检测到
					combatComponent.leaveATKDistance(contact, ContactEntit.target);
			}
			else{
				scriptComponent.script.endContact(contact, ContactEntit.target); // 普通碰撞检测事件
			}
		}
		
		contactEntitPools.free(ContactEntit);
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
	 * 获得碰撞检测实体和碰撞目标
	 * 
	 * @param contact 碰撞类
	 * @return null 没有碰撞检测刚体参与碰撞或碰撞自己
	 */
	private ContactEntit getEntityAndTarget(Contact contact){
		
		ContactEntit contactEntit = null;
		
		// 碰撞检测刚体的isSensor是true。另一个就是碰撞的目标
		if(contact.getFixtureA().isSensor()){
			contactEntit = contactEntitPools.obtain();
			contactEntit.setEntityFixture(contact.getFixtureA());
			contactEntit.setTargetFixture(contact.getFixtureB());
		}
		else if(contact.getFixtureB().isSensor()){
			contactEntit = contactEntitPools.obtain();
			contactEntit.setEntityFixture(contact.getFixtureB());
			contactEntit.setTargetFixture(contact.getFixtureA());
		}
		if(contactEntit == null)
			return null;
		if(contactEntit.entity == contactEntit.target){
			contactEntitPools.free(contactEntit);
			return null;
		}
		
		return contactEntit;
	}
	
	/**
	 * 保存检测碰撞的实体和碰撞的目标实体
	 * 
	 * @author D
	 * @date 2016年10月14日
	 */
	private class ContactEntit implements Poolable{
		
		/**
		 * 碰撞检测刚体的实体
		 */
		public Entity entity;
		public Fixture entityFixture;
		
		/**
		 * 碰撞的目标实体
		 */
		public Entity target;
//		public Fixture targetFixture;
		
		public void setEntityFixture(Fixture entityFixture) {
			this.entityFixture = entityFixture;
			this.entity = (Entity) entityFixture.getBody().getUserData();
		}

		public void setTargetFixture(Fixture targetFixture) {
//			this.targetFixture = targetFixture;
			this.target = (Entity) targetFixture.getBody().getUserData();
		}

		@Override
		public void reset() {
			entity = null;
			entityFixture = null;
			target = null;
//			targetFixture = null;
		}
	}
	
	/**
	 * 碰撞数据频繁，做池化
	 * 
	 * @author D
	 * @date 2016年10月24日
	 */
	private class ContactEntitPools extends Pool<ContactEntit>  {

		public ContactEntitPools(int initialCapacity) {
			super(initialCapacity);
		}
		
		@Override
		protected ContactEntit newObject() {
			return new ContactEntit();
		}
	}
}
