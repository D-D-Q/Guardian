package com.guardian.game;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.core.Assets;
import com.game.core.manager.EntityManager;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.entity.dao.DataTemplateDao;
import com.guardian.game.entity.dao.EntityDao;
import com.guardian.game.logs.Log;
import com.guardian.game.screen.GameScreen;
import com.guardian.game.systems.AnimationSystem;
import com.guardian.game.systems.CombatSystem;
import com.guardian.game.systems.EquippedSystem;
import com.guardian.game.systems.ItemsSystem;
import com.guardian.game.systems.MessageHandlingSystem;
import com.guardian.game.systems.PhysicsSystem;
import com.guardian.game.systems.RenderingSystem;

/**
 * 游戏主运行类
 * 
 * @author D
 * @date 2016年9月11日 下午2:15:32
 */
public class GuardianGame extends Game {
	
	/**
	 * opengl绘制
	 */
	public SpriteBatch batch;
	
	/**
	 * 资源管理
	 */
	public Assets assets;
	
	/**
	 * ashley组件实体系统引擎
	 */
	public PooledEngine engine;
	
	public EntityDao entityDao;
	
	/**
	 * 在控制台输出fps
	 */
	FPSLogger fpsLog;
	
	@Override
	public void create () {
		
		Log.setLogLevel(Application.LOG_INFO); // 日志级别
		fpsLog = new FPSLogger();
		Log.info(this, "create begin");
		
		assets = new Assets();
		try {
//			assets.loadAssets(MainMenuScreenAssets.class);
			assets.loadAssets(GameScreenAssets.class);
		} catch (Exception e) {
			Log.error(this, "资源加载失败:" + e.getMessage());
			e.printStackTrace();
		}
		
		DataTemplateDao dataTemplateDao = new DataTemplateDao();
		dataTemplateDao.load(GameScreenAssets.charactersTemplate);
		
		batch = new SpriteBatch();

		engine = new PooledEngine(1, 10, 1, 10);
		engine.addEntityListener(new EntityManager());
		
		entityDao = new EntityDao(this);
		
		GAME.UICameraComponent = engine.createComponent(CameraComponent.class);
		
//		engine.addSystem((GAME.itemsSystem = new ItemsSystem(this)));
//		engine.addSystem((GAME.equippedSystem = new EquippedSystem(this)));
		GAME.itemsSystem = new ItemsSystem(this);
		GAME.equippedSystem = new EquippedSystem(this);
		
		engine.addSystem(new PhysicsSystem(0));
		engine.addSystem(new AnimationSystem(1));
		engine.addSystem(new CombatSystem(2));
		engine.addSystem(new MessageHandlingSystem(3));
		engine.addSystem(new RenderingSystem(this, 4));
		
		assets.assetManager.finishLoading();
		
//		setScreen(new MainMenuScreen(this));
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		Log.debug(this, "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
		
//		fpsLog.log();
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
		
		engine.clearPools();
		PhysicsManager.dispose();
		
		batch.dispose();
		assets.dispose();
	}
}
