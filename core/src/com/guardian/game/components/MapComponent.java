package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.GameConfig;

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
	public int width;
	
	/**
	 * 地图高（像素）
	 */
	public int height;
	
	/**
	 * 初始化地图
	 * @param map
	 */
	public void init(TiledMap map, SpriteBatch batch){
		this.map = map;
		this.renderer = new OrthogonalTiledMapRenderer(map, 1f, batch); // 地图绘制，第二个参数缩放，比如要游戏1像素=地图16像素，就是缩小地图16倍，可以传1/16f
		
		TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(0);
		width = mapLayer.getWidth() * GameConfig.tile;
		height = mapLayer.getHeight() * GameConfig.tile;
	};
	
	/**
	 * 绘制
	 */
	public void render(OrthographicCamera camera){
		renderer.setView(camera);
		renderer.render();
	}

	@Override
	public void reset() {
		map = null; // 资源管理器dispose
		renderer.dispose();
		renderer = null;
	}
}
