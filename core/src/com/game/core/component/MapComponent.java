package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.GameConfig;
import com.guardian.game.logs.Log;

/**
 * 地图组件
 * 
 * @author D
 * @date 2016年9月15日 下午10:11:11
 */
public class MapComponent implements Component, Poolable{
	
	public TiledMap map; 
	
	public OrthogonalTiledMapRenderer renderer;
	
	/**
	 * 地图宽（像素）
	 */
	public static float width;
	
	/**
	 * 地图高（像素）
	 */
	public static float height;
	
	/**
	 * 小地图缩放倍数
	 */
	public static float miniMapScale;
	
	/**
	 * 初始化地图
	 * @param map
	 */
	public void init(TiledMap map, SpriteBatch batch){
		this.map = map;
		this.renderer = new OrthogonalTiledMapRenderer(map, 1f, batch); // 地图绘制，第二个参数缩放，比如要游戏1像素=地图16像素，就是缩小地图16倍，可以传1/16f
		
		TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(0);
		width = mapLayer.getWidth() * GameConfig.tileSize;
		height = mapLayer.getHeight() * GameConfig.tileSize;
		
		miniMapScale = Math.min(GameConfig.miniMapSize/width, GameConfig.miniMapSize/height);
		
	};
	
	/**
	 * 绘制
	 */
	public void render(CameraComponent cameraComponent){
		renderer.setView(cameraComponent.camera);
		renderer.render();
	}
	
	/**
	 * 绘制小地图
	 */
	public void renderMini(CameraComponent cameraComponent){
		/*
		 * cameraComponent.viewport.getScreenX()和 cameraComponent.viewport.getScreenY()
		 * 是viewport缩放之后的偏移量，因为缩放之后居中，所以偏移量是真正缩放差/2
		 * 比如cameraComponent.viewport.getScreenX()是-1, 那么程序中绘制0实际对当前坐标就是绘制的-1
		 * abs(cameraComponent.viewport.getScreenX())就是屏幕两边多出的距离
		 */
		
		// 备份
		float zoom = cameraComponent.camera.zoom;
		Vector3 position = cameraComponent.camera.position.cpy();
		
		// 修改
		cameraComponent.camera.zoom = zoom/miniMapScale;
//		cameraComponent.camera.position.x = Gdx.graphics.getWidth() - GameConfig.miniMapSize;
//		cameraComponent.camera.position.y = Gdx.graphics.getHeight() - GameConfig.miniMapSize;
		cameraComponent.camera.position.x = cameraComponent.camera.viewportWidth - GameConfig.miniMapSize/2;
		cameraComponent.camera.position.y = cameraComponent.camera.viewportHeight - GameConfig.miniMapSize/2;
		cameraComponent.camera.update();
		
		render(cameraComponent);
		
		// 还原
		cameraComponent.camera.zoom = zoom;
		cameraComponent.camera.position.set(position);
		cameraComponent.camera.update();
	}

	@Override
	public void reset() {
		map = null; // 资源管理器dispose
		renderer.dispose();
		renderer = null;
	}
}
