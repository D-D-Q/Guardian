package com.guardian.game.tools;

import com.badlogic.ashley.core.Family;
import com.guardian.game.components.AnimationComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.components.TransformComponent;

/**
 * family集合工具
 * 
 * @author D
 * @date 2016年8月28日 下午8:40:28
 */
public class FamilyTools {

	public static final Family animationF = Family.all(StateComponent.class, TransformComponent.class, AnimationComponent.class, TextureComponent.class).get();
	public static final Family renderingF = Family.all(TextureComponent.class, TransformComponent.class).get();
	public static final Family itemF = Family.all(TextureComponent.class, TransformComponent.class).get();
}
