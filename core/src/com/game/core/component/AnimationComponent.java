package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Interpolation.BounceOut;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.GAME;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 实体的动画集合组件
 * 
 * @author D
 * @date 2016年8月28日 上午10:16:43
 */
public class AnimationComponent implements Component, Poolable {
	
	public Entity entity;

	/**
	 * 根据状态和方向存储的动画数组
	 */
	public ObjectMap<States, Animation[]> animations;
	
	public Array<Label> subtitle;
	
	/**
	 * 当前动画已播放帧时间
	 * TODO 每次切换动画的时候其实应该重新置0，否则可能不从第一帧播放。暂不做
	 */
	public float stateTime;

	public AnimationComponent() {
		animations = new ObjectMap<>(8, 1);
		subtitle = new Array<>(false, 8);
	}
	
	/**
	 * 添加动画帧
	 * @param state
	 * @param animations
	 * @return
	 */
	public AnimationComponent addAnimation(States state, Animation[] animations){
		this.animations.put(state, animations);
        return this;
	}
	
	/**
	 * 添加字幕效果
	 * @param word
	 */
	public void addSubtitle(String word){
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		Label label = new Label(word, GAME.skin);
		label.setPosition(transformComponent.position.x - label.getWidth()/2, transformComponent.position.y + transformComponent.spriteHeight/2 - label.getHeight()/2);
		label.setOrigin(Align.center);
		label.setColor(Color.RED);
		
		label.setScale(0.1f);
		label.setFontScale(0.1f);
		RepeatAction repeatAction = Actions.forever(null);
		repeatAction.setAction(Actions.run(new Runnable() {
			public void run() {
				if (label.getScaleX() >= 1 && label.getScaleY() >= 1)
					repeatAction.finish();
				else
					label.setFontScale(label.getScaleX(), label.getScaleY());
			}
		}));
		label.addAction(
				Actions.parallel(Actions.moveBy(0, transformComponent.spriteHeight, 0.4f, Interpolation.bounceOut),
						Actions.scaleTo(1, 1, 0.4f, Interpolation.bounceOut), repeatAction));
		label.addAction(Actions.after(Actions.delay(0.2f, Actions.run(new Runnable() {
			public void run() {
				label.remove();
			}
		}))));
		
		subtitle.add(label);
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		entity = null;
		animations.clear();
		subtitle.clear();
		stateTime = 0;
	}
}
