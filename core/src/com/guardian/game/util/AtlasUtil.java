package com.guardian.game.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
			if(orientation.isFlip) // 非翻转得来的帧
				break;
			Sprite[] sprites = new Sprite[length];
			System.arraycopy(frames.items, index, sprites, 0, length);
			animation[orientation.value] = new Animation(zs, sprites);
			index += length;
		}
		return animation;
	}
}
