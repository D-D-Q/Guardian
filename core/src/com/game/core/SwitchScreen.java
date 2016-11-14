package com.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.logs.Log;

/**
 * 屏幕切换用，中间屏
 * 
 * @author D
 * @date 2016年9月2日 上午6:02:20
 */
public class SwitchScreen extends ScreenAdapter {
	
	private Game game;
	private Class<? extends Screen> screen;
	
	private Stage UIstage;
	private ProgressBar progressBar;
	
	public SwitchScreen(Game game, Class<? extends Screen> screen, Class<?> screenAssets) {
		this.game = game;
		this.screen = screen;
		
		 // 开始加载资源
		try {
			Assets.instance.loadAssets(screenAssets);
			Assets.instance.update();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(this, "资源加载失败:" + e.getMessage());
			Gdx.app.exit();
		}
		
		UIstage = new Stage(GAME.UIViewport, GAME.batch);
//		Skin skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
		
		Pixmap pixmap1 = new Pixmap(1, 16, Format.RGB888);
		pixmap1.setColor(Color.WHITE);
		pixmap1.fill();
		
		Pixmap pixmap2 = new Pixmap(1, 16, Format.RGB888);
		pixmap2.setColor(Color.BLUE);
		pixmap2.fill();
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
		progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		
		progressBar = new ProgressBar(0, 1, 0.01f, false, progressBarStyle);
		progressBar.setAnimateDuration(0.1f);
		progressBar.setSize(GameConfig.width * 0.8f, 16);
		progressBar.setPosition((GameConfig.width - progressBar.getWidth())/2, 100);
		
		UIstage.addActor(progressBar);
	}
	
	@Override
	public void render(float delta) {
		
		if(Assets.instance.update() && progressBar.getVisualValue() == 1){ // 判断加载完成 && 也显示100%
			Screen instance = null;
			try {
				instance = ScreenProxy.instance.createScreen(screen); // 创建Screen代理
				ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // 销毁原Screen代理
			} 
			catch (Exception e) {
				Log.error(this, "screen切换失败");
				e.printStackTrace();
				Gdx.app.exit();
			}
			game.setScreen(instance);
		}
		
		progressBar.setValue(Assets.instance.getProgress());
		UIstage.getViewport().apply();
		UIstage.act(delta);
		UIstage.draw();
	}
	
	@Override
	public void hide() {
		Log.info(this, "dispose begin");
		game = null;
		screen = null;
		UIstage.dispose();
	}
}
