package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.guardian.game.GuardianGame;
import com.guardian.game.components.ItemComponent;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.ui.CharacterUI;

/**
 * 物品栏系统
 * 
 * @author D
 * @date 2016年9月9日 下午9:40:06
 */
public class ItemsSystem extends EntitySystem{
	
	public final GuardianGame game;
	
	private CharacterUI ui;
	
	public ItemsSystem(GuardianGame guardianGame) {
		this.game = guardianGame;
	}
	
	public void setUi(CharacterUI ui) {
		this.ui = ui;
	}

	/**
	 * 物品
	 */
	private Entity[] entitys = new Entity[18]; //TODO 物品栏数量写死了
	
	/**
	 * 当前第一个空的物品位置
	 */
	private int index = 0;

	/**
	 * 物品放入物品栏
	 */
	public int addItem(Entity entity) {
		if(index == -1)
			return -1; //TODO 物品栏没位置了
		
		int i = index;
		entitys[index] = entity;
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		ItemComponent itemComponent = MapperTools.itemCM.get(entity);
		Image image = new Image(textureComponent.textureRegion);
		image.setName(itemComponent.name);
		ui.addItem(index, image);
		
		Log.info(this, "添加物品:" + itemComponent.name);
		
		for(++index; index < entitys.length; ++index) // 从添加位置下一个开始遍历到最近一个空物品栏
			if(entitys[index] == null)
				return i;
		index = -1; // 没位置了
		
		return i;
	}
	
	/**
	 * 物品放入物品栏
	 */
	public int addItem(int i, Entity entity) {
		if(index == -1)
			return -1; //TODO 物品栏没位置了
		
		if(entitys[i] != null) // 有物品不能覆盖
			return addItem(entity);
		
		entitys[i] = entity;
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		ItemComponent itemComponent = MapperTools.itemCM.get(entity);
		Image image = new Image(textureComponent.textureRegion);
		image.setName(itemComponent.name);
		ui.addItem(i, image);
		
		Log.info(this, "添加物品:" + itemComponent.name);
		
		if(i == index){ // 如果正好是当前空位
			for(++index; index < entitys.length; ++index) // 从添加位置下一个开始遍历到最近一个空物品栏
				if(entitys[index] == null)
					return i;
			index = -1; // 没位置了
		}
		
		return i;
	}

	/**
	 * 物品移除物品栏
	 */
	public void removeItem(Entity entity) {
		
		for(int i=0; i < entitys.length; ++i){
			if(entitys[i] == entity){
				entitys[i] = null;
				if(i < index) // 如果移除的位置比当前空物品栏靠前
					index = i;
				break;
			}
		}
	}
	
	/**
	 * 物品移除物品栏
	 */
	public Entity removeItem(int i) {
		if(i >= entitys.length)
			return null;
		
		Entity entity = entitys[i];
		entitys[i] = null;
		
		if(i < index) // 如果移除的位置比当前空物品栏靠前
			index = i;
		
		Log.info(this, "移除物品:" + i);
		
		return entity;
	}
	
	/**
	 * 物品交互位置
	 */
	public void exchangeEntity(int i, int j) {
		
		if(i >= entitys.length || j >= entitys.length)
			return;
		
		Entity entity = entitys[i];
		entitys[i] = entitys[j];
		entitys[j] = entity;
		
		if(entitys[i] == null && i < index)
			index = i;
		if(entitys[j] == null && j < index)
			index = j;
		
		Log.info(this, "物品交换" + i + "," + j);
	}
}
