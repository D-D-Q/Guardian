package com.game.core.system;

import com.badlogic.ashley.core.EntitySystem;
import com.game.core.manager.MsgManager;

/**
 * 消息处理系统
 * 
 * @author D
 * @date 2016年10月9日 下午9:08:32
 */
public class MessageHandlingSystem extends EntitySystem{
	
	/**
	 * @param priority 系统调用优先级
	 */
	public MessageHandlingSystem(int priority) {
		super(priority);
	}
	
	@Override
	public void update(float deltaTime) {
		MsgManager.instance.update(); // 处理消息
	}
}
