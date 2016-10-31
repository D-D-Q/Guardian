package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CameraComponent;
import com.game.core.component.TextureComponent;
import com.guardian.game.GAME;
import com.guardian.game.GuardianGame;
import com.guardian.game.components.StateComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * 动画系统
 * 决定精灵的当前帧
 * 
 * @author D
 * @date 2016年8月28日 上午10:52:12
 */
public class AnimationSystem extends IteratingSystem {
	
	/**
	 * @param priority 系统调用优先级
	 */
	public AnimationSystem(int priority) {
		super(FamilyTools.animationF, priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
		
		// 根据状态获得动画
		Animation[] animations = animationComponent.animations.get(stateComponent.entityState.getCurrentState());
		if(animations == null || animations.length < stateComponent.orientation.value)
			return;
		
		// 根据时间获得当前帧，循环
		animationComponent.stateTime += deltaTime;
		TextureRegion currentFrame = animations[stateComponent.orientation.value].getKeyFrame(animationComponent.stateTime, true); 
		
		// 设置当前帧给纹理组件
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		textureComponent.textureRegion = currentFrame; 
	}
}
