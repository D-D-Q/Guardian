package com.guardian.game.entity.dao;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.guardian.game.GuardianGame;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.AnimationComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.components.TransformComponent;

/**
 * 生产指定实体
 * 
 * @author D
 * @date 2016年9月16日 上午8:00:39
 */
public class EntityDao {

	private GuardianGame game;
	
	public EntityDao(GuardianGame game) {
		this.game = game;
	}
	
	public Entity hero(float positionX, float positionY){
		
		Entity entity = game.engine.createEntity();
		
		TransformComponent transformComponent = game.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(positionX, positionY, 100f);
		transformComponent.width = 75;
		transformComponent.height = 75;
		transformComponent.positionOrigin.set(50, 0);
		
		StateComponent stateComponent = game.engine.createComponent(StateComponent.class);
		stateComponent.state = 0;
		TextureComponent textureComponent = game.engine.createComponent(TextureComponent.class);
		AnimationComponent animationComponent = game.engine.createComponent(AnimationComponent.class);
		
		// 正面站立动画
		animationComponent.addAnimation(0, new Animation(0.2f, game.assets.getFrames(GameScreenAssets.hero, 100, 105))); //TODO 动画写死了
		
		AttributesComponent attributesComponent = game.engine.createComponent(AttributesComponent.class);
		attributesComponent.Lv = 1;
		attributesComponent.ATK = 100;
		attributesComponent.DEF = 100;
		attributesComponent.HIT = 100;
		attributesComponent.AVD = 100;
		attributesComponent.VIT = 100;
		
		entity.add(transformComponent);
		entity.add(stateComponent);
		entity.add(textureComponent);
		entity.add(animationComponent);
		entity.add(attributesComponent);
		
		return entity;
	}
}
