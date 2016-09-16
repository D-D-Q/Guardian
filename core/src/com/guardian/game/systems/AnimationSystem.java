package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.guardian.game.components.AnimationComponent;
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

	private float stateTime;
	
	public AnimationSystem() {
		// TODO 系统调用优先级写死了
		super(FamilyTools.animationF, 0);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Log.debug(this, "processEntity");
		
		stateTime += deltaTime;
		
		AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
		Animation animation = animationComponent.getAnimation(MapperTools.stateCM.get(entity).state);
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // 根据时间获得当前帧，循环
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		textureComponent.textureRegion = currentFrame; // 设置当前帧给纹理组件
	}
}
