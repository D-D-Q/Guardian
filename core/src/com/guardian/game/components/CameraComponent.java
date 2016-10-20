package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.guardian.game.GameConfig;

/**
 * 相机组件
 * 
 * @author D
 * @date 2016年9月16日 上午7:13:25
 */
public class CameraComponent implements Component, Poolable{
	
	/**
	 * 相机 
	 */
	public OrthographicCamera camera;
	
	/**
	 * 相机的游戏可视窗口，适配分辨率管理
	 */
	public Viewport viewport;
	
	/**
	 * 默认值
	 */
	public CameraComponent() {
		camera = new OrthographicCamera(); // 默认正交相机
		viewport = new FillViewport(GameConfig.width, GameConfig.hieght, camera); // 默认扩大显示
//		viewport.setScreenPosition(screenX, screenY);
	}
	
	/**
	 * 使用相机
	 */
	public void apply(SpriteBatch batch){
//		viewport.apply(); // 如果有多个viewport可以设置当前应用哪个。内部会调用camera.update();更新相机数据
		camera.update(); // 更新相机数据
		batch.setProjectionMatrix(camera.combined); // 相机信息设置给SpriteBatch，否则相机无用
	}
	
	@Override
	public void reset() {
		camera = null;
		viewport = null;
	}
}
