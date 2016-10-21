package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 纹理组件
 * 用于绘制显示精灵
 * 
 * @author D
 * @date 2016年8月28日 上午11:35:57
 */
public class TextureComponent implements Component, Poolable {

	public TextureRegion textureRegion = null;
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		textureRegion = null;
	}

}
