package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.game.core.script.EntityScript;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.tools.MessageType;

/**
 * 英雄脚本
 * 
 * @author D
 * @date 2016年10月13日 下午9:56:24
 */
public class HeroScript extends EntityScript implements InputProcessor{
	
	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		
		super.message(messageType, sender, extraInfo);
		
		// 加经验升级
		if(messageType == MessageType.MSG_DEATH){
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			attributesComponent.curExp += 1; // TODO 暂固定一个怪加1
			
			while(attributesComponent.curExp >= attributesComponent.levelUpExp){
				
				attributesComponent.Lv += 1;
				attributesComponent.curAttrs += 4; // 每级加4属性点
				attributesComponent.maxVit += 10; // 每级加10体力
				attributesComponent.curVit += 10;
				attributesComponent.curExp -= attributesComponent.levelUpExp;
			}
		}
	
		return true;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		
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
		
		MapperTools.characterCM.get(entity).move(new Vector2(x, y).nor());
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
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
		
		if(x == 0 && y == 0)
			MapperTools.characterCM.get(entity).stopMove();
		else
			MapperTools.characterCM.get(entity).move(new Vector2(x, y).nor());
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
