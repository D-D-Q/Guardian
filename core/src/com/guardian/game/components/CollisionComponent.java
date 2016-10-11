package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 碰撞检测包围盒
 * 
 * 碰撞检测范围, 一般大概是图像宽, 图像高/2。
 * 	高看着取，在地图上要有立体感，不能等于图像高。
 * @author D
 * @date 2016年9月17日 上午8:01:16
 */
public class CollisionComponent implements Component, Poolable{

	
	
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		
	}
}
