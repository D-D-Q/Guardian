package com.guardian.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 游戏主窗口UI
 * 
 * @author D
 * @date 2016年9月11日 上午11:51:03
 */
public class GameUI extends Table{
	
	public AttributesUI attributesUI;
	
	public CharacterUI characterUI;

	public GameUI(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameUI");
		this.setOrigin(Align.center);
//		this.setFillParent(true);
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.pad(8); 
		this.bottom().defaults();
		
		attributesUI = new AttributesUI(skin, i18NBundle);
		characterUI = new CharacterUI(skin, i18NBundle);
		
		// -----------------------------------------row
		Table panel = new Table(); // 表格
		panel.setDebug(GameConfig.UIdebug);
		panel.pad(8);
		panel.setVisible(false);
		this.add(panel).colspan(5).fill(); // fill会拉伸子元素，只有子元素是table不怕拉伸
		
		Cell<?> panelView = panel.add().colspan(4).fill(); // 切换展示页
		
		panel.row().bottom();
		
		Button panel_button = new Button(skin, GameScreenAssets.button1); // 切换按钮
		panel_button.setName("p1");
		panel_button.setTransform(true);
		panel_button.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(0.9f);
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(1f);
				super.touchUp(event, x, y, pointer, button);
			}
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				panelView.setActor(attributesUI);
			}
		});
		panel.add(panel_button).expandX().center();
		
		panel_button = new Button(skin, GameScreenAssets.button1);
		panel_button.setName("p2");
		panel_button.setTransform(true);
		panel_button.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(0.9f);
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(1f);
				super.touchUp(event, x, y, pointer, button);
			}
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				panelView.setActor(characterUI);
			}
		});
		panel.add(panel_button).expandX().center();
		
		panel_button = new Button(skin, GameScreenAssets.button1);
		panel_button.setName("p3");
		panel.add(panel_button).expandX().center();
		
		panel_button = new Button(skin, GameScreenAssets.button1);
		panel_button.setName("p4");
		panel.add(panel_button).expandX().center();
		
		// ---------------------------------------- row
		this.row();
		
		Button button = new Button(skin, GameScreenAssets.button1);
		button.setName("1");
		button.setTransform(true);
		button.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(0.9f);
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(1f);
				super.touchUp(event, x, y, pointer, button);
			}
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
				attributesComponent.curVit = MathUtils.clamp(attributesComponent.curVit + 100, 0, attributesComponent.maxVit);
			}
		});
		this.add(button).expandX().center();
		
		button = new Button(skin, GameScreenAssets.button1);
		button.setName("2");
		this.add(button).expandX().center();
		
		button = new Button(skin, GameScreenAssets.button1);
		button.setName("3");
		this.add(button).expandX().center();;
		
		button = new Button(skin, GameScreenAssets.button1);
		button.setName("4");
		this.add(button).expandX().center();;
		
		Button main_button = new Button(skin, GameScreenAssets.button1);
		main_button.setName("main_button");
		main_button.setTransform(true);
		main_button.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(0.9f);
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				actor.setScale(1f);
				super.touchUp(event, x, y, pointer, button);
			}
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Log.info(this, "main_button点击");
				
				// UI切换显示
				panelView.setActor(attributesUI); // 默认属性页
				panel.setVisible(!panel.isVisible());
			}
		});
		this.add(main_button).expandX().right();
	}
}
