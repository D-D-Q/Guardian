package com.guardian.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.core.Assets;
import com.guardian.game.GAME;
import com.guardian.game.GuardianGame;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.assets.MainMenuScreenAssets;
import com.guardian.game.logs.Log;

/**
 * 游戏主菜单
 * 
 * @author D
 * @date 2016年8月29日 下午9:41:05
 */
public class MainMenuScreen extends ScreenAdapter{

	private Stage UIstage;
	
	private Texture login;
	
	public MainMenuScreen() {
		
		UIstage = new Stage(GAME.UIViewport, GAME.batch);
		Gdx.input.setInputProcessor(UIstage);
		
		login = Assets.instance.get(MainMenuScreenAssets.login, Texture.class);
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				
				GuardianGame.game.setScreen(new SwitchScreen(GuardianGame.game, GameScreen.class, GameScreenAssets.class));
				return true;
			}
			
		});
	}
	
	@Override
	public void render(float delta) {
		
		GAME.UIViewport.apply(); // true 相机位置锚点是左下，内部会camera.update(); // 更新相机数据
		GAME.batch.setProjectionMatrix(GAME.UIViewport.getCamera().combined); // 相机信息设置给SpriteBatch，否则相机无用
		
		GAME.batch.begin();
		GAME.batch.draw(login, 0, 0);
		GAME.batch.end();
	}
	
	@Override
	public void dispose() {
		Log.info(this, "dispose begin");
		
		UIstage.dispose();
		
		super.dispose();
	}
}
