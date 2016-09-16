package com.guardian.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.guardian.game.GameConfig;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.logs.Log;

/**
 * 游戏主窗口UI
 * 
 * @author D
 * @date 2016年9月11日 上午11:51:03
 */
public class GameUI extends Table{

	public GameUI(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameUI");
		this.setFillParent(true);
		this.pad(35); 
		this.bottom().right();
		
		Button button = new Button(skin, GameScreenAssets.button1);
		button.setName("main_button");
		button.setOrigin(button.getWidth()/2, button.getHeight()/2);
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
				
				for(Actor actor : GameUI.this.getParent().getChildren())
					if("AttributesUI".equals(actor.getName())){ // TODO UI切换显示
						actor.setVisible(!actor.isVisible());
						break;
					}
						
				Log.info(this, "main_button点击");
			}
		});
		
		this.add(button);
	}
}
