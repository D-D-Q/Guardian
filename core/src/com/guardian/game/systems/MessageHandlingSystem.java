package com.guardian.game.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;

/**
 * 消息处理系统
 * 
 * @author D
 * @date 2016年10月9日 下午9:08:32
 */
public class MessageHandlingSystem extends EntitySystem{
	
	public static final int MSG_ATTACK = 0;
	
	/**
	 * 消息处理管理器
	 */
	private static MessageManager messageManager;
	
	/**
	 * @param priority 系统调用优先级
	 */
	public MessageHandlingSystem(int priority) {
		super(priority);
		
		messageManager = MessageManager.getInstance();
	}
	
	@Override
	public void update(float deltaTime) {
		
		GdxAI.getTimepiece().update(deltaTime); // 更新AI系统时间，所有AI都要使用这个时间
		
		messageManager.update(); // 处理消息
	}

	public static MessageManager getMessageManager() {
		return messageManager;
	}
}
