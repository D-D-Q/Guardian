package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
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
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
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
