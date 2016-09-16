package com.guardian.game;

import com.badlogic.ashley.core.Entity;
import com.guardian.game.components.CameraComponent;
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
	 * UI相机组件 
	 */
	public static CameraComponent UICameraComponent;

	/**
	 * 当前世界实体
	 */
	public static Entity screenEntity;
	
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
	 * 当前UI皮肤
	 */
//	public static Skin skin;
}
