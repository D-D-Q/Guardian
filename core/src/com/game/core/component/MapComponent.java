package com.game.core.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.guardian.game.GameConfig;
import com.guardian.game.logs.Log;

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
	public float width;
	
	/**
	 * 地图高（像素）
	 */
	public float height;
	
	/**
	 * 小地图
	 */
	private Texture miniMap;
	
	/**
	 * 小地图缩放倍数
	 */
	public float miniMapScale;
	
	/**
	 * 小地图的上的屏幕框大小 
	 */
	private float miniScreenWidth;
	private float miniScreenHeight;
	
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
		this.renderer = new OrthogonalTiledMapRenderer(map, 1f, batch); // 地图绘制，第二个参数缩放，比如要游戏1像素=地图16像素，就是缩小地图16倍，可以传1/16f
		this.miniMap = miniMap;
		
		TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(0);
		width = mapLayer.getWidth() * GameConfig.tileSize;
		height = mapLayer.getHeight() * GameConfig.tileSize;
		
		miniMapScale = Math.min(miniMap.getWidth()/width, miniMap.getHeight()/height);
		miniScreenWidth = GameConfig.width * miniMapScale;
		miniScreenHeight = GameConfig.hieght * miniMapScale;
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	};
	
	/**
	 * 绘制
	 */
	public void render(Viewport viewport){
		renderer.setView((OrthographicCamera)viewport.getCamera());
		renderer.render();
	}
	
	/**
	 * 开始绘制小地图，右上角
	 */
	public void renderMiniBegin(Viewport viewport){
		/*
		 * viewport.getScreenX()和 viewport.getScreenY()
		 * 是viewport缩放之后的偏移量，因为缩放之后居中，所以偏移量是真正缩放差/2
		 * 比如viewport.getScreenX()是-1, 那么程序中绘制0实际对当前坐标就是绘制的-1
		 * abs(viewport.getScreenX())就是屏幕两边多出的距离
		 */
		Camera camera = viewport.getCamera();
		
		// 地图绘制的位置
		miniMapX = camera.position.x + (camera.viewportWidth/2 + viewport.getScreenX() - miniMap.getWidth());
		miniMapY = camera.position.y + (camera.viewportHeight/2 + viewport.getScreenY() - miniMap.getHeight());
		
		renderer.getBatch().begin();
		renderer.getBatch().draw(miniMap, miniMapX, miniMapY);
		renderer.getBatch().end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		// 当前相机位置映射到小地图的位置
		float centerX = camera.position.x * miniMapScale;
		float centerY = camera.position.y * miniMapScale;
		
		// 绘制屏幕映射
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(miniMapX + (centerX - miniScreenWidth/2), miniMapY + (centerY - miniScreenHeight/2), miniScreenWidth, miniScreenHeight);
		
		shapeRenderer.set(ShapeType.Filled);
	}
	
	public void renderMiniEnd(){
		
		
		
		
		shapeRenderer.end();
	}
	
	/**
	 * 位置在小地图的点
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
