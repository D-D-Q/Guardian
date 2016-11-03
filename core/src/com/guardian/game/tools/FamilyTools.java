package com.guardian.game.tools;

import com.badlogic.ashley.core.Family;
import com.game.core.component.AnimationComponent;
import com.game.core.component.CharacterComponent;
import com.game.core.component.CollisionComponent;
import com.game.core.component.CombatComponent;
import com.game.core.component.PathfindingComponent;
import com.game.core.component.ScriptComponent;
import com.game.core.component.SkillsComponent;
import com.game.core.component.TextureComponent;
import com.game.core.component.TransformComponent;
import com.guardian.game.components.StateComponent;

/**
 * family集合工具
 * 
 * @author D
 * @date 2016年8月28日 下午8:40:28
 */
public class FamilyTools {

//	public static final Family itemF = Family.all(TextureComponent.class, TransformComponent.class).get();
	
	public static final Family generalF = Family.one(StateComponent.class, ScriptComponent.class).get();
	public static final Family animationF = Family.all(StateComponent.class, TransformComponent.class, AnimationComponent.class, TextureComponent.class).get();
	public static final Family renderingF = Family.all(TextureComponent.class, TransformComponent.class).get();
	public static final Family combatF = Family.all(TransformComponent.class, CombatComponent.class, CharacterComponent.class, StateComponent.class, SkillsComponent.class).get();
	public static final Family physicsF = Family.all(TransformComponent.class).one(CharacterComponent.class, CollisionComponent.class).get();
	public static final Family AIF = Family.all(PathfindingComponent.class, CharacterComponent.class).get();
	
}
