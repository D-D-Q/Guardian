package com.guardian.game.assets.parameter;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

/**
 * ttf字体加载参数
 * 
 * @author D
 * @date 2016年9月13日 上午6:32:40
 */
public class FzstkFontParameter<BitmapFont> extends FreeTypeFontLoaderParameter {

	public FzstkFontParameter() {
		
		this.fontFileName = "fonts/FZSTK.TTF"; // ttf字体的资源路径和文件名
		
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32; // 字体大小

		// 设置要显示的中文
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "等级伤害防御命中闪避体力";
		this.fontParameters = parameter;
	}
}
