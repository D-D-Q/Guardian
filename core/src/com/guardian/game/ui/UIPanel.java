package com.guardian.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.guardian.game.GameConfig;

public class UIPanel extends Table {

	public UIPanel() {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("UIPanel");
		this.setFillParent(true);
		this.pad(35); 
		this.bottom().right();
	}
}
