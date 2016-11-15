package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.script.EntityScript;

/**
 * 脚本组件，包含消息
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public class ScriptComponent implements Component, Telegraph, Poolable{

	/**
	 * 脚本对象。
	 */
	public EntityScript script;
	
	/**
	 * 可以接受的消息类型
	 */
	public int[] message;

	@Override
	public boolean handleMessage(Telegram msg) {
		
		ScriptComponent senderScriptComponent = (ScriptComponent)msg.sender;
		return script.message(msg.message, senderScriptComponent.script.entity, msg.extraInfo);
	}
	
	@Override
	public void reset() {
		script = null;
	}
}
