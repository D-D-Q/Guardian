package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 纹理组件
 * 用于绘制显示精灵
 * 
 * @author D
 * @date 2016年8月28日 上午11:35:57
 */
public class TextureComponent implements Component, Poolable {
	
	/**
	 * 体力条
	 */
	public ProgressBar vitBar;

	public TextureRegion textureRegion = null;
	
	public TextureComponent() {
		
		Pixmap pixmap1 = new Pixmap(1, 3, Format.RGB888);
		pixmap1.setColor(Color.WHITE);
		pixmap1.fill();
		
		Pixmap pixmap2 = new Pixmap(1, 3, Format.RGB888);
		pixmap2.setColor(Color.RED);
		pixmap2.fill();
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
		progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		
		vitBar = new ProgressBar(0, 0, 1, false, progressBarStyle);
		vitBar.setAnimateDuration(0.1f);
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		textureRegion = null;
		vitBar.remove();
		vitBar.setVisible(false);
	}

}
