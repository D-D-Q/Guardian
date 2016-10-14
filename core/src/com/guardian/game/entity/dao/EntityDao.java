package com.guardian.game.entity.dao;

import java.lang.reflect.Constructor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.game.core.component.ScriptComponent;
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
import com.guardian.game.components.StateComponent.State;
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

	private GuardianGame game;
	
	public EntityDao(GuardianGame game) {
		this.game = game;
	}
	
	/**
	 * 创建角色实体
	 * 
	 * @param template
	 * @return
	 */
	public Entity createCharactersEntity(CharactersTemplate template, int positionX, int positionY){
		
		Entity entity = game.engine.createEntity();
		
		StateComponent stateComponent = game.engine.createComponent(StateComponent.class);
		
		TextureComponent textureComponent = game.engine.createComponent(TextureComponent.class);
		
		AnimationComponent animationComponent = game.engine.createComponent(AnimationComponent.class);
		Array<Sprite> frames = game.assets.getFrames(template.fileName); // 所有动画帧
		int index = 0;
		animationComponent.addAnimation(State.idle, AtlasUtil.stateAnimation(frames, index, template.idleFrames));
		index += 5 * template.idleFrames;
		animationComponent.addAnimation(State.run, AtlasUtil.stateAnimation(frames, index, template.runFrames));
		index += 5 * template.runFrames;
		Animation[] animations = AtlasUtil.stateAnimation(frames, index, template.attackFrames);
		animationComponent.addAnimation(State.attack, animations);
		
		TransformComponent transformComponent = game.engine.createComponent(TransformComponent.class);
		transformComponent.init(frames.get(0).getWidth(), frames.get(0).getHeight(), template.offsetX, template.offsetY, 100); // 初始化画布大小和锚点
//		transformComponent.setMapPosition(positionX, positionY); // 初始化tile位置, z是绘制优先级
		transformComponent.position.set(positionX, positionY, transformComponent.position.z); // 初始化位置, z是绘制优先级
		
		AttributesComponent attributesComponent = game.engine.createComponent(AttributesComponent.class); // 变量属性信息
		attributesComponent.name = template.name;
		attributesComponent.Lv = template.Lv;
		attributesComponent.ATK = template.ATK;
		attributesComponent.DEF = template.DEF;
		attributesComponent.HIT = template.HIT;
		attributesComponent.AVD = template.AVD;
		attributesComponent.VIT = template.VIT;
		
		CombatComponent combatComponent = game.engine.createComponent(CombatComponent.class);
		for(Orientation direction : Orientation.values()){
			combatComponent.attackTextureRegion[direction.value] = animations[direction.value].getKeyFrames()[template.attackFrameIndex]; // 攻击事件的关键帧
		}
		
		MessageComponent messageComponent = game.engine.createComponent(MessageComponent.class);
		messageComponent.message = template.message;
		
		CharacterComponent physicsComponent = game.engine.createComponent(CharacterComponent.class);
		physicsComponent.radius = template.physicsRadius;
		
		CollisionComponent collisionComponent = game.engine.createComponent(CollisionComponent.class);
		collisionComponent.radius = template.collisionRadius;
		
		ScriptComponent scriptComponent = game.engine.createComponent(ScriptComponent.class);
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
		entity.add(combatComponent);
		entity.add(messageComponent);
		entity.add(physicsComponent);
		entity.add(collisionComponent);
		entity.add(scriptComponent);
		
		return entity;
	}
}
