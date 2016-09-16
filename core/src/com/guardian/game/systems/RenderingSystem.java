package com.guardian.game.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.guardian.game.GAME;
import com.guardian.game.GuardianGame;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.components.MapComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.util.AtlasUtil;

/**
 * 渲染系统
 * 渲染精灵的每一帧
 * 
 * @author D
 * @date 2016年8月28日 上午10:57:35
 */
public class RenderingSystem extends SortedIteratingSystem {
	
	public final GuardianGame game;
	
	public RenderingSystem(GuardianGame guardianGame) {
		// TODO 系统调用优先级写死了
		super(FamilyTools.renderingF, new Comparator<Entity>(){
			@Override
	        public int compare(Entity e1, Entity e2) {
	            return (int)Math.signum(MapperTools.transformCM.get(e1).position.z - MapperTools.transformCM.get(e2).position.z);
	        }
		}, 1);
		
		this.game = guardianGame;
	}
	
	@Override
	public void update(float deltaTime) {
		
		CameraComponent cameraComponent = MapperTools.cameraCM.get(GAME.screenEntity);
		cameraComponent.apply(game.batch);  // 更新相机数据，并设置相机数据给batch
		
		MapComponent mapComponent = MapperTools.mapCM.get(GAME.screenEntity);
		mapComponent.render(cameraComponent.camera); // 把更新数据了的相机，设置给地图显示使用
		
		game.batch.begin();
		super.update(deltaTime);
		game.batch.end();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Log.debug(this, "processEntity");
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		if(transformComponent.isHidden)
			return;
		
		TextureRegion textureRegion = MapperTools.textureCM.get(entity).textureRegion;
		
		// 修正变换
		if(textureRegion instanceof TextureAtlas.AtlasRegion)
			transformComponent = AtlasUtil.correct((TextureAtlas.AtlasRegion)textureRegion, 
					transformComponent.copyTo(game.engine.createComponent(TransformComponent.class)));
		
        /*
         *  TextureRegion region, 绘制纹理
         *  float x, float y, 绘制位置，已左下角为原点，该位置是指纹理的左下角的要在位置
         *  float originX, float originY, 设置锚点，值是相对于原点（纹理左下角）的位置
         *  float width, float height, 纹理宽高
         *  float scaleX, float scaleY, 缩放，1是原始大小。从锚点向四周缩放
         *  float rotation, 旋转，正数是逆时针。以锚点为圆心旋转
         */
		game.batch.draw(textureRegion, 
				transformComponent.position.x - transformComponent.positionOrigin.x, // 设置纹理显示锚点在绘制位置 
				transformComponent.position.y - transformComponent.positionOrigin.y, // 设置纹理显示锚点在绘制位置
				transformComponent.origin.x, transformComponent.origin.y,
				textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
				transformComponent.scale.x, transformComponent.scale.y,
				transformComponent.rotation);
	}
	
}
