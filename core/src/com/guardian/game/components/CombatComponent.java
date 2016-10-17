package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.game.core.manager.PhysicsManager;
import com.guardian.game.tools.MapperTools;

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
	 * 阵营
	 * 取值 1, 2, 4, 8, ...。默认0x00000001
	 */
	public int campBits = 0x00000001;
	
	/**
	 * 阵营掩码，可攻击的阵营和
	 * 例如:1|4|8 = 13, 默认-1全部攻击
	 */
	public int campMaskBits = -1; 
	
	/**
	 * 攻击范围内的所有目标
	 */
	public Array<Entity> rangeTargets = new Array<>(false, 1);
	
	/**
	 * 攻击距离内的所有目标
	 */
	public Array<Entity> distanceTargets = new Array<>(false, 1);
	
	/**
	 * 当前攻击目标
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
	 * 当前目标是否在攻击距离之内
	 * @return
	 */
	public boolean IsDistanceTarget(){
		
		if(target == null)
			return false;
		
		if(this.target.flags == 0){ // 目标Entity已销毁
			this.target = null;
			return false;
		}
		
		if(distanceTargets.contains(target, true))
			return true;
		
		return false;
	}
	
	/**
	 * 是否是能攻击的阵营目标
	 * 
	 * @param target
	 * @return
	 */
	public boolean isCampTarget(Entity target){
		
		if(target == null || target.flags == 0)
			return false;
		
		CombatComponent targetCombatComponent = MapperTools.combatCM.get(target);
		if(targetCombatComponent == null) // 没有战斗组件
			return false;
		if((campMaskBits & targetCombatComponent.campBits) < 1) // 不能攻击的阵营
			return false;
		
		return true;
	}
	
	/**
	 * 目前目标，是否是能攻击的阵营目标
	 * 
	 * @param target
	 * @return 目前基本必true
	 */
	public boolean isCampTarget(){
		
		if(this.target.flags == 0){ // 目标Entity已销毁
			this.target = null;
			return false;
		}
		return isCampTarget(this.target);
	}
	
	/**
	 * 进入攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void enterATKRange(Contact contact, Entity target) {
		
		if(!isCampTarget(target))
			return;
		
		rangeTargets.add(target);
		
		if(target == null) // 设置为当前目标
			this.target = target;
	}
	
	/**
	 * 离开攻击范围
	 * 
	 * @param contact 碰撞类
	 * @param target 目标实体
	 */
	public void leaveATKRange(Contact contact, Entity target) {
		
		if(!isCampTarget(target))
			return;
		
		rangeTargets.removeValue(target, true);
		
		if(this.target == target){ // 设置新目标
			this.target = distanceTargets.size == 0 ? 
					(rangeTargets.size == 0 ? null : rangeTargets.first()) : 
						distanceTargets.first(); 
		}
	}
	
	/**
	 * 进入攻击距离
	 * 
	 * @param contact
	 * @param target
	 */
	public void enterATKDistance(Contact contact, Entity target) {
		
		if(!isCampTarget(target))
			return;
		
		distanceTargets.add(target);

		if(this.target == null || !IsDistanceTarget()){ // 更换攻击目标，谁直接能打到优先打谁，先不考虑追击
			this.target = target;
		}
	}
	
	/**
	 * 离开攻击距离
	 * 
	 * @param contact
	 * @param target
	 */
	public void leaveATKDistance(Contact contact, Entity target) {
		
		if(!isCampTarget(target))
			return;
		
		distanceTargets.removeValue(target, true);
		
		// 设置新目标
		if(this.target == target){
			this.target = distanceTargets.size == 0 ? target : distanceTargets.first(); 
		}
	}
	
	@Override
	public void reset() {
		PhysicsManager.disposeBody(rangeBody);
		ATKRange = 0f;
		PhysicsManager.disposeBody(distanceBody);
		ATKDistance = 0f;
		campBits = 0x0001;
		campMaskBits = -1;
		rangeTargets.clear();
		distanceTargets.clear();
		target = null;
		isSendAttackMessage = false;
		for(TextureRegion textureRegion: attackTextureRegion)
			textureRegion = null;
	}
}
