package com.guardian.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.game.core.annotation.Asset;
import com.guardian.game.data.template.CharactersTemplate;

/**
 * 加载游戏窗口资源
 * @author D
 * @date 2016年9月11日 下午9:14:13
 */
public class GameScreenAssets {
	
	@Asset(BitmapFont.class)
	public final static String font0 = "fonts/fzstk.fnt"; 
	
	@Asset(I18NBundle.class)
	public final static String i18NBundle = "i18n/GameScreenMessage";
	
	@Asset(TiledMap.class)
	public final static String map = "map/map.tmx";
	@Asset(Texture.class)
	public final static String miniMap = "map/miniMap.png";
	
	// DATA资源
//	@Asset(TextureAtlas.class)
//	public final static String yx = "res/yx.atlas"; // 英雄
	@Asset(CharactersTemplate.class)
	public final static String data1 = "data/data1.json"; // 英雄
	
	@Asset(Skin.class)
	public final static String default_skin = "skin/defaultUI.json"; // UI皮肤会其他资源都加载之完成之后才能加载
	
	// UI资源的key
	public final static String button1 = "button1"; // 主按钮
	public final static String button2 = "button2"; // 生熟悉按钮
	public final static String equipped1 = "equipped1"; // 装备栏1默认样式，单手武器
	public final static String equipped2 = "equipped2"; // 装备栏1默认样式，单手盾
	public final static String equipped3 = "equipped3"; // 装备栏1默认样式，双手武器
	public final static String equipped4 = "equipped4"; // 装备栏1默认样式，衣服
	public final static String equipped5 = "equipped5"; // 装备栏1默认样式，裤子
	public final static String equipped6 = "equipped6"; // 装备栏1默认样式，裤子
	public final static String item0 = "item0"; // 物品栏默认样式
	public final static String item1 = "item1";
}
