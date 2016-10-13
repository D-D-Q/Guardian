package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 战斗检查组件
 * 
 * @author D
 * @date 2016年9月20日 下午8:50:01
 */
public class CombatComponent implements Component, Poolable{
	
	/**
	 * 8个方向的攻击帧
	 */
	public TextureRegion[] attackTextureRegion = new TextureRegion[8];
	
	/**
	 * 目标
	 */
	public Entity targetEntity;
	
	/**
	 * 标记本次动画是否已经发送过攻击消息
	 */
	public boolean isSendAttackMessage = false;
	
	// 警戒范围
	
	// 攻击范围

	@Override
	public void reset() {
		
		for(TextureRegion textureRegion: attackTextureRegion)
			textureRegion = null;
		
		targetEntity = null;
	}
}
