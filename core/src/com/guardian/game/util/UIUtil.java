package com.guardian.game.util;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class UIUtil {

	/**
	 * 复制Image对象
	 * 
	 * @param image
	 * @return
	 */
	public static Image cloneImage(Image image){
		
		Image clone = new Image(image.getDrawable());
		clone.setName(image.getName());
		
		return clone;
	}
}
