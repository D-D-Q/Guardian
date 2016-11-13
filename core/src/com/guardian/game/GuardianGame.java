package com.guardian.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.game.core.Assets;
import com.game.core.GlobalInline;
import com.game.core.SwitchScreen;
import com.game.core.manager.AshleyManager;
import com.game.core.manager.InputManager;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.logs.Log;
import com.guardian.game.screen.GameScreen;

/**
 * 游戏主运行类
 * 
 * @author D
 * @date 2016年9月11日 下午2:15:32
 */
public class GuardianGame extends Game {
	
	public static GuardianGame game;
	
	/**
	 * 在控制台输出fps
	 */
	FPSLogger fpsLog;
	
	@Override
	public void create () {
		
		game = this;
		
		Log.setLogLevel(Application.LOG_INFO); // 日志级别
		if(GameConfig.fpsDebug)
			fpsLog = new FPSLogger();
		
		Log.info(this, "create begin");
		
		GAME.batch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(InputManager.instance.inputMultiplexer); // 监听输入事件
		
//		GAME.UIViewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // UI窗口大小等于当前设备
		GAME.UIViewport = new FillViewport(GameConfig.width, GameConfig.hieght);
		
		setScreen(new SwitchScreen(this, GameScreen.class, GameScreenAssets.class));
	}

	@Override
	public void render () {
		Log.debug(this, "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
		
		if(GameConfig.physicsdebug && GAME.gameViewport != null)
			PhysicsManager.instance.debugRender(GAME.gameViewport.getCamera());
		
		if(GameConfig.fpsDebug)
			fpsLog.log();
	}
	
	/**
	 * 窗口大小改变
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Log.info(this, "resize " + width + "," + height);
		super.resize(width, height);
		
		GAME.UIViewport.update(width, height, true); // true表示设置相机的位置在:(设计分辨率宽/2, 设计分辨率高/2)。这样起始显示屏幕左下角就是虚拟世界坐标原点0,0了
	}
	
	@Override
	public void dispose () {
		Log.info(this, "dispose begin");
		super.dispose();
		
		game = null;
		
		GlobalInline.instance.disabledALL();
		PhysicsManager.instance.dispose();
		
		GAME.batch.dispose();
		Assets.instance.dispose();
	}
}
