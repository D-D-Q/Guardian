package com.guardian.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.game.core.Assets;
import com.game.core.GlobalInline;
import com.game.core.ScreenProxy;
import com.game.core.component.MapComponent;
import com.game.core.component.TextureComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.manager.InputManager;
import com.game.core.system.AnimationSystem;
import com.game.core.system.CombatSystem;
import com.game.core.system.GeneralSystem;
import com.game.core.system.MessageHandlingSystem;
import com.game.core.system.PathfindingSystem;
import com.game.core.system.PhysicsSystem;
import com.game.core.system.RenderingSystem;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.ItemComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.logs.Log;
import com.guardian.game.systems.EquippedSystem;
import com.guardian.game.systems.ItemsSystem;
import com.guardian.game.systems.Monstersystem;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.ui.GameUI;

/**
 * 游戏主屏幕
 * 
 * @author D
 * @date 2016年8月29日 下午9:40:56
 */
public class GameScreen extends ScreenAdapter {
	
	/**
	 * UI根节点
	 */
	private Stage UIstage;
	
	/**
	 * 子屏幕，可以快速切换的
	 */
	private Screen subScreen;
	
	private Screen xiuLianScreen;
	
	public GameScreen() {
		Log.info(this, "create begin");

		GlobalInline.instance.enter(this);
		
		// TODO 可以添加语言切换功能
		
		// 游戏视口，分辨率匹配
		GAME.gameViewport = new FillViewport(GameConfig.width, GameConfig.hieght); // 默认扩大显示
		
		// 资源
		GAME.i18NBundle = Assets.instance.get(GameScreenAssets.i18NBundle , I18NBundle.class); // 获得国际化
		GAME.skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
		UIstage = new Stage(GAME.UIViewport, GAME.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		
		AshleyManager ashleyManager = new AshleyManager();
		GlobalInline.instance.putAshleyManager(ashleyManager);
		
		// 地图
		MapComponent mapComponent = ashleyManager.engine.createComponent(MapComponent.class); // 添加地图组件
		mapComponent.init(Assets.instance.get(GameScreenAssets.map, TiledMap.class), 
				Assets.instance.get(GameScreenAssets.miniMap, Texture.class), 
				GAME.batch); // 初始化地图
		GlobalInline.instance.put("map", mapComponent);
		
		// 英雄
		GAME.hero = ashleyManager.entityDao.createHeroEntity(Assets.instance.get(GameScreenAssets.data1, CharactersTemplate.class), 1040, 480); // 创建英雄
		ashleyManager.engine.addEntity(GAME.hero);
		MapperTools.stateCM.get(GAME.hero).orientation = Orientation.d8;
		
		// ----要使用的系统
//		engine.addSystem((GAME.itemsSystem = new ItemsSystem(this)));
//		engine.addSystem((GAME.equippedSystem = new EquippedSystem(this)));
		GAME.itemsSystem = new ItemsSystem();
		GAME.equippedSystem = new EquippedSystem();
		
		ashleyManager.engine.addSystem(new GeneralSystem(5));
		ashleyManager.engine.addSystem(new PhysicsSystem(10));
		ashleyManager.engine.addSystem(new AnimationSystem(20));
		ashleyManager.engine.addSystem(new CombatSystem(30));
		
		ashleyManager.engine.addSystem(new RenderingSystem(50));
		
		ashleyManager.engine.addSystem(new PathfindingSystem(0.4f, 60));
		ashleyManager.engine.addSystem(new MessageHandlingSystem(70));
		ashleyManager.engine.addSystem(new Monstersystem(80));

		initUI();
		
		InputManager.instance.addProcessor(UIstage); // UI事件
		InputManager.instance.addProcessor(new InputAdapter() { // 拖动屏幕
			
			private float minGameCameraPositionX = 0 + GameConfig.width/2; // 地图左边边界是x坐标= 0 + 相机在屏幕中间
			private float maxGameCameraPositionX = MapComponent.width - GameConfig.width/2; // 地图右边边界x坐标=地图宽 - 相机在屏幕中间
			private float minGameCameraPositionY = 0 + GameConfig.hieght/2; // 地图下边边界是y坐标=0
			private float maxGameCameraPositionY = MapComponent.height - GameConfig.hieght/2; // 地图上边边界x坐标=地图高 - 相机在屏幕中间
			
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
				GAME.gameViewport.getCamera().position.x = MathUtils.clamp(GAME.gameViewport.getCamera().position.x - (screenX - this.screenX), 
						minGameCameraPositionX, maxGameCameraPositionX);
				GAME.gameViewport.getCamera().position.y = MathUtils.clamp(GAME.gameViewport.getCamera().position.y + (screenY - this.screenY), 
						minGameCameraPositionY, maxGameCameraPositionY);
				
				Log.debug(this, "gameCamera move to：" + GAME.gameViewport.getCamera().position.x + "," + GAME.gameViewport.getCamera().position.y);
				
				this.screenX = screenX;
				this.screenY = screenY;
				
				return true;
			}
			
		});
		
		GAME.gameViewport.getCamera().position.set(1040, 480, 0); // 初始化相机位置, 该位置会在屏幕中心  // 相机锚点是中心, 如果相机位置是0,0 那么虚拟世界坐标原点(0,0)拍摄的画面就是屏幕中间了
		
		xiuLianScreen = ScreenProxy.instance.createScreen(GameScreenSub1.class);
		subScreen = xiuLianScreen;
		
		GlobalInline.instance.exit();
	}
	
	/**
	 * 创建UI
	 */
	
	private void initUI(){
		
//		UIstage.addActor(new CharacterUI(skin, i18NBundle));
//		UIstage.addActor(new AttributesUI(skin, i18NBundle));
		UIstage.addActor(GameUI.instance);
		
//		GAME.itemsSystem.addItem(gettest());
//		GAME.itemsSystem.addItem(gettest());
//		GAME.itemsSystem.addItem(gettest());
//		GAME.itemsSystem.addItem(gettest());
	}
	
	@Override
	public void render(float delta) {
		
		// 游戏速度
		delta *= GameConfig.gameSpeed;
		
		GAME.gameViewport.apply();
		
		// AI时间
		GdxAI.getTimepiece().update(delta);
		
		// ECS系统
		GlobalInline.instance.getAshleyManager().engine.update(delta);
		
		if(subScreen != null){
			subScreen.render(delta);
		}
		
		GAME.UIViewport.apply();
		UIstage.act(delta);
		UIstage.draw(); // 它自己会把相机信息设置给SpriteBatch
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		GAME.gameViewport.update(width, height, false); // 设置屏幕宽高。必须！
	}
	
	@Override
	public void pause() {
		super.pause();
	}
	
	/**
	 * 游戏切出去过，资源可能被回收。需要重新判断加载
	 */
	@Override
	public void resume() {
		
		if(Assets.instance.update()){
			// TODO 恢复完成
		}
		
		// loadding
	}
	
	@Override
	public void show() {
		super.show();
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
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		
		// 测试添加物品
		Entity entity = ashleyManager.engine.createEntity();
		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
		textureComponent.textureRegion = GAME.skin.getRegion(GameScreenAssets.item1);
		entity.add(textureComponent);
		ItemComponent itemComponent = ashleyManager.engine.createComponent(ItemComponent.class);
		itemComponent.name = "大剑";
		itemComponent.subType = 0;
		entity.add(itemComponent);
		return entity;
	}
}
