package com.game.core.system;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CameraComponent;
import com.game.core.component.MapComponent;
import com.game.core.component.TextureComponent;
import com.game.core.component.TransformComponent;
import com.guardian.game.GAME;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

/**
 * 渲染系统
 * 渲染精灵的每一帧
 * 
 * @author D
 * @date 2016年8月28日 上午10:57:35
 */
public class RenderingSystem extends SortedIteratingSystem {
	
	/**
	 * 地图
	 */
	private MapComponent mapComponent;
	
	/**
	 * 显示效果
	 */
	private Stage subtitleStage;
	
	public RenderingSystem(int priority) {
		
		super(FamilyTools.renderingF, new Comparator<Entity>(){
			@Override
	        public int compare(Entity e1, Entity e2) {// 降序排列，后面的后绘制在上层。 返回正数绘制e1在上,返回负数绘制e2在上
				
				TransformComponent e1TransformComponent = MapperTools.transformCM.get(e1);
				TransformComponent e2TransformComponent = MapperTools.transformCM.get(e2);
				
				int y = (int)Math.signum(e2TransformComponent.position.y - e1TransformComponent.position.y); // 先按y轴算
				if(y == 0)
					return (int)Math.signum(e1TransformComponent.index_z - e2TransformComponent.index_z); // 再按z抽算
				return y;
	        }
		}, priority);
		
		CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
		subtitleStage = new Stage(cameraComponent.viewport, GAME.batch);
	}
	
	@Override
	public void update(float deltaTime) {
		
		CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
		cameraComponent.update(GAME.batch);  // 更新相机数据，并设置相机数据给batch
		
		mapComponent = MapperTools.mapCM.get(GAME.screenEntity);
		if(mapComponent != null){
			mapComponent.render(cameraComponent); // 把更新数据了的相机，设置给地图显示使用
			mapComponent.renderMiniBegin(cameraComponent); // 小地图
		}
		
		forceSort(); // 绘制排序
		
		GAME.batch.begin();
		
		super.update(deltaTime);
		
		GAME.batch.end();
		
		// 字幕
		subtitleStage.act();
		subtitleStage.draw();
		
		// 地图
		if(mapComponent != null){
			mapComponent.renderMiniEnd();
		}
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Log.debug(this, "processEntity");
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		if(transformComponent.isHidden)
			return;
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		if(textureComponent.textureRegion == null){
			Log.info(this, "textureRegion is null");
			return;
		}
		
		if(textureComponent.textureRegion instanceof Sprite){ // 绘制精灵
			Sprite sprite = (Sprite)textureComponent.textureRegion;
			sprite.setPosition(transformComponent.getRenderPositionX(), transformComponent.getRenderPositionY());
			sprite.draw(GAME.batch);
		}
		else{  // 绘制纹理
	        /*
	         *  TextureRegion region, 绘制纹理
	         *  float x, float y, 绘制位置，已左下角为原点，该位置是指纹理的左下角的要在位置
	         *  float originX, float originY, 设置锚点，值是相对于原点（纹理左下角）的位置
	         *  float width, float height, 纹理宽高
	         *  float scaleX, float scaleY, 缩放，1是原始大小。从锚点向四周缩放
	         *  float rotation, 旋转，正数是逆时针。以锚点为圆心旋转
	         */
			GAME.batch.draw(textureComponent.textureRegion, 
					transformComponent.getRenderPositionX(), transformComponent.getRenderPositionY(), 
					transformComponent.origin.x, transformComponent.origin.y,
					transformComponent.getWidth(), transformComponent.getHeight(),
					transformComponent.scale.x, transformComponent.scale.y,
					transformComponent.rotation);
		}
		
		if(textureComponent.vitBar.isVisible()){
			AttributesComponent attributesComponent = MapperTools.attributesCM.get(entity);
			textureComponent.vitBar.setPosition(transformComponent.position.x - transformComponent.spriteWidth/2, transformComponent.position.y + transformComponent.spriteHeight);
			textureComponent.vitBar.setRange(0, attributesComponent.maxVit);
			textureComponent.vitBar.setValue(attributesComponent.curVit);
//			textureComponent.vitBar.act(deltaTime);
//			textureComponent.vitBar.draw(GAME.batch, 1);
			subtitleStage.addActor(textureComponent.vitBar);
		}
		
		if(mapComponent != null){
			if(entity == GAME.hero)
				mapComponent.miniDraw(Color.WHITE, transformComponent.position.x, transformComponent.position.y);
			else
				mapComponent.miniDraw(Color.RED, transformComponent.position.x, transformComponent.position.y);
		}
		
		AnimationComponent animationComponent = MapperTools.animationCM.get(entity);
		while(animationComponent.subtitle.size != 0)
			subtitleStage.addActor(animationComponent.subtitle.pop());
		
	}
}
