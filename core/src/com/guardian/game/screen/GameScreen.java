package com.guardian.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.game.core.manager.AshleyManager;
import com.game.core.manager.InputManager;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.GuardianGame;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.components.ItemComponent;
import com.guardian.game.components.MapComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.ui.AttributesUI;
import com.guardian.game.ui.CharacterUI;
import com.guardian.game.ui.GameUI;

/**
 * 游戏主屏幕
 * 
 * @author D
 * @date 2016年8月29日 下午9:40:56
 */
public class GameScreen extends ScreenAdapter {
	
	private I18NBundle i18NBundle;
	
	private Stage UIstage;
	
	private Skin skin;
	
	public GameScreen() {
		Log.info(this, "create begin");
		
		i18NBundle = GuardianGame.game.assets.getI18NBundle(GameScreenAssets.i18NBundle); // 获得国际化
		skin = GuardianGame.game.assets.getSkin(GameScreenAssets.default_skin); // 获得皮肤
		UIstage = new Stage(GAME.UICameraComponent.viewport, GuardianGame.game.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		
		GAME.screenEntity = AshleyManager.engine.createEntity(); // 表示当前Screen的实体
		AshleyManager.engine.addEntity(GAME.screenEntity);
		
		MapComponent mapComponent = AshleyManager.engine.createComponent(MapComponent.class); // 添加地图组件
		GAME.screenEntity.add(mapComponent);
		mapComponent.init(GuardianGame.game.assets.getMap(GameScreenAssets.map), GuardianGame.game.batch); // 初始化地图
		
		CameraComponent gameCameraComponent = AshleyManager.engine.createComponent(CameraComponent.class); // 添加相机组件
		GAME.screenEntity.add(gameCameraComponent);
		gameCameraComponent.camera.position.set(864, 480, 0); // 初始化相机位置, 该位置会在屏幕中心  // 相机锚点是中心, 如果相机位置是0,0 那么虚拟世界坐标原点(0,0)拍摄的画面就是屏幕中间了
		
		gameCameraComponent.camera.zoom = 10;
		
		GAME.hero = AshleyManager.entityDao.createHeroEntity(GAME.charactersTemplate.get(0), 864, 480); // 创建英雄
		AshleyManager.engine.addEntity(GAME.hero);
		MapperTools.stateCM.get(GAME.hero).orientation = Orientation.d8;
		
		initUI();
		
		InputManager.addProcessor(UIstage); // UI事件
		InputManager.addProcessor(new InputAdapter() { // 拖动屏幕
			
			private int minGameCameraPositionX = 0 + GameConfig.width/2; // 地图左边边界是x坐标= 0 + 相机在屏幕中间
			private int maxGameCameraPositionX = mapComponent.width - GameConfig.width/2; // 地图右边边界x坐标=地图宽 - 相机在屏幕中间
			private int minGameCameraPositionY = 0 + GameConfig.hieght/2; // 地图下边边界是y坐标=0
			private int maxGameCameraPositionY = mapComponent.height - GameConfig.hieght/2; // 地图上边边界x坐标=地图高 - 相机在屏幕中间
			
			private int screenX, screenY;
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				this.screenX = screenX;
				this.screenY = screenY;
				return true;
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				this.screenX = 0;
				this.screenY = 0;
				return true;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) { // X,Y是鼠标新位置的坐标,以右上角为0,0。是屏幕坐标系不是游戏世界坐标系，
				
				// 显示相机范围，不能超地图
				gameCameraComponent.camera.position.x = MathUtils.clamp(gameCameraComponent.camera.position.x - (screenX - this.screenX), 
						minGameCameraPositionX, maxGameCameraPositionX);
				gameCameraComponent.camera.position.y = MathUtils.clamp(gameCameraComponent.camera.position.y + (screenY - this.screenY), 
						minGameCameraPositionY, maxGameCameraPositionY);
				
				Log.debug(this, "gameCamera move to：" + gameCameraComponent.camera.position.x + "," + gameCameraComponent.camera.position.y);
				
				this.screenX = screenX;
				this.screenY = screenY;
				
				return true;
			}
			
		});
	}
	
	/**
	 * 创建UI
	 */
	
	private void initUI(){
		
		UIstage.addActor(new CharacterUI(skin, i18NBundle));
		UIstage.addActor(new AttributesUI(skin, i18NBundle));
		UIstage.addActor(new GameUI(skin, i18NBundle));
		
		GAME.itemsSystem.addItem(gettest());
		GAME.itemsSystem.addItem(gettest());
		GAME.itemsSystem.addItem(gettest());
		GAME.itemsSystem.addItem(gettest());
	}
	
	@Override
	public void render(float delta) {
		
		// AI时间
		GdxAI.getTimepiece().update(delta);
		
		// ECS系统
		AshleyManager.engine.update(delta);
		
		GAME.UICameraComponent.viewport.apply();
		UIstage.act(delta);
		UIstage.draw(); // 它自己会把相机信息设置给SpriteBatch
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
		cameraComponent.viewport.update(width, height, false); // 设置屏幕宽高。必须！
	}
	
	/**
	 * 游戏切出去过，资源可能被回收。需要重新判断加载
	 */
	@Override
	public void resume() {
		
		if(GuardianGame.game.assets.assetManager.update()){
			// TODO 恢复完成
		}
		
		// loadding
	}
	
	/**
	 * dispose的时候调用, Screen的dispose根本不会被调用
	 */
	@Override
	public void hide() {
		Log.info(this, "dispose begin");
		UIstage.dispose();
	}
	
	private Entity gettest(){
		// 测试添加物品
		Entity entity = AshleyManager.engine.createEntity();
		TextureComponent textureComponent = AshleyManager.engine.createComponent(TextureComponent.class);
		textureComponent.textureRegion = skin.getRegion(GameScreenAssets.item1);
		entity.add(textureComponent);
		ItemComponent itemComponent = AshleyManager.engine.createComponent(ItemComponent.class);
		itemComponent.name = "大剑";
		itemComponent.subType = 0;
		entity.add(itemComponent);
		return entity;
	}
}
