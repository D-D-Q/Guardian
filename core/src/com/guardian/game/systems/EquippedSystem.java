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
 * 装备系统
 * 
 * @author D
 * @date 2016年9月10日 下午4:19:53
 */
public class EquippedSystem extends EntitySystem {

	public final GuardianGame game;
	
	private CharacterUI ui;
	
	public EquippedSystem(GuardianGame guardianGame) {
		this.game = guardianGame;
	}
	
	public void setUi(CharacterUI ui) {
		this.ui = ui;
	}

	/**
	 * 装备
	 */
	private Entity[] entitys = new Entity[5]; //TODO 装备栏数量写死了
	
	/**
	 * 穿装备
	 */
	public Entity equippedItem(Entity entity) {
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		ItemComponent itemComponent = MapperTools.itemCM.get(entity);
		
		Entity oldEntity = entitys[itemComponent.subType];
		entitys[itemComponent.subType] = entity;
		
		Image image = new Image(textureComponent.textureRegion);
		image.setName(itemComponent.name);
		ui.equippedItem(itemComponent.subType, image);
		
		Log.info(this, "装备物品:" + itemComponent.name);
		
		return oldEntity;
	}
	
	/**
	 * 卸下装备
	 */
	public Entity unwieldItem(int i) {
		
		if(i >= entitys.length)
			return null;
		
		Entity entity = entitys[i];
		entitys[i] = null;
		
		Log.info(this, "卸下装备物品:" + i);
		
		return entity;
	}
}
