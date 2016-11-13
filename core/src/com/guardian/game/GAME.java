package com.guardian.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.guardian.game.systems.EquippedSystem;
import com.guardian.game.systems.ItemsSystem;

/**
 * 游戏逻辑需求的变量
 * 
 * @author D
 * @date 2016年9月11日 下午2:08:56
 */
public class GAME {
	
	/**
	 * 全局的openGL绘制对象
	 */
	public static SpriteBatch batch;
	
	/**
	 * 游戏窗口相机
	 */
	public static Viewport gameViewport;
	
	/**
	 * UI相机组件 
	 */
	public static Viewport UIViewport;

	/**
	 * 英雄
	 */
	public static Entity hero;

	/**
	 * 装备系统
	 */
	public static EquippedSystem equippedSystem;
	
	/**
	 * 物品系统 
	 */
	public static ItemsSystem itemsSystem;
	
	/**
	 * 国际化
	 */
	public static I18NBundle i18NBundle;
	
	/**
	 * 当前UI皮肤
	 */
	public static Skin skin;
	
}
