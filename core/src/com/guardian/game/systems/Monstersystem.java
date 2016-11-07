package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.game.core.Assets;
import com.game.core.manager.AshleyManager;
import com.guardian.game.GAME;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.tools.MapperTools;

/**
 * 出怪系统
 * 
 * @author D
 * @date 2016年10月17日
 */
public class Monstersystem extends EntitySystem {
	
	/**
	 * 出怪间隔, 单位秒
	 * 批间隔3秒。波间隔30秒
	 */
	private float interval;
	
	/**
	 * 未执行的流逝时间
	 */
	private float accumulator;
	
	/**
	 * 总波数
	 */
	public int times = 30;
	
	/**
	 * 当前波数
	 */
	public int curTimes = 0;
	
	/**
	 * 每波的总批数
	 */
	public int tolat = 2;
	
	/**
	 * 当前批数
	 */
	public int cur = 0;

	public Monstersystem (int priority) {
		super(priority);
		this.interval = 3;
		this.accumulator = 0;
	}

	@Override
	public final void update (float deltaTime) {
		accumulator += deltaTime;

		while (accumulator >= interval) {
			accumulator -= interval;
			updateInterval();
		}
	}

	/**
	 * 生成波数和BOSS
	 * TODO 还未做
	 */
	protected void updateInterval (){
		
		// 最后一批
		if(cur >= tolat){
			// 最后一波
			if(curTimes >= times){
				this.setProcessing(false);
			}
			else{
				cur = 0;
				interval = 10; // 波数间隔秒数
			}
			return;
		}
		// 第一批
		else if(cur == 0){
			interval = 3; // 批次间隔秒数
			++curTimes;
		}
		++cur;
		
		CharactersTemplate data2Template = Assets.instance.get(GameScreenAssets.data2, CharactersTemplate.class);
		CharactersTemplate data3Template = Assets.instance.get(GameScreenAssets.data3, CharactersTemplate.class);
		
		Entity entity = AshleyManager.instance.entityDao.createCharactersEntity(data2Template, 520, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.instance.entityDao.createCharactersEntity(data3Template, 1040, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.instance.entityDao.createCharactersEntity(data2Template, 1560, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
	}
}
