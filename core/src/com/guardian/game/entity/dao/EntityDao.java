package com.guardian.game.entity.dao;

import java.lang.reflect.Constructor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.game.core.Assets;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CollisionComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.MessageComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.ScriptComponent;
import com.game.core.component.SkillsComponent;
import com.game.core.component.TextureComponent;
import com.game.core.component.TransformComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.script.EntityScript;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.components.StateComponent.States;
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
	public Entity createHeroEntity(CharactersTemplate template, int positionX, int positionY){
		
		Entity entity = AshleyManager.instance.engine.createEntity();
		entity.flags = 1; // 设置成有效
		
		StateComponent stateComponent = AshleyManager.instance.engine.createComponent(StateComponent.class);
		entity.add(stateComponent);
		
		TextureComponent textureComponent = AshleyManager.instance.engine.createComponent(TextureComponent.class);
		textureComponent.enableVitBar();
		textureComponent.vitBar.setSize(template.spriteWidth, 1); // 高是无效值
		entity.add(textureComponent);
		
		AnimationComponent animationComponent = AshleyManager.instance.engine.createComponent(AnimationComponent.class);
		Array<Sprite> frames = Assets.instance.getFrames(template.fileName); // 所有动画帧
		int index = 0;
		animationComponent.addAnimation(States.idle, AtlasUtil.stateAnimation(frames, index, template.idleFrames));
		index += 5 * template.idleFrames;
		animationComponent.addAnimation(States.run, AtlasUtil.stateAnimation(frames, index, template.runFrames));
		index += 5 * template.runFrames;
		Animation[] animations = AtlasUtil.stateAnimation(frames, index, template.attackFrames);
		animationComponent.addAnimation(States.attack, animations);
		entity.add(animationComponent);
		
		TransformComponent transformComponent = AshleyManager.instance.engine.createComponent(TransformComponent.class);
		transformComponent.width = frames.get(0).getWidth();
		transformComponent.height = frames.get(0).getHeight();
		transformComponent.offsetX = template.offsetX;
		transformComponent.offsetY = template.offsetY;
		transformComponent.index_z = 100;
		transformComponent.spriteHeight = template.spriteHeight;
		transformComponent.spriteWidth = template.spriteWidth;
		transformComponent.position.set(positionX, positionY); // 初始化位置, z是绘制优先级
		entity.add(transformComponent);
		
		AttributesComponent attributesComponent = AshleyManager.instance.engine.createComponent(AttributesComponent.class); // 变量属性信息
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
			CombatComponent combatComponent = AshleyManager.instance.engine.createComponent(CombatComponent.class);
			for(Orientation direction : Orientation.values()){
				combatComponent.attackTextureRegion[direction.value] = animations[direction.value].getKeyFrames()[template.attackFrameIndex]; // 攻击事件的关键帧
			}
			combatComponent.ATKRange = template.ATKRange;
			combatComponent.ATKDistance = template.ATKDistance;
			combatComponent.campBits = template.campBits;
			combatComponent.campMaskBits = template.campMaskBits;
			entity.add(combatComponent);
		}
		
		MessageComponent messageComponent = AshleyManager.instance.engine.createComponent(MessageComponent.class);
		messageComponent.message = template.message;
		entity.add(messageComponent);
		
		CharacterComponent characterComponent = AshleyManager.instance.engine.createComponent(CharacterComponent.class);
		if(template.characterRadius == 0)
			characterComponent.radius = template.spriteWidth/2;
		else
			characterComponent.radius = template.characterRadius;
		entity.add(characterComponent);
		
		if(template.collisionRadius != 0){
			CollisionComponent collisionComponent = AshleyManager.instance.engine.createComponent(CollisionComponent.class);
			collisionComponent.radius = template.collisionRadius;
			entity.add(collisionComponent);
		}
		
		ScriptComponent scriptComponent = AshleyManager.instance.engine.createComponent(ScriptComponent.class);
		try {
			Class<?> scriptClass = ClassReflection.forName(template.script);
			Constructor<?> constructor = scriptClass.getConstructor();
			scriptComponent.script =  (EntityScript) constructor.newInstance();
			entity.add(scriptComponent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SkillsComponent skillsComponent = AshleyManager.instance.engine.createComponent(SkillsComponent.class);
		skillsComponent.addSkill(NormalAttack.getInstance());
		skillsComponent.curSkill = NormalAttack.getInstance();
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
	public Entity createCharactersEntity(CharactersTemplate template, int positionX, int positionY){
		
		Entity entity = createHeroEntity(template, positionX, positionY);
		
		PathfindingComponent pathfindingComponent = AshleyManager.instance.engine.createComponent(PathfindingComponent.class);
		entity.add(pathfindingComponent);
		
		Assets.instance.finishLoading();
		
		return entity;
	}
}
