package com.guardian.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.guardian.game.GuardianGame;
import com.guardian.game.logs.Log;

/**
 * 屏幕切换用，中间屏
 * 
 * @author D
 * @date 2016年9月2日 上午6:02:20
 */
public class SwitchScreen extends ScreenAdapter {
	
	public final GuardianGame game;
	public Class<Screen> screen;
	public Class<?> screenAssets;
	public boolean isNotLoadding = true;
	
	@SuppressWarnings("unchecked")
	public <T extends Screen> SwitchScreen(GuardianGame game, Class<T> screen, Class<?> screenAssets) {
		this.game = game;
		this.screen = (Class<Screen>) screen;
		this.screenAssets = screenAssets;
		
		 // 开始加载资源
		try {
			game.assets.loadAssets(screenAssets);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(this, "资源加载失败:" + e.getMessage());
			Gdx.app.exit();
		}
	}
	
	@Override
	public void render(float delta) {
		
		if(game.assets.assetManager.update()){ // 判断加载完成
			Screen instance = null;
			try {
				// 用libgdx的反射实现，可以支持GWT编译成html。用java的反射不能
				Constructor constructor = ClassReflection.getConstructor(screen, GuardianGame.class);
				instance = (Screen) constructor.newInstance(game);
			} 
			catch (Exception e) {
				e.printStackTrace();
				Log.error(this, "screen切换失败:" + e.getMessage());
				Gdx.app.exit();
			} 
			game.setScreen(instance);
		}
		
		// TODO 加载中...
//		game.batch.begin();
		
		Log.info(this, game.assets.assetManager.getProgress());
		
//		game.batch.end();
	}
}
