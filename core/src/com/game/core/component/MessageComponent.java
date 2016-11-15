package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.tools.MapperTools;

/**
 * 消息组件
 * 	比如被攻击消息
 * 
 * @author D
 * @date 2016年10月9日 下午9:24:09
 */
@Deprecated
public class MessageComponent implements Component, Poolable, Telegraph  {
	
	/**
	 * 使用消息组件的实体
	 */
	public Entity entity;
	
	/**
	 * 可以接受的消息类型
	 */
	public int[] message;
	
	/**
	 * 接受消息
	 * @see com.badlogic.gdx.ai.msg.Telegraph#handleMessage(com.badlogic.gdx.ai.msg.Telegram)
	 */
	@Override
	public boolean handleMessage(Telegram msg) {
		
		// 转发消息到脚本
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null){
			MessageComponent senderMessageComponent = (MessageComponent)msg.sender;
			return scriptComponent.script.message(msg.message, senderMessageComponent.entity, msg.extraInfo);
		}
		
		return true;
	}
	
	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		entity = null;
		message = null;
	}
}