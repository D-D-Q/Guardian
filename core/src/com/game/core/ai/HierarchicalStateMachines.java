package com.game.core.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.MapperTools;

/**
 * 分层状态机
 * 修改DefaultStateMachine的全局状态成从当前状态获取
 * 
 * @author D
 * @date 2016年11月19日 下午6:58:50
 */
public class HierarchicalStateMachines extends DefaultStateMachine<Entity, StateAdapter> {
	
	/**
	 * 是否开始执行
	 */
	private boolean isStart = false;
	
	/**
	 * @param initialState 初始状态
	 */
	public HierarchicalStateMachines(StateAdapter initialState) {
		super(null, initialState); // 第一个参数(owner)在该组件添加到实体的时候赋值, 查看EntityManager类。
	}

	@Override
	public void changeState(StateAdapter newState) {
		
		if(newState == null)
			throw new RuntimeException("跳转状态是null");
		
		if(currentState == null){
			if(newState.getParentState() != null){
				throw new RuntimeException("当前状态是null，只能跳转顶层状态");
			}
		}
		else if(currentState.getParentState() != newState // 不是当前状态的父状态
				&& currentState != newState.getParentState()// 不是当前状态的子状态
				&& currentState.getParentState() != newState.getParentState()){ // 不是同父子状态
			
			throw new RuntimeException("只能跳转子状态，父状态，同父状态");
		}
				if(Gdx.app.getLogLevel() == Application.LOG_DEBUG){
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(owner);
			Log.debug(this, attributesComponent.name + "，转换状态：" + newState);
		}
		
		super.changeState(newState);
	}
	
	/**
	 * 返回父状态
	 */
	public void revertParentState(){
		if(currentState != null && currentState.getParentState() != null){
			super.changeState(currentState.getParentState());
		}
	}
	
	@Override
	public void update() {
		
		if(!isStart){
			if (currentState != null) currentState.enter(owner); // 初始状态的enter。DefaultStateMachine不会调用初始状态的enter
			isStart = true;
		}
		
		if (currentState != null){
			
			/* 如果全局状态执行时跳转了状态，那么新状态的全局状态也要被执行。如果是相同的全局状态则不必 */
			StateAdapter currentState_temp; 
			
			do{
				currentState_temp = currentState; // 记录执行前状态
				globalState = currentState.getGlobalState(); // 获得全局状态
				
				if (globalState != null) globalState.update(owner);
				
			}while(currentState_temp != currentState && globalState != currentState.getGlobalState()); // 不同表示跳转过。排除全局状态相同重复执行
			
			currentState.update(owner);
		}
	}
}
