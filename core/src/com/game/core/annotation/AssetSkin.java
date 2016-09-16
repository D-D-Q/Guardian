package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;

/**
 * UI皮肤资源，指定加载参数类
 *  TODO 暂时不要使用, 因为资源如果使用资源管理器加载，又添加给皮肤管理器。
 *  会被资源管理器和皮肤管理器两个持有引用，销毁的时候会重复消耗两次，报异常。
 *  目前只处理了BitmapFont类型资源的重复
 *  
 *  
 * @author D
 * @date 2016年9月13日 下午9:02:20
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssetSkin {
	
	Class<? extends SkinParameter> value();
}

// 如果要使用此注解，可以给skin提前添加一些资源，注意必须保证这些资源已经加载完成
// 例如：给SkinParameter.resources属性设置字体资源如下

//FreeTypeFontParameter parameter = new FreeTypeFontParameter();
//parameter.size = 32; // 字体大小
//parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "等级伤害防御命中闪避体力"; // 设置要显示的中文

// 1、自己创建加载
//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FZSTK.TTF")); // 字体
//font = generator.generateFont(parameter);
//generator.dispose();
// 2、使用资源管理器加载，销毁会重复 
//FreeTypeFontLoaderParameter loaderParameter = new FreeTypeFontLoaderParameter();
//loaderParameter.fontFileName = "fonts/fzstk.ttf"; // 字体的资源路径和文件名
//loaderParameter.fontParameters = parameter;
//assetManager.load("fzstk.ttf", BitmapFont.class, loaderParameter); // 资源管理器加载并添加到资源管理,生成的字体名字。字体是同步加载的(会阻塞等待加载完)
//assetManager.finishLoadingAsset("fzstk.ttf");

// ObjectMap<String, Object> font = new ObjectMap<>();
// font.put("fzstk.ttf", assetManager.get("assetManager", BitmapFont.class));
// this(font); // 就是new SkinParameter(font);

// 注意：使用注解@AssetTTFFont(...)加载的字体资源会添加到使用@Asset(Skin.class)注解的UI皮肤中。
// 如果只有提前加入字体资源不必使用该注解