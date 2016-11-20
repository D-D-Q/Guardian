package com.guardian.game.entity.dao;

import java.lang.reflect.Constructor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.game.core.Assets;
import com.game.core.GlobalInline;
import com.game.core.ai.HierarchicalStateMachines;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CollisionComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.ScriptComponent;
import com.game.core.component.SkillsComponent;
import com.game.core.component.TextureComponent;
import com.game.core.component.TransformComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.script.EntityScript;
import com.guardian.game.ai.CharacterState;
import com.guardian.game.ai.HeroState;
import com.guardian.game.animation.CharacterAnimation;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.skills.NormalAttack;
import com.guardian.game.util.AtlasUtil;

/**
 * 生产指定实体
 * 
 * TODO 实体纹理和实体CharactersTemplate的预加载和释放
 * 
 * @author D
 * @date 2016年9月16日 上午8:00:39
 */
public class EntityDao {
	
	/**
	 * 创建角色实体
	 * 
	 * @param template
	 * @return
	 */
	public Entity createHeroEntity(CharactersTemplate template, float positionX, float positionY){
		
		Entity entity = new Entity();
		entity.flags = 1; // 设置成有效
		
		StateComponent stateComponent = new StateComponent();
		stateComponent.entityState = new HierarchicalStateMachines(HeroState.idle); // 第一个参数(owner)在该组件添加到实体的时候赋值, 查看EntityManager类。
		stateComponent.entity = entity;
		entity.add(stateComponent);
		
		TextureComponent textureComponent = new TextureComponent();
		textureComponent.enableVitBar();
		textureComponent.vitBar.setSize(template.spriteWidth, 1); // 高是无效值
		entity.add(textureComponent);
		
		AnimationComponent animationComponent = new AnimationComponent();
		Array<Sprite> frames = Assets.instance.getFrames(template.fileName); // 所有动画帧
		int index = 0;
		animationComponent.addAnimation(CharacterAnimation.idle, AtlasUtil.stateAnimation(frames, index, template.idleFrames));
		index += 5 * template.idleFrames;
		animationComponent.addAnimation(CharacterAnimation.run, AtlasUtil.stateAnimation(frames, index, template.runFrames));
		index += 5 * template.runFrames;
		Animation[] animations = AtlasUtil.stateAnimation(frames, index, template.attackFrames);
		animationComponent.addAnimation(CharacterAnimation.attack, animations);
		animationComponent.entity = entity;
		entity.add(animationComponent);
		
		TransformComponent transformComponent = new TransformComponent();
		transformComponent.width = frames.get(0).getWidth();
		transformComponent.height = frames.get(0).getHeight();
		transformComponent.offsetX = template.offsetX;
		transformComponent.offsetY = template.offsetY;
		transformComponent.index_z = 100;
		transformComponent.spriteHeight = template.spriteHeight;
		transformComponent.spriteWidth = template.spriteWidth;
		transformComponent.position.set(positionX, positionY); // 初始化位置, z是绘制优先级
		entity.add(transformComponent);
		
		AttributesComponent attributesComponent = new AttributesComponent(); // 变量属性信息
		attributesComponent.name = template.name;
		attributesComponent.Lv = template.Lv;
		attributesComponent.ATK = template.ATK;
		attributesComponent.ASPD = template.ASPD;
		attributesComponent.DEF = template.DEF;
		attributesComponent.AGI = template.AGI;
		attributesComponent.maxVit = template.VIT;
		attributesComponent.curVit = template.VIT;
		attributesComponent.moveSpeed = template.speed;
		entity.add(attributesComponent);
		
		if(template.ATKRange != 0 || template.ATKDistance != 0){
			CombatComponent combatComponent = new CombatComponent();
			for(Orientation direction : Orientation.values()){
				combatComponent.attackTextureRegion[direction.value] = animations[direction.value].getKeyFrames()[template.attackFrameIndex]; // 攻击事件的关键帧
			}
			combatComponent.combatState = HeroState.combat_attack;
			combatComponent.ATKRange = template.ATKRange;
			combatComponent.ATKDistance = template.ATKDistance;
			combatComponent.campBits = template.campBits;
			combatComponent.campMaskBits = template.campMaskBits;
			entity.add(combatComponent);
		}
		
//		MessageComponent messageComponent = new MessageComponent();
//		messageComponent.message = template.message;
//		entity.add(messageComponent);
		
		CharacterComponent characterComponent = new CharacterComponent();
		if(template.characterRadius == 0)
			characterComponent.radius = template.spriteWidth/2;
		else
			characterComponent.radius = template.characterRadius;
		characterComponent.entity = entity;
		entity.add(characterComponent);
		
		if(template.collisionRadius != 0){
			CollisionComponent collisionComponent = new CollisionComponent();
			collisionComponent.radius = template.collisionRadius;
			entity.add(collisionComponent);
		}
		
		ScriptComponent scriptComponent = new ScriptComponent();
		try {
			scriptComponent.message = template.message;
			
			Class<?> scriptClass = ClassReflection.forName(template.script);
			Constructor<?> constructor = scriptClass.getConstructor();
			scriptComponent.script =  (EntityScript) constructor.newInstance();
			scriptComponent.script.entity = entity;
			entity.add(scriptComponent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SkillsComponent skillsComponent = new SkillsComponent();
		skillsComponent.addSkill(NormalAttack.getInstance());
		skillsComponent.curSkill = NormalAttack.getInstance();
		skillsComponent.entity = entity;
		entity.add(skillsComponent);
		
		Assets.instance.finishLoading();
		
		return entity;
	}

	/**
	 * 创建角色实体
	 * 
	 * @param template
	 * @return
	 */
	public Entity createCharactersEntity(CharactersTemplate template, float positionX, float positionY){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		
		Entity entity = ashleyManager.engine.createEntity();
		entity.flags = 1; // 设置成有效
		
		StateComponent stateComponent = ashleyManager.engine.createComponent(StateComponent.class);
		stateComponent.entityState = new HierarchicalStateMachines(CharacterState.idle);
		stateComponent.entity = entity;
		entity.add(stateComponent);
		
		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
		textureComponent.enableVitBar();
		textureComponent.vitBar.setSize(template.spriteWidth, 1); // 高是无效值
		entity.add(textureComponent);
		
		AnimationComponent animationComponent = ashleyManager.engine.createComponent(AnimationComponent.class);
		Array<Sprite> frames = Assets.instance.getFrames(template.fileName); // 所有动画帧
		int index = 0;
		animationComponent.addAnimation(CharacterAnimation.idle, AtlasUtil.stateAnimation(frames, index, template.idleFrames));
		index += 5 * template.idleFrames;
		animationComponent.addAnimation(CharacterAnimation.run, AtlasUtil.stateAnimation(frames, index, template.runFrames));
		index += 5 * template.runFrames;
		Animation[] animations = AtlasUtil.stateAnimation(frames, index, template.attackFrames);
		animationComponent.addAnimation(CharacterAnimation.attack, animations);
		animationComponent.entity = entity;
		entity.add(animationComponent);
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.width = frames.get(0).getWidth();
		transformComponent.height = frames.get(0).getHeight();
		transformComponent.offsetX = template.offsetX;
		transformComponent.offsetY = template.offsetY;
		transformComponent.index_z = 100;
		transformComponent.spriteHeight = template.spriteHeight;
		transformComponent.spriteWidth = template.spriteWidth;
		transformComponent.position.set(positionX, positionY); // 初始化位置, z是绘制优先级
		entity.add(transformComponent);
		
		AttributesComponent attributesComponent = ashleyManager.engine.createComponent(AttributesComponent.class); // 变量属性信息
		attributesComponent.name = template.name;
		attributesComponent.Lv = template.Lv;
		attributesComponent.ATK = template.ATK;
		attributesComponent.ASPD = template.ASPD;
		attributesComponent.DEF = template.DEF;
		attributesComponent.AGI = template.AGI;
		attributesComponent.maxVit = template.VIT;
		attributesComponent.curVit = template.VIT;
		attributesComponent.moveSpeed = template.speed;
		entity.add(attributesComponent);
		
		if(template.ATKRange != 0 || template.ATKDistance != 0){
			CombatComponent combatComponent = ashleyManager.engine.createComponent(CombatComponent.class);
			for(Orientation direction : Orientation.values()){
				combatComponent.attackTextureRegion[direction.value] = animations[direction.value].getKeyFrames()[template.attackFrameIndex]; // 攻击事件的关键帧
			}
			combatComponent.combatState = CharacterState.combat_attack;
			combatComponent.ATKRange = template.ATKRange;
			combatComponent.ATKDistance = template.ATKDistance;
			combatComponent.campBits = template.campBits;
			combatComponent.campMaskBits = template.campMaskBits;
			entity.add(combatComponent);
		}
		
//		MessageComponent messageComponent = ashleyManager.engine.createComponent(MessageComponent.class);
//		messageComponent.message = template.message;
//		entity.add(messageComponent);
		
		CharacterComponent characterComponent = ashleyManager.engine.createComponent(CharacterComponent.class);
		if(template.characterRadius == 0)
			characterComponent.radius = template.spriteWidth/2;
		else
			characterComponent.radius = template.characterRadius;
		characterComponent.entity = entity;
		entity.add(characterComponent);
		
		if(template.collisionRadius != 0){
			CollisionComponent collisionComponent = ashleyManager.engine.createComponent(CollisionComponent.class);
			collisionComponent.radius = template.collisionRadius;
			entity.add(collisionComponent);
		}
		
		ScriptComponent scriptComponent = ashleyManager.engine.createComponent(ScriptComponent.class);
		try {
			scriptComponent.message = template.message;
			
			Class<?> scriptClass = ClassReflection.forName(template.script);
			Constructor<?> constructor = scriptClass.getConstructor();
			scriptComponent.script =  (EntityScript) constructor.newInstance();
			scriptComponent.script.entity = entity;
			entity.add(scriptComponent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SkillsComponent skillsComponent = ashleyManager.engine.createComponent(SkillsComponent.class);
		skillsComponent.addSkill(NormalAttack.getInstance());
		skillsComponent.curSkill = NormalAttack.getInstance();
		skillsComponent.entity = entity;
		entity.add(skillsComponent);
		
		PathfindingComponent pathfindingComponent = ashleyManager.engine.createComponent(PathfindingComponent.class);
		pathfindingComponent.entity = entity;
		entity.add(pathfindingComponent);
		
		Assets.instance.finishLoading();
		
		return entity;
	}
}
