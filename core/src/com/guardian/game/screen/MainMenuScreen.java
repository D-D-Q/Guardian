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

	private final GuardianGame game;
	
	private Stage UIstage;
	
	private Texture login;
	
	public MainMenuScreen(GuardianGame guardianGame) {
		this.game = guardianGame;
		
		UIstage = new Stage(GAME.UICameraComponent.viewport, game.batch);
		Gdx.input.setInputProcessor(UIstage);
		
		login = Assets.instance.get(MainMenuScreenAssets.login, Texture.class);
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				
				game.setScreen(new SwitchScreen(game, GameScreen.class, GameScreenAssets.class));
				return true;
			}
			
		});
	}
	
	@Override
	public void render(float delta) {
		
		GAME.UICameraComponent.viewport.apply(); // true 相机位置锚点是左下，内部会camera.update(); // 更新相机数据
		game.batch.setProjectionMatrix(GAME.UICameraComponent.camera.combined); // 相机信息设置给SpriteBatch，否则相机无用
		
		game.batch.begin();
		game.batch.draw(login, 0, 0);
		game.batch.end();
	}
	
	@Override
	public void dispose() {
		Log.info(this, "dispose begin");
		
		UIstage.dispose();
		
		super.dispose();
	}
}
