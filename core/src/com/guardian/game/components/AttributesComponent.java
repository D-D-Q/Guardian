package com.guardian.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.AttributesEnum;

/**
 * 角色属性组件
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
	public int ATK;
	
	/**
	 * 防御
	 */
	public int DEF;
	
	/**
	 * 命中
	 */
	public int HIT;
	
	/**
	 * 闪避
	 */
	public int AVD;
	
	/**
	 * 体力
	 */
	public int VIT;
	
	/**
	 * 修改某属性值
	 * @param type
	 * @param value 正加，负减
	 */
	public int update(AttributesEnum type, int value){

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
			case HIT:
				this.HIT += value;
				Log.info(this, "修改命中" + value);
				return this.HIT;
			case AVD:
				this.AVD += value;
				Log.info(this, "修改闪避" + value);
				return this.AVD;
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
		Lv = 0;
		ATK = 0;
		DEF = 0;
		HIT = 0;
		AVD = 0;
		VIT = 0;
	}
}
