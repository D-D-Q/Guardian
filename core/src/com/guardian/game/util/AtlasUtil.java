package com.guardian.game.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.logs.Log;

public class AtlasUtil {

	/**
	 * 因为Texture packer打包造成的旋转和位移，进行变换修正
	 * 
	 * @param atlasRegion
	 */
	public static TransformComponent correct(AtlasRegion atlasRegion, TransformComponent transformComponent){
		Log.debug("AtlasUtil", "correct");
		
		// 是否被旋转了
		if(atlasRegion.rotate){
			transformComponent.rotation -= 90f;
		}
		
		// 正面站立动画
		if(atlasRegion.name.startsWith("1")){
			
			// 保证动画所有帧对齐的位置是原图的左下角。帧绘制位置不同，但保证所有帧的锚点在屏幕上的位置一致（重合），这样才能保证统一旋转不错位。
			if(atlasRegion.rotate){
				
				// 因为打包逆时针旋转了90度过，所以原左下角现在是右下角位置。先左移动，值是旋转后的宽度（原高度）
				transformComponent.position.x -= atlasRegion.packedWidth;
				
				// 移动锚点到原位置，现在右下角位置。因为绘制位置变，锚点就会跟着移动，要保证锚点在屏幕上的位置不变			
				transformComponent.origin.x = atlasRegion.packedWidth;
				
				// 所有帧加上底部被去掉的空白，这样底部就对齐了，然后减去固定51空白。保证渲染的时候底部是对齐的				
				transformComponent.position.y += (atlasRegion.offsetY - 51f);
				
				// 绘制位置变了  但是该帧的锚点在屏幕上的位置不能变，这样旋转之后所有帧才能继续对齐。
				transformComponent.origin.y -= (atlasRegion.offsetY - 51f);
			}
			else{
				// 所有帧加上底部被去掉的空白，这样底部就对齐了，然后减去固定51空白。保证渲染的时候底部是对齐的
				transformComponent.position.y += (atlasRegion.offsetY - 51f);
				transformComponent.origin.y -= (atlasRegion.offsetY - 51f);  // 锚点保持原屏幕位置
			}
			
			// 帧有点偏了 修正下
			if(atlasRegion.name.equals("100.png.pvr")){
				transformComponent.position.x += -2.3f;
				transformComponent.origin.x -= -2.3f; // 锚点保持原屏幕位置
			}

			// 帧有点偏了 修正下			
			if(atlasRegion.name.equals("102.png.pvr") || atlasRegion.name.equals("103.png.pvr") || atlasRegion.name.equals("104.png.pvr")){
				transformComponent.position.y += -1f;
				transformComponent.origin.y -= -1f; // 锚点保持原屏幕位置
			}
		}
		
		return transformComponent;
	}
}
