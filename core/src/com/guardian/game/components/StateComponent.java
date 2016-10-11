package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 精灵状态组件
 * 
 * @author D
 * @date 2016年8月28日 下午2:32:17
 */
public class StateComponent implements Component, Poolable  {
	
	/**
	 * 状态
	 * 数字是方向，按照小键盘数字方位
	 * 
	 * @author D
	 * @date 2016年9月16日 下午5:24:09
	 */
	public static enum State{
		
		/**
		 * 空闲 
		 */
		idle(0), 
		
		/**
		 * 移动
		 */
		run(1),
		
		/**
		 * 攻击 
		 */
		attack(2);
		
		/**
		 * 玩完 
		 */
//		death1(3),
		
		public int value;
		
		private State(int value) {
			this.value = value;
		}
	}
	
	/**
	 * 方位按小键盘数字, 5是中心0,0。
	 * 动画数组索引是从2(下)开始顺时针定义
	 * 
	 * @author D
	 * @date 2016年9月16日 下午6:52:37
	 */
	public static enum Orientation{
		
		d2(0, 0, -1), d1(1, 7, -1, -1), d4(2, 6, -1, 0), d7(3, 5, -1, 1), 
		d8(4, 0, 1), d9(5, 3, 1, 1), d6(6, 2, 1, 0), d3(7, 0, 1, -1);
		
		public int value; // 动画数组的索引
		
		public int reverse; // 反方向动画数组的的索引
		
		/**
		 * 方向向量
		 */
		public Vector2 vector;
		
		private Orientation(int value, int x, int y){
			this.value = value;
			vector = new Vector2(x, y);
		}
		
		private Orientation(int value, int reverse, int x, int y){
			this.value = value;
			this.reverse = reverse;
			vector = new Vector2(x, y);
		}
	}
	
	/**
	 * 方向
	 */
	public Orientation orientation;

	/**
	 * 状态
	 */
	public State state;
	
	/**
	 * 动画帧时间
	 */
	public float stateTime;
	
	public StateComponent() {
		state = State.idle;
		orientation = Orientation.d2;
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		state = null;
		orientation = null;
	}
}
