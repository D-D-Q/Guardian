package com.guardian.game.entity.dao;

import java.lang.reflect.Constructor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.game.core.component.ScriptComponent;
import com.game.core.manager.AshleyManager;
import com.game.core.script.EntityScript;
import com.guardian.game.GuardianGame;
import com.guardian.game.components.AnimationComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.CharacterComponent;
import com.guardian.game.components.CollisionComponent;
import com.guardian.game.components.CombatComponent;
import com.guardian.game.components.MessageComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.StateComponent.Orientation;
import com.guardian.game.components.StateComponent.States;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.components.TransformComponent;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.util.AtlasUtil;

/**
 * 生产指定实体
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
	public Entity createCharactersEntity(CharactersTemplate template, int positionX, int positionY){
		
		Entity entity = AshleyManager.engine.createEntity();
		entity.flags = 1; // 设置成有效
		
		StateComponent stateComponent = AshleyManager.engine.createComponent(StateComponent.class);
		
		TextureComponent textureComponent = AshleyManager.engine.createComponent(TextureComponent.class);
		
		AnimationComponent animationComponent = AshleyManager.engine.createComponent(AnimationComponent.class);
		Array<Sprite> frames = GuardianGame.game.assets.getFrames(template.fileName); // 所有动画帧
		int index = 0;
		animationComponent.addAnimation(States.idle, AtlasUtil.stateAnimation(frames, index, template.idleFrames));
		index += 5 * template.idleFrames;
		animationComponent.addAnimation(States.run, AtlasUtil.stateAnimation(frames, index, template.runFrames));
		index += 5 * template.runFrames;
		Animation[] animations = AtlasUtil.stateAnimation(frames, index, template.attackFrames);
		animationComponent.addAnimation(States.attack, animations);
		
		TransformComponent transformComponent = AshleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.init(frames.get(0).getWidth(), frames.get(0).getHeight(), template.offsetX, template.offsetY, 100); // 初始化画布大小和锚点
//		transformComponent.setMapPosition(positionX, positionY); // 初始化tile位置, z是绘制优先级
		transformComponent.position.set(positionX, positionY, transformComponent.position.z); // 初始化位置, z是绘制优先级
		
		AttributesComponent attributesComponent = AshleyManager.engine.createComponent(AttributesComponent.class); // 变量属性信息
		attributesComponent.name = template.name;
		attributesComponent.Lv = template.Lv;
		attributesComponent.ATK = template.ATK;
		attributesComponent.DEF = template.DEF;
		attributesComponent.HIT = template.HIT;
		attributesComponent.AVD = template.AVD;
		attributesComponent.VIT = template.VIT;
		attributesComponent.speed = template.speed;
		
		if(template.ATKRange != 0 || template.ATKDistance != 0){
			CombatComponent combatComponent = AshleyManager.engine.createComponent(CombatComponent.class);
			for(Orientation direction : Orientation.values()){
				combatComponent.attackTextureRegion[direction.value] = animations[direction.value].getKeyFrames()[template.attackFrameIndex]; // 攻击事件的关键帧
			}
			combatComponent.ATKRange = template.ATKRange;
			combatComponent.ATKDistance = template.ATKDistance;
			combatComponent.campBits = template.campBits;
			combatComponent.campMaskBits = template.campMaskBits;
			entity.add(combatComponent);
		}
		
		MessageComponent messageComponent = AshleyManager.engine.createComponent(MessageComponent.class);
		messageComponent.message = template.message;
		
		CharacterComponent characterComponent = AshleyManager.engine.createComponent(CharacterComponent.class);
		characterComponent.radius = template.characterRadius;
		
		if(template.collisionRadius != 0){
			CollisionComponent collisionComponent = AshleyManager.engine.createComponent(CollisionComponent.class);
			collisionComponent.radius = template.collisionRadius;
			entity.add(collisionComponent);
		}
		
		ScriptComponent scriptComponent = AshleyManager.engine.createComponent(ScriptComponent.class);
		try {
			Class<?> scriptClass = ClassReflection.forName(template.script);
			Constructor<?> constructor = scriptClass.getConstructor();
			scriptComponent.script =  (EntityScript) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		entity.add(transformComponent);
		entity.add(stateComponent);
		entity.add(textureComponent);
		entity.add(animationComponent);
		entity.add(attributesComponent);
		
		entity.add(messageComponent);
		entity.add(characterComponent);
		
		entity.add(scriptComponent);
		
		GuardianGame.game.assets.assetManager.finishLoading();
		
		return entity;
	}
}
