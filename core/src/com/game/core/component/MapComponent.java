package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	
	/**
	 * 大地图绘制
	 */
	private OrthogonalTiledMapRenderer renderer;
	
	/**
	 * 地图宽（像素）
	 */
	public static float width;
	
	/**
	 * 地图高（像素）
	 */
	public static float height;
	
	/**
	 * 小地图
	 */
	private Texture miniMap;
	
	/**
	 * 小地图缩放倍数
	 */
	public static float miniMapScale;
	
	/**
	 * 小地图绘制
	 */
	private ShapeRenderer shapeRenderer;
	
	/**
	 * 保存小地图绘制的位置
	 */
	private float miniMapX, miniMapY;
	
	/**
	 * 初始化地图, 必须调用
	 * @param map
	 */
	public void init(TiledMap map, Texture miniMap, SpriteBatch batch){
		this.miniMap = miniMap;
		this.renderer = new OrthogonalTiledMapRenderer(map, 1f, batch); // 地图绘制，第二个参数缩放，比如要游戏1像素=地图16像素，就是缩小地图16倍，可以传1/16f
		
		TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(0);
		width = mapLayer.getWidth() * GameConfig.tileSize;
		height = mapLayer.getHeight() * GameConfig.tileSize;
		
		miniMapScale = Math.min(GameConfig.miniMapSize/width, GameConfig.miniMapSize/height);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	};
	
	/**
	 * 绘制
	 */
	public void render(CameraComponent cameraComponent){
		renderer.setView(cameraComponent.camera);
		renderer.render();
	}
	
	/**
	 * 开始绘制小地图，右上角
	 */
	public void renderMiniBegin(CameraComponent cameraComponent){
		/*
		 * cameraComponent.viewport.getScreenX()和 cameraComponent.viewport.getScreenY()
		 * 是viewport缩放之后的偏移量，因为缩放之后居中，所以偏移量是真正缩放差/2
		 * 比如cameraComponent.viewport.getScreenX()是-1, 那么程序中绘制0实际对当前坐标就是绘制的-1
		 * abs(cameraComponent.viewport.getScreenX())就是屏幕两边多出的距离
		 */
		miniMapX = cameraComponent.camera.position.x + (cameraComponent.camera.viewportWidth/2 + cameraComponent.viewport.getScreenX() - GameConfig.miniMapSize);
		miniMapY = cameraComponent.camera.position.y + (cameraComponent.camera.viewportHeight/2 + cameraComponent.viewport.getScreenY() - GameConfig.miniMapSize);
		renderer.getBatch().begin();
		renderer.getBatch().draw(miniMap, miniMapX, miniMapY);
		renderer.getBatch().end();
		
		shapeRenderer.setProjectionMatrix(cameraComponent.camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
	}
	
	public void renderMiniEnd(){
		shapeRenderer.end();
	}
	
	/**
	 * 位置小地图点
	 * 
	 * @param color
	 * @param x 在大地图的位置
	 * @param y 在大地图的位置
	 */
	public void miniDraw(Color color, float x, float y){
		shapeRenderer.setColor(color);
		shapeRenderer.circle(miniMapX + x * miniMapScale, miniMapY + y * miniMapScale, 2);
	}

	@Override
	public void reset() {
		miniMap = null;
		renderer.dispose();
		renderer = null;
	}
}
