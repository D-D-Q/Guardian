package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.core.component.AnimationComponent;
import com.game.core.component.TextureComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.States;
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
		
		// 速度设置(修改每帧时间)
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
		if(attributesComponent != null){
			if(stateComponent.entityState.getCurrentState() == States.attack){
				animations[stateComponent.orientation.value].setFrameDuration(1/attributesComponent.ASPD/animations[stateComponent.orientation.value].getKeyFrames().length);
			}
		}
		
		// 根据时间获得当前帧，不循环
		animationComponent.stateTime += deltaTime;
		TextureRegion currentFrame = animations[stateComponent.orientation.value].getKeyFrame(animationComponent.stateTime, false); 
		
		if(animations[stateComponent.orientation.value].isAnimationFinished(animationComponent.stateTime)){
			animationComponent.stateTime = 0; // 可以通过它为0，判断动画播放结束
		}
		
		// 设置当前帧给纹理组件
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		textureComponent.textureRegion = currentFrame; 
	}
}
