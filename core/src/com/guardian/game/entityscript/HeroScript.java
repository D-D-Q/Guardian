package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.game.core.script.EntityScript;
import com.guardian.game.components.CombatComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 英雄脚本
 * 
 * @author D
 * @date 2016年10月13日 下午9:56:24
 */
public class HeroScript extends EntityScript implements InputProcessor{

	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		
		return false;
	}

	@Override
	public void beginContact(Contact contact, Entity target) {
		
		Log.info(this, "beginContact");
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent.targetEntity == null)
			combatComponent.targetEntity = target;
	}

	@Override
	public void endContact(Contact contact, Entity target) {
		
		CombatComponent combatComponent = MapperTools.combatCM.get(entity);
		if(combatComponent.targetEntity == target)
			combatComponent.targetEntity = null;
	}

	float speed = 100;
	
	@Override
	public boolean keyDown (int keycode) {
		
		Body body = MapperTools.characterCM.get(entity).dynamicBody;
		
		float x = 0, y = 0;
		
		if(Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.S))
			y = 0;
		else if(Gdx.input.isKeyPressed(Keys.W))
			y = speed;
		else if(Gdx.input.isKeyPressed(Keys.S))
			y = -speed;
		
		if(Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D))
			x = 0;
		else if(Gdx.input.isKeyPressed(Keys.A))
			x = -speed;
		else if(Gdx.input.isKeyPressed(Keys.D))
			x = speed;
		
		body.setLinearVelocity(x, y);
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
		Body body = MapperTools.characterCM.get(entity).dynamicBody;
		
		float x = 0, y = 0;
		
		if(Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.S))
			y = 0;
		else if(Gdx.input.isKeyPressed(Keys.W))
			y = speed;
		else if(Gdx.input.isKeyPressed(Keys.S))
			y = -speed;
		
		if(Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D))
			x = 0;
		else if(Gdx.input.isKeyPressed(Keys.A))
			x = -speed;
		else if(Gdx.input.isKeyPressed(Keys.D))
			x = speed;
		
		body.setLinearVelocity(x, y);
		
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
}
