package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

/**
 * ttf字体资源，指定参数类
 * 
 * @author D
 * @date 2016年9月13日 下午9:02:20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssetTTFFont {
	
	Class<? extends FreeTypeFontLoaderParameter> value();
}
