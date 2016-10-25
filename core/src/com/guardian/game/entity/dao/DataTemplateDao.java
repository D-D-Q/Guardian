package com.guardian.game.entity.dao;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.guardian.game.GAME;
import com.guardian.game.data.template.CharactersTemplate;
import com.guardian.game.logs.Log;

/**
 * 加载精灵配置数据
 * 已修改成使用Assets自定义加载
 * 
 * @author D
 * @date 2016年9月19日 下午9:36:29
 */
@Deprecated
public class DataTemplateDao {
	
	InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();

	/**
	 * 加载数据
	 */
	public void load(String fileName){
		Log.info(this, "load begin" + fileName);
		
		FileHandle fileHandle = fileHandleResolver.resolve(fileName);
		
		JsonReader jsonReader = new JsonReader();
		JsonValue root = jsonReader.parse(fileHandle); // 加载成数组
		
		Json json = new Json();
//		for(int i = 0, j =root.size; i<j; ++i)
//			GAME.charactersTemplate.add(json.readValue(CharactersTemplate.class, root.get(i))); // 转换成对象，缓存起来
	}
}
