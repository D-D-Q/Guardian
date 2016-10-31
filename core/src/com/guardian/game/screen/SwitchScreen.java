package com.guardian.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.game.core.Assets;
import com.guardian.game.GAME;
import com.guardian.game.logs.Log;

/**
 * 屏幕切换用，中间屏
 * 
 * @author D
 * @date 2016年9月2日 上午6:02:20
 */
public class SwitchScreen extends ScreenAdapter {
	
	private Game game;
	private Class<Screen> screen;
	
	private Stage UIstage;
	private Skin skin;
	private ProgressBar progressBar;
	
	private boolean isNotLoadding = true;
	
	
	@SuppressWarnings("unchecked")
	public <T extends Screen> SwitchScreen(Game game, Class<T> screen, Class<?> screenAssets) {
		this.game = game;
		this.screen = (Class<Screen>) screen;
		
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
//		skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
		
		SpriteDrawable drawable1 = new SpriteDrawable();
		drawable1.tint(Color.WHITE);
		
		SpriteDrawable drawable2 = new SpriteDrawable();
		drawable2.tint(Color.BLUE);
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle(drawable1, drawable2);
		progressBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
		progressBar.setPosition(100, 100);
		
		UIstage.addActor(progressBar);
	}
	
	@Override
	public void render(float delta) {
		
		progressBar.setValue(Assets.instance.getProgress() * 100);
		Log.info(this, "资源加载进度:" + Assets.instance.getProgress() * 100);
		UIstage.act(delta);
		UIstage.draw();
		
		if(Assets.instance.update()){ // 判断加载完成
			Screen instance = null;
			try {
				// 用libgdx的反射实现，可以支持GWT编译成html。用java的反射不能
//				Constructor constructor = ClassReflection.getConstructor(screen, GuardianGame.class);
//				instance = (Screen) constructor.newInstance(game);
//				Constructor constructor = ClassReflection.getConstructor(screen);
//				instance = (Screen) constructor.newInstance();
			} 
			catch (Exception e) {
				e.printStackTrace();
				Log.error(this, "screen切换失败:" + e.getMessage());
				Gdx.app.exit();
			} 
//			game.setScreen(instance);
		}
	}
	
	@Override
	public void hide() {
		Log.info(this, "dispose begin");
		UIstage.dispose();
	}
}
