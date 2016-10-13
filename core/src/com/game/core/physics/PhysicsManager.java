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
import com.badlogic.gdx.utils.Array;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.PhysicsComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.tools.MapperTools;

/**
 * box2d物理引擎管理器
 * 非线程安全
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
	 * 保证同一个精灵身上的刚体没有碰撞，非线程安全
	 * groupIndex 取值范围[-1, -32768]
	 */
	private static short maxGroupIndex = Short.MIN_VALUE; // -32768
	
	/**
	 * 防止groupIndex重复
	 */
	private static Array<Short> groupIndexArray = new Array<Short>(false, 100);
	
	/**
	 * 添加精灵刚体
	 * 保证精灵和精灵有碰撞检测，但不被力推动
	 * 保证精灵和其他刚体的正常物理效果
	 * 
	 * @param entity
	 * @return
	 */
	public static void addCharacterRigidBody(Entity entity){
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		// 获得组号，如果已占用继续获取
		do{
			maxGroupIndex = (short) ((maxGroupIndex%Math.abs(Short.MIN_VALUE)) - 1);
		}while(groupIndexArray.contains(maxGroupIndex, true));
		
		short groupIndex_temp = maxGroupIndex;
		
		CircleShape circle = new CircleShape(); // 圆形
		circle.setRadius(physicsComponent.radius);
		
		Body dynamicBody = create(BodyType.DynamicBody, circle, groupIndex_temp, (short)0x0010, (short)0x1101, // (0x1000)是其他刚体
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		dynamicBody.setUserData(entity); // 刚体携带实体
		
		Body staticBody = create(BodyType.StaticBody, circle, groupIndex_temp, (short)0x0100, (short)0x0010,
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		staticBody.setUserData(entity); // 刚体携带实体
		
		physicsComponent.dynamicBody = dynamicBody;
		physicsComponent.staticBody = staticBody;
		
		circle.dispose();
	}
	
	/**
	 * 添加碰撞检测
	 * categoryBits是默认值0x0001
	 * 
	 * @param entity
	 * @return
	 */
	public static void addCollision(Entity entity){
		
		CollisionComponent collisionComponent = MapperTools.collisionCM.get(entity);
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		CircleShape circle = new CircleShape(); // 圆形
		circle.setRadius(collisionComponent.radius);
		
		Body body = create(BodyType.KinematicBody, circle,
				new Vector2(transformComponent.position.x, transformComponent.position.y));
		body.setUserData(entity); // 刚体携带实体
		
		collisionComponent.rigidBody = body;
		
		circle.dispose();
	}
	
	/**
	 * 生成正常刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param categoryBits isSensor是false时生效。组号，必须是2的指数位值。1 2 4 8...
	 * @param maskBits isSensor是false时生效。可以发生碰撞的组号，可以多个。2|4|8...。-1表示所有都碰撞
	 * @param position 生成位置
	 * @return
	 */
	private static Body create(BodyType bodyType, Shape shape, short groupIndex, short categoryBits, short maskBits, Vector2 position){
		return create(bodyType, shape, false, groupIndex, categoryBits, maskBits, position);
	}
	
	/**
	 * 生成传感器刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param position 生成位置
	 * @return
	 */
	private static Body create(BodyType bodyType, Shape shape, Vector2 position){
		return create(bodyType, shape, true, (short)0, (short)1, (short)-1, position);
	}
	
	/**
	 * 生成刚体
	 * 
	 * @param bodyType 刚体类型
	 * @param shape 图形
	 * @param isSensor true 不做物理反应，只是检测碰撞
	 * @param categoryBits isSensor是false时生效。组号，必须是2的指数位值。1 2 4 8...
	 * @param maskBits isSensor是false时生效。可以发生碰撞的组号，可以多个。2|4|8...。-1表示所有都碰撞
	 * @param position 生成位置
	 * @return
	 */
	private static Body create(BodyType bodyType, Shape shape, boolean isSensor, short groupIndex, short categoryBits, short maskBits, Vector2 position){
		
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
		
		// 碰撞过滤。isSensor是true的刚体一定要检测碰撞，不设置过滤
		if(!isSensor){
			fixtureDef.filter.groupIndex = groupIndex; // 同组的正数的碰撞,负数不碰撞。0或不同组使用categoryBits和maskBits判断
			fixtureDef.filter.categoryBits = categoryBits; // 自己的值，2的指数位值。1 2 4 8...
			fixtureDef.filter.maskBits = maskBits; // 可以发生碰撞的值, 可以多个。2&4&8...
		}
		
		body.createFixture(fixtureDef); // 生成Fixture，直接设置了不用接受返回值
		
		return body;
	}
	
	/**
	 * 销毁刚体
	 * 
	 * @param body
	 */
	public static void disposeBody(Body body){
		
		// 销毁前 移出已占用组号
		Array<Fixture> fixtureList = body.getFixtureList();
		if(fixtureList != null && fixtureList.size != 0)
			groupIndexArray.removeValue(fixtureList.get(0).getFilterData().groupIndex, true);
		
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
