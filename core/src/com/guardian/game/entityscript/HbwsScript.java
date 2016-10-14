package com.guardian.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
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
		AttributesComponent senderAttributesComponent = MapperTools.attributesCM.get(sender);
		
		Log.info(this, attributesComponent.name + ": 我" + senderAttributesComponent.name + "被攻击到了");
		
		return true;
	}

	@Override
	public void beginContact(Contact contact, Entity target) {
		Log.info(this, "beginContact");
	}

	@Override
	public void endContact(Contact contact, Entity target) {
		
	}
}
