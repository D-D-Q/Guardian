package com.game.core.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;

/**
 * 不同状态实现类的适配, 用于分层状态的接口
 * State接口受DefaultStateMachine的泛型要求影响，不能用作DefaultStateMachine的泛型适配多种状态实现类
 * 
 * 最好只在update里切换状态
 * 
 * @author D
 * @date 2016年11月16日 下午8:58:41
 */
public interface StateAdapter extends State<Entity> {

	/**
	 * 获得父节点
	 * 
	 * @return
	 */
	public StateAdapter getParentState();
	
	/**
	 * 获得全局状态
	 * 
	 * @return
	 */
	public StateAdapter getGlobalState();
}
