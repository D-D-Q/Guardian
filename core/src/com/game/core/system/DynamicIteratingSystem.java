package com.game.core.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

/**
 * 会动态监测新添加Entity的遍历系统
 * 因为IteratingSystem的Entity只在创建的时候获取一边，后添加没有。
 * 好像有，这个类每必要了
 * 
 * @author D
 * @date 2016年10月12日
 */
public abstract class DynamicIteratingSystem extends EntitySystem implements EntityListener{
	
	private Family family;
	private ImmutableArray<Entity> entities;
	private Array<Entity> sortedEntities;

	public DynamicIteratingSystem(Family family) {
		this(family, 0);
	}
	
	public DynamicIteratingSystem(Family family, int priority) {
		super(priority);
		this.family = family;
		sortedEntities = new Array<Entity>(false, 16);
		entities = new ImmutableArray<Entity>(sortedEntities);
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		
		ImmutableArray<Entity> newEntities = engine.getEntitiesFor(family);
		sortedEntities.clear();
		if (newEntities.size() > 0) {
			for (int i = 0; i < newEntities.size(); ++i) {
				sortedEntities.add(newEntities.get(i));
			}
		}
		engine.addEntityListener(family, this);
	}

	@Override
	public void removedFromEngine (Engine engine) {
		engine.removeEntityListener(this);
		sortedEntities.clear();
	}

	@Override
	public void entityAdded(Entity entity) {
		sortedEntities.add(entity);
	}

	@Override
	public void entityRemoved(Entity entity) {
		sortedEntities.removeValue(entity, true);
	}

	@Override
	public void update (float deltaTime) {
		for (int i = 0; i < sortedEntities.size; ++i) {
			processEntity(sortedEntities.get(i), deltaTime);
		}
	}
	
	public ImmutableArray<Entity> getEntities () {
		return entities;
	}

	public Family getFamily () {
		return family;
	}

	protected abstract void processEntity (Entity entity, float deltaTime);
}
