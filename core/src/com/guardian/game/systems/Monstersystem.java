package com.guardian.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.game.core.Assets;
import com.game.core.GlobalInline;
import com.game.core.manager.AshleyManager;
import com.guardian.game.GAME;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.ui.GameUI;

/**
 * 出怪系统
 * 
 * @author D
 * @date 2016年10月17日
 * 
 * 
全局90分钟=5400s

30波
每波5400/30=180s=3分钟

90秒波间隔
90秒出怪时间
3秒间隔出怪
90/3=30个*3=90个
最少2秒一个怪才能在180秒呢清完90个
秒杀怪的话90秒左右能清完

怪的攻击力


3个boss
30m一个
10波 20波  30波

18个怪升1级
一波升级4-5


属性点占能力的40%
装备占能力的60%
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
				new MonsterConfig("data/data1.json", 0), // 寒冰卫士
				new MonsterConfig("data/data2.json"),
				new MonsterConfig("data/data3.json"), // 猴子
				new MonsterConfig("data/data4.json"),
				new MonsterConfig("data/data5.json"),
				new MonsterConfig("data/data6.json"),
				new MonsterConfig("data/data7.json"),
				new MonsterConfig("data/data8.json"),
				new MonsterConfig("data/data9.json"),
				new MonsterConfig("data/data10.json", 1, 0b010, ""),
				new MonsterConfig("data/data11.json"),
				new MonsterConfig("data/data12.json"),
				new MonsterConfig("data/data13.json"),
				new MonsterConfig("data/data14.json"),
				new MonsterConfig("data/data15.json"),
				new MonsterConfig("data/data16.json"),
				new MonsterConfig("data/data17.json"),
				new MonsterConfig("data/data18.json"),
				new MonsterConfig("data/data19.json"),
				new MonsterConfig("data/data20.json", 1, 0b010, ""),
				new MonsterConfig("data/data21.json"),
				new MonsterConfig("data/data22.json"),
				new MonsterConfig("data/data23.json"),
				new MonsterConfig("data/data24.json"),
				new MonsterConfig("data/data25.json"),
				new MonsterConfig("data/data26.json"),
				new MonsterConfig("data/data27.json"),
				new MonsterConfig("data/data28.json"),
				new MonsterConfig("data/data29.json"),
				new MonsterConfig("data/data30.json", 1, 0b010, ""),
		};
		
		// 预载入第一波
		Assets.instance.load(monsterConfig[0].monsterData, CharactersTemplate.class);
	}

	@Override
	public final void update (float deltaTime) {
		accumulator += deltaTime;

		if(curTimes == 0){
			GameUI.instance.next_time.setText(String.format("%02d", (int)(interval - accumulator)));
		}
		
		while (accumulator >= interval) {
			accumulator -= interval;
			if(updateInterval())
				break; // 防止卡住时间很长，出怪结束了还在循环
		}
	}

	/**
	 * 生成波数和BOSS
	 */
	protected boolean updateInterval (){
		
		// 当前波第一次
		if(curTimes == 0){
			Assets.instance.finishLoadingAsset(monsterConfig[curBatch].monsterData); // 必须完成当前波的载入
			interval = monsterConfig[curBatch].timesInterval; // 次间隔秒数
		}
		
		++curTimes;
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		CharactersTemplate dataTemplate = Assets.instance.get(monsterConfig[curBatch].monsterData, CharactersTemplate.class);
		
		if((monsterConfig[curBatch].location & 0b100) == 0b100){
			Entity entity = ashleyManager.entityDao.createCharactersEntity(dataTemplate, 520, 2080);
			ashleyManager.engine.addEntity(entity);
//			MapperTools.combatCM.get(entity).target = GlobalInline.instance.get("hero");
		}
		if((monsterConfig[curBatch].location & 0b010) == 0b010){
			Entity entity = ashleyManager.entityDao.createCharactersEntity(dataTemplate, 1040, 2080);
			ashleyManager.engine.addEntity(entity);
//			MapperTools.combatCM.get(entity).target = GlobalInline.instance.get("hero");
		}
		if((monsterConfig[curBatch].location & 0b001) == 0b001){
			Entity entity = ashleyManager.entityDao.createCharactersEntity(dataTemplate, 1560, 2080);
			ashleyManager.engine.addEntity(entity);
//			MapperTools.combatCM.get(entity).target = GlobalInline.instance.get("hero");
		}
		
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
				return true;
			}
		}
		return false;
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
		public float batchInterval = 90;
		
		/**
		 * 次数
		 */
		public int times = 30;
		
		/**
		 * 位置
		 * 中间就是0b010
		 */
		public int location = 0b111;
		
		/**
		 * 次数间隔，秒
		 */
		public float timesInterval = 2;
	
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
		
		public MonsterConfig(String monsterData, int times, int location, String msg) {
			this.monsterData = monsterData;
			this.times = times;
			this.location = location;
			this.msg = msg;
		}
		
		public MonsterConfig(String monsterData, float batchInterval, int times, int location, float timesInterval, String msg) {
			this.monsterData = monsterData;
			this.batchInterval = batchInterval;
			this.times = times;
			this.location = location;
			this.timesInterval = timesInterval;
			this.msg = msg;
		}
	}
}
