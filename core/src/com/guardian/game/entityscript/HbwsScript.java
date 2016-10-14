package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.game.core.script.EntityScript;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 寒冰卫士脚本
 * 
 * @author D
 * @date 2016年10月13日 下午10:13:52
 */
public class HbwsScript extends EntityScript {

	@Override
	public boolean message(int messageType, Entity sender, Object extraInfo) {
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
		
		Log.info(this, attributesComponent.name + ": 我被攻击");
		
		return true;
	}
}
