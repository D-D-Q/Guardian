package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;

/**
 * 战斗组件
 * 
 * @author D
 * @date 2016年9月20日 下午8:50:01
 */
public class CombatComponent implements Component, Poolable{
	
	/**
	 * 攻击范围传感器刚体，系统赋值
	 */
	public Body rangeBody;
	
	/**
	 * 攻击范围半径
	 * 可获得攻击目标的距离
	 */
	public float ATKRange;
	
	/**
	 * 攻击距离传感器刚体，系统赋值
	 */
	public Body distanceBody;
	
	/**
	 * 攻击距离半径
	 * 可真实攻击目标的距离
	 */
	public float ATKDistance;
	
	/**
	 * 攻击范围内的所有目标
	 */
	public Array<Entity> rangeTargets = new Array<>(false, 1);
	
	/**
	 * 攻击距离内的所有目标
	 */
	public Array<Entity> distanceTargets = new Array<>(false, 1);
	
	/**
	 * 当前目标
	 */
	public Entity target;
	
	/**
	 * 标记本次动画是否已经发送过攻击消息
	 */
	public boolean isSendAttackMessage = false;
	
	/**
	 * 8个方向的攻击帧
	 */
	public TextureRegion[] attackTextureRegion = new TextureRegion[8];
	
	/**
	 * 当前目标是否能攻击
	 * @return
	 */
	public boolean IsdistanceTarget(){
		
		if(target == null)
			return false;
		
		if(distanceTargets.contains(target, true))
			return true;
		
		return false;
	}
	
	@Override
	public void reset() {
		PhysicsManager.disposeBody(rangeBody);
		ATKRange = 0f;
		PhysicsManager.disposeBody(distanceBody);
		ATKDistance = 0f;
		rangeTargets.clear();
		distanceTargets.clear();
		target = null;
		isSendAttackMessage = false;
		for(TextureRegion textureRegion: attackTextureRegion)
			textureRegion = null;
	}
}
