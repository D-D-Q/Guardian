package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.game.core.manager.AshleyManager;
import com.guardian.game.GAME;
import com.guardian.game.GuardianGame;
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
	
	public int tolat = 10;
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
		
		if(cur > tolat)
			return;
		++cur;
		
		CharactersTemplate charactersTemplate = GuardianGame.game.assets.assetManager.get(GameScreenAssets.data2, CharactersTemplate.class);
		
		Entity entity = AshleyManager.entityDao.createCharactersEntity(charactersTemplate, 345, 2080);
		AshleyManager.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.entityDao.createCharactersEntity(charactersTemplate, 690, 2080);
		AshleyManager.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.entityDao.createCharactersEntity(charactersTemplate, 1038, 2080);
		AshleyManager.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.entityDao.createCharactersEntity(charactersTemplate, 1383, 2080);
		AshleyManager.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
	}
}
