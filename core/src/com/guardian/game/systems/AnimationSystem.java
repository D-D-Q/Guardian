package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.guardian.game.components.AnimationComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.logs.Log;
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
		Log.debug(this, "processEntity");
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
		
		// 根据状态获得动画
		Animation[] animations = animationComponent.animations.get(stateComponent.entityState.getCurrentState());
		if(animations == null || animations.length < stateComponent.orientation.value)
			return;
		
		// 根据时间获得当前帧，循环
		animationComponent.stateTime += deltaTime;
		TextureRegion currentFrame = animations[stateComponent.orientation.value].getKeyFrame(animationComponent.stateTime, true); 
		
		// 判断翻转方向的帧
		if(stateComponent.orientation.isFlip && !animationComponent.flipFrame.containsKey(currentFrame)){ 
			animationComponent.flipFrame.put(currentFrame, true);
			currentFrame.flip(true, false);
		}
		else if(!stateComponent.orientation.isFlip && animationComponent.flipFrame.containsKey(currentFrame)){
			animationComponent.flipFrame.remove(currentFrame);
			currentFrame.flip(true, false);
		}
		
		// 设置当前帧给纹理组件
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		textureComponent.textureRegion = currentFrame; 
	}
}
