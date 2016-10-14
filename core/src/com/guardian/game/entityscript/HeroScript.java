package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.game.core.script.EntityScript;

/**
 * 英雄脚本
 * 
 * @author D
 * @date 2016年10月13日 下午9:56:24
 */
public class HeroScript extends EntityScript {

	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		
		return false;
	}
	
}
