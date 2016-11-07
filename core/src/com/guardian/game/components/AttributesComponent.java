package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.AttributesEnum;

/**
 * 角色属性组件
 * 
 * 力量: 和对方力量计算得基本攻击力和防御力. 
 * 		因为力量价值太高，要在计算速度和敏捷时起到负作用
 * 速度: 攻击速度
 * 敏捷: 和对方敏捷计算得命中率
 * 体力: 生命
 * 
 * 武器: 增加计算后的攻击力,暂称为外部攻击力
 * 装备: 增加计算后的防御力,暂称为外部攻击力
 * 
 * @author D
 * @date 2016年9月8日 下午8:57:33
 */
public class AttributesComponent implements Component, Poolable{
	
	/**
	 * 名称
	 */
	public String name;
	
	/**
	 * 等级
	 */
	public int Lv;
	
	/**
	 * 攻击力
	 */
	public float ATK;
	
	/**
	 * 攻击速度
	 */
	public float ASPD;
	
	/**
	 * 防御力
	 */
	public float DEF;
	
	/**
	 * 敏捷 影响命中和闪避
	 */
	public float AGI;
	
	/**
	 * 最大体力
	 */
	public float maxVit;
	
	/**
	 * 当前体力
	 */
	public float curVit;
	
	/**
	 * 移动速度
	 */
	public float moveSpeed;
	
	/**
	 * 升级所需经验
	 * TODO 暂固定为10
	 */
	public int levelUpExp = 10;
	
	/**
	 * 当前经验
	 */
	public int curExp;
	
	/**
	 * 当前剩余属性点
	 */
	public int curAttrs;
	
	/**
	 * 修改某属性值
	 * @param type
	 * @param value 正加，负减
	 */
	public float update(AttributesEnum type, int value){

		switch (type) {
			case Lv:
				this.Lv += value;
				Log.info(this, "修改等级" + value);
				return this.Lv;
			case ATK:
				this.ATK += value;
				Log.info(this, "修改伤害" + value);
				return this.ATK;
			case DEF:
				this.DEF += value;
				Log.info(this, "修改防御" + value);
				return this.DEF;
			case AGI:
				this.AGI += value;
				Log.info(this, "修改敏捷" + value);
				return this.AGI;
			case VIT:
				this.maxVit += value * 10; // 1属性10体力
				Log.info(this, "修改体力" + value);
				return this.maxVit;
	
			default:
				return 0;
		}
	}
	
	@Override
	public void reset() {
		name = null;
		moveSpeed = 0;
		Lv = 0;
		ATK = 0;
		DEF = 0;
		AGI = 0;
		maxVit = 0;
		curVit = 0;
		
		levelUpExp = 0;
		curExp = 0;
		curAttrs = 0;
	}
}
