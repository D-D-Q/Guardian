package com.guardian.game.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.guardian.game.components.AnimationComponent;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.components.CameraComponent;
import com.guardian.game.components.ItemComponent;
import com.guardian.game.components.MapComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.components.TextureComponent;
import com.guardian.game.components.TransformComponent;

/**
 * 组件映射工具
 * 
 * @author D
 * @date 2016年8月28日 下午8:40:47
 */
public class MapperTools {

	public static final ComponentMapper<CameraComponent> cameraCM = ComponentMapper.getFor(CameraComponent.class);
	public static final ComponentMapper<MapComponent> mapCM = ComponentMapper.getFor(MapComponent.class);
	
	public static final ComponentMapper<StateComponent> stateCM = ComponentMapper.getFor(StateComponent.class);
	public static final ComponentMapper<TransformComponent> transformCM = ComponentMapper.getFor(TransformComponent.class);;
	public static final ComponentMapper<AnimationComponent> animationCM = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<TextureComponent> textureCM = ComponentMapper.getFor(TextureComponent.class);
	public static final ComponentMapper<AttributesComponent> attributesCM = ComponentMapper.getFor(AttributesComponent.class);
	public static final ComponentMapper<ItemComponent> itemCM = ComponentMapper.getFor(ItemComponent.class);
}
