package com.guardian.game.entityscript;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.core.component.ScriptComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 寒冰卫士脚本
 * 
 * @author D
 * @date 2016年10月13日 下午10:13:52
 */
public class HbwsScript extends ScriptComponent {

	@Override
	public boolean handleMessage(Telegram msg) {
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
		
		Log.info(this, attributesComponent.name + ": 我被攻击");
		
		return true;
	}

	@Override
	public void beginContact(Contact contact) {

	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	@Override
	public void resetScript() {

	}
}
