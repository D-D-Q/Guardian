package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ttf字体资源，指定参数类
 * FreeType的ttf字体在android下会加载失败
 * @author D
 * @date 2016年9月13日 下午9:02:20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface AssetTTFFont {
	
//	Class<? extends FreeTypeFontLoaderParameter> value();
}

//ttf字体加载参数
//public class FzstkFontParameter<BitmapFont> extends FreeTypeFontLoaderParameter {

//	public FzstkFontParameter() {
//		
//		this.fontFileName = "fonts/fzstk.ttf"; // ttf字体的资源路径和文件名
//		
//		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
//		parameter.size = 32; // 字体大小
//
//		// 设置要显示的中文
//		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "等级伤害防御命中闪避体力";
//		this.fontParameters = parameter;
//	}
//}

//@AssetTTFFont(FzstkFontParameter.class) // ttf资源，必须传递加载参数类设置参数
//public final static String font0 = "fzstk.ttf"; // 通过ttf生成的字体资源的名字。会在皮肤之前生成，可以在skin的json中通过该名字使用字体。必须.ttf结尾