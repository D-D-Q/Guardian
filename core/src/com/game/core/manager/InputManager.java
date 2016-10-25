package com.game.core.manager;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/**
 * 输入事件管理器
 * 
 * @author D
 * @date 2016年10月14日
 */
public class InputManager {
	
	public static InputManager instance = new InputManager();
	
	/**
	 * libgdx的事件链
	 */
	public InputMultiplexer inputMultiplexer = new InputMultiplexer(); //事件处理链
	
	public InputManager() {
		inputMultiplexer = new InputMultiplexer();
	}
	
	public void addProcessor (InputProcessor processor) {
		inputMultiplexer.addProcessor(processor);
	}
	
	public void removeProcessor (InputProcessor processor) {
		inputMultiplexer.removeProcessor(processor);
	}
}
