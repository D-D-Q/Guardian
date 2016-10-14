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

	/**
	 * libgdx的事件链
	 */
	public static InputMultiplexer inputMultiplexer = new InputMultiplexer(); //事件处理链
	
	public static void addProcessor (InputProcessor processor) {
		inputMultiplexer.addProcessor(processor);
	}
	
	public static void removeProcessor (InputProcessor processor) {
		inputMultiplexer.removeProcessor(processor);
	}
}
