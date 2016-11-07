package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.game.core.Assets;
import com.game.core.manager.AshleyManager;
import com.guardian.game.GAME;
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
	 * 间隔, 秒
	 */
	private float interval;
	
	/**
	 * 未执行的流逝时间
	 */
	private float accumulator;

	/**
	 * 当前波数
	 */
	private int curBatch;
	
	/**
	 * 当前次数
	 */
	private int curTimes;
	
	/**
	 * 配置数组
	 */
	private MonsterConfig[] monsterConfig;

	public Monstersystem (int priority) {
		super(priority);
		this.interval = 0;
		this.accumulator = 0;
		this.curBatch = 0;
		this.curTimes= 0;
		
		monsterConfig = new MonsterConfig[]{
				new MonsterConfig("data/data2.json", 0), // 寒冰卫士
				new MonsterConfig("data/data3.json") // 猴子
		};
		
		// 预载入第一波
		Assets.instance.load(monsterConfig[0].monsterData, CharactersTemplate.class);
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
	 */
	protected void updateInterval (){
		
		// 当前波第一次
		if(curTimes == 0){
			Assets.instance.finishLoadingAsset(monsterConfig[curBatch].monsterData); // 必须完成当前波的载入
			interval = monsterConfig[curBatch].timesInterval; // 次间隔秒数
		}
		
		++curTimes;
		
		CharactersTemplate dataTemplate = Assets.instance.get(monsterConfig[curBatch].monsterData, CharactersTemplate.class);
		
		Entity entity = AshleyManager.instance.entityDao.createCharactersEntity(dataTemplate, 520, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.instance.entityDao.createCharactersEntity(dataTemplate, 1040, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		entity = AshleyManager.instance.entityDao.createCharactersEntity(dataTemplate, 1560, 2080);
		AshleyManager.instance.engine.addEntity(entity);
		MapperTools.combatCM.get(entity).target = GAME.hero;
		
		// 当前波数最后一次
		if(curTimes >= monsterConfig[curBatch].times){
			
			++curBatch;
			
			// 下一波预备
			if(curBatch < monsterConfig.length){
				curTimes = 0;
				interval = monsterConfig[curBatch].batchInterval;
				Assets.instance.load(monsterConfig[curBatch].monsterData, CharactersTemplate.class); // 预载入下一波
			}
			// 当前波是最后一波了，结束系统
			else{
				this.setProcessing(false);
			}
		}
	}
	
	/**
	 * 出怪配置
	 * 先分波数，每波分次数
	 * 
	 * @author D
	 * @date 2016年11月7日
	 */
	private class MonsterConfig{
		
		/**
		 * 角色数据
		 */
		public String monsterData;
		
		/**
		 * 距离上波间隔，秒
		 */
		public float batchInterval = 10;
		
		/**
		 * 次数
		 */
		public int times = 10;
		
		/**
		 * 次数间隔，秒
		 */
		public float timesInterval = 3;
	
		/**
		 * 开始时提示信息
		 */
		public String msg = "";
		
		public MonsterConfig(String monsterData) {
			this.monsterData = monsterData;
		}
		
		public MonsterConfig(String monsterData, float batchInterval) {
			this.monsterData = monsterData;
			this.batchInterval = batchInterval;
		}
		
		public MonsterConfig(String monsterData, float batchInterval, int times, float timesInterval, String msg) {
			this.monsterData = monsterData;
			this.batchInterval = batchInterval;
			this.times = times;
			this.timesInterval = timesInterval;
			this.msg = msg;
		}
	}
}
