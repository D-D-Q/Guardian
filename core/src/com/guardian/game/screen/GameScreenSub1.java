package com.guardian.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.game.core.GlobalInline;

public class GameScreenSub1 extends ScreenAdapter {

	public GameScreenSub1() {
		GlobalInline.instance.enter(this);
		
		
		
		
		
		GlobalInline.instance.exit();
	}
	
	@Override
	public void render(float delta) {
		
	}
}
