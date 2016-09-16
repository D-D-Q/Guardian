package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;

/**
 * 纹理资源，指定加载参数类
 * 
 * @author D
 * @date 2016年9月13日 下午9:10:03
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssetTexture {

	Class<? extends TextureParameter> value();
}