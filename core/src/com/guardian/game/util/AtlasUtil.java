package com.guardian.game.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.guardian.game.components.StateComponent.Orientation;

public class AtlasUtil {

	/**
	 * 获得8个方向的动画帧，帧数组要按Orientation定义的顺序组合
	 * 
	 * @param frames 帧数组
	 * @param index 开始的数组索引
	 * @param length 帧数
	 * @return
	 */
	public static Animation[] stateAnimation(Array<Sprite> frames, int index, int length){
		
		float zs = 0.15f; // 动画一帧时间
		
		Animation[] animation = new Animation[8];
		for(Orientation orientation : Orientation.values()){
			
			Sprite[] sprites = new Sprite[length];
			
			if(orientation.isFlip){ // 翻转得来的帧
				TextureRegion[] keyFrames = animation[orientation.flipValue].getKeyFrames(); // 获得原帧
				for(int i = 0; i < keyFrames.length; ++i){
					
					if(keyFrames[i] instanceof AtlasSprite)
						 // TODO AtlasSprite(AtlasSprite)的构造方法有问题，不会复制AtlasRegion，而是跟原对象使用一个，造成翻转互相影响。所以使用AtlasSprite(AtlasRegion)
						sprites[i] = new AtlasSprite(((AtlasSprite)keyFrames[i]).getAtlasRegion());
					else
						sprites[i] = new Sprite((Sprite)keyFrames[i]);
					
					sprites[i].flip(true, false); // 翻转
				}
			}
			else{
				System.arraycopy(frames.items, index, sprites, 0, length);
			}
			animation[orientation.value] = new Animation(zs, sprites);
			index += length;
		}
		return animation;
	}
}
