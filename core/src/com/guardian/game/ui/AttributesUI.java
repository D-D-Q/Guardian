package com.guardian.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.assets.GameScreenAssets;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.tools.AttributesEnum;
import com.guardian.game.tools.MapperTools;
import com.guardian.game.ui.event.UpButtonListener;

/**
 * 属性UI
 * 
 * @author D
 * @date 2016年9月11日 上午11:51:20
 */
public class AttributesUI extends Table {
	
	public Table table;

	public AttributesUI(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("AttributesUI");
		this.setVisible(false);
		this.setFillParent(true);
		this.pad(GameConfig.UIpad, GameConfig.UIpad, GameConfig.UIpad + 90,GameConfig.UIpad); 
		this.bottom();
		
		table = new Table(); // 属性面板表格
		this.add(table).expandX().fillX();
		table.setDebug(GameConfig.UIdebug);
		table.pad(15);
		table.defaults().height(70).space(10);
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
		
		table.add(new Label(i18NBundle.get("Lv"), skin)).left();
		table.add(new Label(String.valueOf(attributesComponent.Lv) , skin)).width(100).right();
		table.add().left();
		table.add().expandX().right();
		
		table.row().spaceTop(15);
		table.add(new Label(i18NBundle.get("ATK"), skin)).left();
		table.add(new Label(String.valueOf(attributesComponent.ATK) , skin)).right();
		table.add(new Label("+10" , skin)).left();
		Button ATKButton = new Button(skin, GameScreenAssets.button1);
		ATKButton.setName("ATKButton");
		ATKButton.addListener(new UpButtonListener(AttributesEnum.ATK));
		table.add(ATKButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("DEF"), skin)).left();
		table.add(new Label(String.valueOf(attributesComponent.DEF) , skin)).right();
		table.add(new Label("+10" , skin)).left();
		Button DEFButton = new Button(skin, GameScreenAssets.button1);
		DEFButton.setName("DEFButton");
		DEFButton.addListener(new UpButtonListener(AttributesEnum.DEF));
		table.add(DEFButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("AGI"), skin)).left();
		table.add(new Label(String.valueOf(attributesComponent.AGI) , skin)).right();
		table.add(new Label("+10" , skin)).left();
		Button HITButton = new Button(skin, GameScreenAssets.button1);
		HITButton.setName("AGIButton");
		HITButton.addListener(new UpButtonListener(AttributesEnum.AGI));
		table.add(HITButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("VIT"), skin)).left();
		table.add(new Label(String.valueOf(attributesComponent.maxVit) , skin)).right();
		table.add(new Label("+10" , skin)).left();
		Button VITButton = new Button(skin, GameScreenAssets.button1);
		VITButton.setName("VITButton");
		VITButton.addListener(new UpButtonListener(AttributesEnum.VIT));
		table.add(VITButton).right();
	}
}
