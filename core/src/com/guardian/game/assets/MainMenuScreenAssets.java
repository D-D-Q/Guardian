package com.guardian.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import com.game.core.annotation.Asset;

public class MainMenuScreenAssets {

	@Asset(I18NBundle.class)
	public final static String i18NBundle = "i18n/MainMenuScreenMessage";
	
	@Asset(Texture.class)
	public final static String login = "badlogic.jpg";
}
