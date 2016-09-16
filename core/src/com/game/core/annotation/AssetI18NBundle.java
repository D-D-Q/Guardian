package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter;

/**
 * I18NBundle国际化资源，指定加载参数类
 * 
 * @author D
 * @date 2016年9月13日 下午9:08:07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssetI18NBundle {

	Class<? extends I18NBundleParameter> value();
}
