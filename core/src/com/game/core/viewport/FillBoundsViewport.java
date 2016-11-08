package com.game.core.viewport;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * 可以设置大小和位置的FillViewport
 * libgdx提供的都是以全屏为目的
 * 
 * @author D
 * @date 2016年11月8日
 */
public class FillBoundsViewport extends FillViewport {

	public FillBoundsViewport(float worldWidth, float worldHeight) {
		super(worldWidth, worldHeight);
	}
	
	public FillBoundsViewport(float worldWidth, float worldHeight, Camera camera) {
		super(worldWidth, worldHeight, camera);
	}

	// TODO 还未做
//	GAME.UIViewport.setScreenBounds(100, 100, 500, 500);
//	GAME.UIViewport.apply();
}
