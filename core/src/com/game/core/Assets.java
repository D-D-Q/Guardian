package com.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.game.core.annotation.Asset;
import com.guardian.game.assets.CharacterTemplateLoader;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.logs.Log;

/**
 * 所有资源管理
 * 所有资源，比如动画帧以左下角对齐
 * 
 * @author D
 * @date 2016年8月28日 下午8:48:16
 */
public class Assets extends AssetManager{
	
	public static Assets instance = new Assets();
	
	/**
	 * 动画帧缓存
	 */
	public ObjectMap<String, Array<Sprite>>  framesCache = new ObjectMap<String, Array<Sprite>>();
	
	/**
	 * 资源管理器和皮肤管理器中重复的资源
	 * 因为资源被资源管理器和皮肤管理器两个持有引用，所以销毁的时候会重复，报异常
	 */
//	private Array<String> repeated = new Array<String>(4);
	
	private Assets() {
		
		// FreeType在android下会加载失败
//		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assetManager.getFileHandleResolver())); // 设置ttf字体扩展的Loader
//		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(assetManager.getFileHandleResolver())); // 设置ttf字体扩展的Loader
		super.setLoader(TiledMap.class, new TmxMapLoader(super.getFileHandleResolver())); //设置Tiled编辑器地图
		super.setLoader(CharactersTemplate.class, new CharacterTemplateLoader(super.getFileHandleResolver())); // 角色数据
		
		Texture.setAssetManager(this); // 设置游戏切出切回时候，资源管理器可以管理纹理
	}
	
	/**
	 * 加载屏幕资源
	 * @throws Exception 
	 */
	public void loadAssets(Class<?> screen) throws Exception{
		
		ObjectMap<String, Object> resources = new ObjectMap<>(2); // 字体资源，皮肤和资源管理器都需要
		Field skin = null;
		
		// 不能用java的反射。用libgdx的反射实现， 这样可以支持GWT编译html。
		for(Field field : ClassReflection.getFields(screen)){
			if(field.isAnnotationPresent(Asset.class)){ // 普通资源
				Annotation annotation = field.getDeclaredAnnotation(Asset.class);
				Asset assetType = annotation.getAnnotation(Asset.class);
				if(assetType.value().equals(Skin.class)) {
					skin = field;
					continue;
				}
				super.load(field.get(null).toString(), assetType.value());
			}
//			else if(field.isAnnotationPresent(AssetTTFFont.class)){ // 指定参数的ttf字体资源
//				Annotation annotation = field.getDeclaredAnnotation(AssetTTFFont.class);
//				AssetTTFFont assetType = annotation.getAnnotation(AssetTTFFont.class);
//				String fileName = field.get(null).toString();
//				assetManager.load(fileName, BitmapFont.class, assetType.value().newInstance());
//				assetManager.finishLoadingAsset(fileName);
//				resources.put(fileName, assetManager.get(fileName, BitmapFont.class)); // 添加字体到皮肤资源
//				repeated.add(fileName); // 记录资源管理器和皮肤管理器的重复资源名
//			}
			// TODO 实现各种资源类型注解（含有指定加载参数类）的判断和指定参数的初始化，并加载该资源。比如AssetTexture、AssetTextureAtlas等注解。AssetTTFFont是已经实现了的
			
		}
		
		if(skin != null){ // 皮肤最后加载
			SkinParameter skinParameter = new SkinParameter(resources);
			super.load(skin.get(null).toString(), Skin.class, skinParameter);
		}
	
		super.update();
	}
	
	/**
	 * 卸载屏幕资源
	 * @throws Exception 
	 */
	public void disposeAssets(Class<?> screen) throws Exception{
		
		// 不能用java的反射。用libgdx的反射实现， 这样可以支持GWT编译html。
		for(Field field : ClassReflection.getFields(screen)){
			if(field.isAnnotationPresent(Asset.class)){ // 普通资源
				super.unload(field.get(null).toString());
			}
		}
		
		super.update();
	}
		 
	/**
	 * 获取帧数组
	 * 
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public Array<Sprite> getFrames(String fileName){
		
		Array<Sprite> sprites = framesCache.get(fileName);
		if(sprites == null){
			TextureAtlas textureAtlas = super.get(fileName, TextureAtlas.class);
			sprites = textureAtlas.createSprites(fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."))); // 这步很慢，如果频繁使用要缓存
			framesCache.put(fileName, sprites);
		}
		
		return sprites;
	}
	
	/**
	 * 销毁所有资源
	 */
	@Override
	public void dispose () {
		Log.info(this, "dispose begin");
		
		/* 移除皮肤管理器中和资源管理器中重复的资源，交给资源管理器销毁。以免销毁重复报异常 */
//		for(String fileName : assetManager.getAssetNames()){
//			Object object = assetManager.get(fileName);
//			if(object instanceof Skin){
//				Skin skin = (Skin)object;
//				for(String name : repeated){
//					
//				}
//				break;
//			}
//		}
		
		super.dispose();
	}
}
