package com.guardian.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.guardian.game.data.template.CharactersTemplate;

/**
 * 加载角色数据
 * 
 * @author D
 * @date 2016年10月21日
 */
public class CharacterTemplateLoader extends AsynchronousAssetLoader<CharactersTemplate, CharacterTemplateLoader.Parameters> {
	
	protected CharactersTemplate charactersTemplate;

	public CharacterTemplateLoader(FileHandleResolver resolver) {
		
		super(resolver);
	}

	/**
	 * 异步加载
	 */
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
		
	}

	/**
	 * 加载之后返回
	 */
	@Override
	public CharactersTemplate loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
			
		CharactersTemplate charactersTemplate = this.charactersTemplate;
		this.charactersTemplate = null; // 返回之后不保留引用
		
		return charactersTemplate;
	}

	/**
	 * 加载依赖资源，卸载的时候也会跟着卸载
	 * 应该也是异步
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter) {
		
		charactersTemplate = new Json().fromJson(CharactersTemplate.class, file);
		
		Array<AssetDescriptor> dependencies = new Array<>(1);
		dependencies.add(new AssetDescriptor<>(charactersTemplate.fileName, TextureAtlas.class));
		
		return dependencies;
	}
	
	public static class Parameters extends AssetLoaderParameters<CharactersTemplate> {
	}

}
