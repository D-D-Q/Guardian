package com.game.core.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.game.core.component.MessageComponent;
import com.guardian.game.tools.MapperTools;

/**
 * 消息处理管理器
 * 不使用MessageManager了, 直接继承MessageDispatcher
 * 
 * @author D
 * @date 2016年10月13日 下午10:38:33
 */
public class MsgManager extends MessageDispatcher{
	
	public static final MsgManager instance = new MsgManager();
	
	private MsgManager() {
	}
	
	/**
	 * 发送消息
	 * 
	 * @param sender 发送实体
	 * @param receiver 接受实体
	 * @param msg 消息类型
	 * @param extraInfo 携带对象
	 * @param needsReturnReceipt 是否需要回执
	 */
	public void sendMessage(Entity sender, Entity receiver, int msg, Object extraInfo, boolean needsReturnReceipt) {
		
		MessageComponent senderMessageComponent = MapperTools.messageCM.get(sender);
		MessageComponent receiverMessageComponent = MapperTools.messageCM.get(receiver);
		
		dispatchMessage(0f, senderMessageComponent, receiverMessageComponent, msg, extraInfo, needsReturnReceipt);
	}

}
