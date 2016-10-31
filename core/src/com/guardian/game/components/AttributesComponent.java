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
	 * 攻击
	 */
	public float ATK;
	
	/**
	 * 防御
	 */
	public float DEF;
	
	/**
	 * 敏捷 影响命中和闪避
	 */
	public float AGI;
	
	/**
	 * 体力
	 */
	public float VIT;
	
	/**
	 * 移动速度
	 */
	public float speed;
	
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
				this.VIT += value;
				Log.info(this, "修改体力" + value);
				return this.VIT;
	
			default:
				return 0;
		}
	}
	
	@Override
	public void reset() {
		name = null;
		Lv = 0;
		ATK = 0;
		DEF = 0;
		AGI = 0;
		VIT = 0;
		speed = 0;
	}
}
