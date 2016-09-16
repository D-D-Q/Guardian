package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 物品通用属性组件
 * 
 * @author D
 * @date 2016年9月10日 上午10:30:16
 */
public class ItemComponent implements Component, Poolable{
	
	/**
	 * 名称
	 */
	public String name;
	
	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 子类型,装备的子类型对应装备的位置
	 */
	public int subType;
	
	/**
	 * 数量
	 */
	public int num;

	@Override
	public void reset() {
		type = 0;
		subType = 0;
		num = 0;
	}
}
