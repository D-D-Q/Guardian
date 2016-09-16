package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 变换组件
 * 保存精灵的位置、缩放、旋转信息
 * 
 * @author D
 * @date 2016年8月28日 下午2:31:59
 */
public class TransformComponent implements Component, Poolable  {
	
	/**
	 * 位置信息
	 * x轴和y轴 屏幕左下为原点
	 * z抽、深度、绘制优先级、数越小越先绘制（先绘制会被覆盖）
	 */
	public final Vector3 position = new Vector3();
	
	/**
	 * 精灵宽高，通常等于纹理
	 */
	public int width;
	public int height;
	
	/**
	 * 精灵定位锚点坐标。以精灵大小为世界，以精灵左下角为原点0, 0
	 * 绘制显示时，会把该点显示在position所指的位置上
	 */
	public final Vector2 positionOrigin = new Vector2();
	
	/**
	 * 引擎锚点位置，缩放、旋转使用
	 */
	public final Vector2 origin = new Vector2();
	
	/**
	 * 缩放
	 */
	public final Vector2 scale = new Vector2(1.0f, 1.0f);
	 
	/**
	 * 逆时针旋转
	 */
	public float rotation = 0.0f;
	 
	/**
	 * 是否隐藏
	 */
	public boolean isHidden = false;
	
	/**
	 * 复制
	 */
	public TransformComponent copyTo(TransformComponent temp){
		temp.position.set(this.position);
		temp.scale.set(this.scale);
		temp.rotation = this.rotation;
		temp.isHidden = this.isHidden;
		return temp;
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		position.setZero();
		width = 0;
		height = 0;
		positionOrigin.setZero();
		origin.setZero();
		scale.set(1.0f, 1.0f);
		rotation = 0.0f;
		isHidden = false;
	}
}
