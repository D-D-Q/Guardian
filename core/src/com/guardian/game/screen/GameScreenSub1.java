package com.guardian.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.game.core.Assets;
import com.game.core.GlobalInline;
import com.game.core.component.MapComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.system.AnimationSystem;
import com.game.core.system.CombatSystem;
import com.game.core.system.GeneralSystem;
import com.game.core.system.MessageHandlingSystem;
import com.game.core.system.PathfindingSystem;
import com.game.core.system.PhysicsSystem;
import com.game.core.system.RenderingSystem;
import com.guardian.game.GAME;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.tools.MapperTools;

public class GameScreenSub1 extends ScreenAdapter {

	public GameScreenSub1() {
		
		// ECS系统
		AshleyManager ashleyManager = new AshleyManager();
		GlobalInline.instance.putAshleyManager(ashleyManager);
		
		ashleyManager.engine.addSystem(new GeneralSystem(5));
		ashleyManager.engine.addSystem(new PhysicsSystem(10));
		ashleyManager.engine.addSystem(new AnimationSystem(20));
		ashleyManager.engine.addSystem(new CombatSystem(30));
		
		ashleyManager.engine.addSystem(new RenderingSystem(50));
		
		ashleyManager.engine.addSystem(new PathfindingSystem(0.4f, 60));
		ashleyManager.engine.addSystem(new MessageHandlingSystem(70));
		
		// 地图
		MapComponent mapComponent = ashleyManager.engine.createComponent(MapComponent.class); // 添加地图组件
		mapComponent.init(Assets.instance.get(GameScreenAssets.map1, TiledMap.class), 
				Assets.instance.get(GameScreenAssets.miniMap, Texture.class), 
				GAME.batch); // 初始化地图
		GlobalInline.instance.put("map", mapComponent);
	}
	
	@Override
	public void render(float delta) {
		
		// ECS系统
		GlobalInline.instance.getAshleyManager().engine.update(delta);
	}
	
	@Override
	public void show() {
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		
		// 英雄
		ashleyManager.addCopy(GAME.hero);
		MapperTools.stateCM.get(GAME.hero).orientation = Orientation.d8;
	}
	
	@Override
	public void hide() {
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		ashleyManager.removeForCopy(GAME.hero);
		ashleyManager.engine.removeAllEntities();
		
		GlobalInline.instance.clearMark();
	}
	
	@Override
	public void dispose() {
		GlobalInline.instance.getAshleyManager().disabled();
		GlobalInline.instance.disabled(this.getClass());
	}
}
