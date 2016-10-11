package com.guardian.game.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;
import com.guardian.game.components.StateComponent.Orientation;

public class AtlasUtil {

	
	/**
	 * 翻转所有精灵，返回新的精灵数组，不改变原来的数组中的精灵
	 * 
	 * @param array
	 * @return
	 */
	public static Sprite[] flipAll(Sprite[] prites){
		
		Sprite[] spritesTemp = new Sprite[prites.length];
		
		int i = 0;
		for(Sprite sprite : prites){
			
			Sprite spriteTemp;
			if(sprite instanceof AtlasSprite)
				spriteTemp = new AtlasSprite((AtlasSprite)sprite);
			else
				spriteTemp = new Sprite(sprite);
			spriteTemp.flip(true, false);
			spritesTemp[i++] = spriteTemp;
		}
		
		return spritesTemp;
	}
	
	/**
	 * 获得8个方向的动画帧，帧数组要按Direction定义的顺序组合
	 * 
	 * @param frames 帧数组
	 * @param index 开始的数组索引
	 * @param length 帧数
	 * @return
	 */
	public static Animation[] stateAnimation(Array<Sprite> frames, int index, int length){
		
		float zs = 0.15f; // 动画一帧时间
		
		Animation[] animation = new Animation[8];
		for(Orientation direction : Orientation.values()){
			if(direction == Orientation.d9) // 只遍历前5个方向就可以了。到9方位 就是都完成赋值了
				break;
			
			Sprite[] sprites = new Sprite[length];
			System.arraycopy(frames.items, index, sprites, 0, length);
			animation[direction.value] = new Animation(zs, sprites);
			if(direction.reverse != 0){
				animation[direction.reverse] = new Animation(zs, AtlasUtil.flipAll(sprites));
			}
			index += length;
		}
		return animation;
	}
}
