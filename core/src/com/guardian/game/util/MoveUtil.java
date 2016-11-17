package com.guardian.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class MoveUtil {

	/**
	 * 按键移动
	 * 
	 * @return
	 */
	public static Vector2 getMove(){
		
		float x = 0, y = 0;
		
		if(Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.S))
			y = 0;
		else if(Gdx.input.isKeyPressed(Keys.W))
			y = 1;
		else if(Gdx.input.isKeyPressed(Keys.S))
			y = -1;
		
		if(Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D))
			x = 0;
		else if(Gdx.input.isKeyPressed(Keys.A))
			x = -1;
		else if(Gdx.input.isKeyPressed(Keys.D))
			x = 1;
		
		return new Vector2(x, y);
	}
}
