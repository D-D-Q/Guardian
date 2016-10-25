package com.guardian.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.core.Assets;
import com.game.core.component.CameraComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.manager.InputManager;
import com.game.core.manager.PhysicsManager;
import com.game.core.system.AnimationSystem;
import com.game.core.system.CombatSystem;
import com.game.core.system.GeneralSystem;
import com.game.core.system.MessageHandlingSystem;
import com.game.core.system.PathfindingSystem;
import com.game.core.system.PhysicsSystem;
import com.game.core.system.RenderingSystem;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.logs.Log;
import com.guardian.game.screen.GameScreen;
import com.guardian.game.systems.EquippedSystem;
import com.guardian.game.systems.ItemsSystem;
import com.guardian.game.systems.Monstersystem;
import com.guardian.game.tools.MapperTools;

/**
 * 游戏主运行类
 * 
 * @author D
 * @date 2016年9月11日 下午2:15:32
 */
public class GuardianGame extends Game {
	
	public static GuardianGame game;
	
	/**
	 * opengl绘制
	 */
	public SpriteBatch batch;
	
	/**
	 * 在控制台输出fps
	 */
	FPSLogger fpsLog;
	
	@Override
	public void create () {
		
		game = this;
		
		/**--------------------libgdx start-------------------------*/
		
		Log.setLogLevel(Application.LOG_INFO); // 日志级别
		if(GameConfig.fpsDebug)
			fpsLog = new FPSLogger();
		
		Log.info(this, "create begin");
		
		try {
//			Assets.instance.loadAssets(MainMenuScreenAssets.class);
			Assets.instance.loadAssets(GameScreenAssets.class);
		} catch (Exception e) {
			Log.error(this, "资源加载失败:" + e.getMessage());
			e.printStackTrace();
			Gdx.app.exit(); // 不会立刻停止，方法还好继续向下执行
		}
		
		batch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(InputManager.instance.inputMultiplexer); // 监听输入事件
		
		/**--------------------libgdx end-------------------------*/
		
		/**--------------------ashley start-------------------------*/

		GAME.UICameraComponent = AshleyManager.instance.engine.createComponent(CameraComponent.class);
		
//		engine.addSystem((GAME.itemsSystem = new ItemsSystem(this)));
//		engine.addSystem((GAME.equippedSystem = new EquippedSystem(this)));
		GAME.itemsSystem = new ItemsSystem(this);
		GAME.equippedSystem = new EquippedSystem(this);
		
		AshleyManager.instance.engine.addSystem(new GeneralSystem(5));
		AshleyManager.instance.engine.addSystem(new PhysicsSystem(10));
		AshleyManager.instance.engine.addSystem(new AnimationSystem(20));
		AshleyManager.instance.engine.addSystem(new CombatSystem(30));
		
		AshleyManager.instance.engine.addSystem(new RenderingSystem(this, 50));
		
		AshleyManager.instance.engine.addSystem(new PathfindingSystem(0.4f, 60));
		AshleyManager.instance.engine.addSystem(new MessageHandlingSystem(70));
		AshleyManager.instance.engine.addSystem(new Monstersystem(80));
		
		/**--------------------ashley end-------------------------*/
		
//		DataTemplateDao dataTemplateDao = new DataTemplateDao();
//		dataTemplateDao.load(GameScreenAssets.charactersTemplate);
		
		Assets.instance.finishLoading();
		
		Log.info(this, "资源加载进度:" + Assets.instance.getProgress() * 100);
		
//		setScreen(new MainMenuScreen(this));
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		Log.debug(this, "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
		
		if(GameConfig.physicsdebug){
			CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
			if(cameraComponent != null)
				PhysicsManager.instance.debugRender(cameraComponent.camera);
		}
		
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
		GAME.UICameraComponent.viewport.update(width, height, false); // true表示设置相机的位置在:(设计分辨率宽/2, 设计分辨率高/2)。这样起始显示屏幕左下角就是虚拟世界坐标原点0,0了
	}
	
	@Override
	public void dispose () {
		Log.info(this, "dispose begin");
		super.dispose();
		
		game = null;
		
		AshleyManager.instance.engine.clearPools();
		PhysicsManager.instance.dispose();
		
		batch.dispose();
		Assets.instance.dispose();
	}
}
