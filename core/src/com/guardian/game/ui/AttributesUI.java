package com.guardian.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	
	private Table table;
	
	private Label lv_label;
	private Label attr_label;
	private Label atk_label;
	private Label aspd_label;
	private Label def_label;
	private Label agi_label;
	private Label vit_label;

	public AttributesUI(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("AttributesUI");
//		this.setVisible(false);
//		this.setFillParent(true);
//		this.pad(GameConfig.UIpad, GameConfig.UIpad, GameConfig.UIpad + 90,GameConfig.UIpad); 
		this.bottom();
		
		table = new Table(); // 属性面板表格
		this.add(table).expandX().fillX();
		table.setDebug(GameConfig.UIdebug);
		table.pad(15);
		table.defaults().height(70).space(10);
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
		
		table.add(new Label(i18NBundle.get("Lv"), skin)).left();
		lv_label = new Label(String.valueOf(attributesComponent.Lv) , skin);
		table.add(lv_label).width(100).right();
		table.add(new Label(i18NBundle.get("Statpoints"), skin)).left();
		attr_label = new Label(String.valueOf(attributesComponent.curAttrs) , skin);
		table.add(attr_label).expandX().right();
		
		table.row().spaceTop(15);
		table.add(new Label(i18NBundle.get("ATK"), skin)).left();
		atk_label = new Label(String.format("%.0f", attributesComponent.ATK) , skin);
		table.add(atk_label).right();
		table.add(new Label("" , skin)).left();
		Button ATKButton = new Button(skin, GameScreenAssets.button1);
		ATKButton.setName("ATKButton");
		ATKButton.addListener(new UpButtonListener(AttributesEnum.ATK));
		table.add(ATKButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("ASPD"), skin)).left();
		aspd_label = new Label(String.format("%.3f", attributesComponent.ASPD) + "/" + i18NBundle.get("/s"), skin);
		table.add(aspd_label).right();
		table.add(new Label("" , skin)).left();
		Button ASPDButton = new Button(skin, GameScreenAssets.button1);
		ASPDButton.setName("ATKButton");
		ASPDButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
				if(attributesComponent.curAttrs <= 0)
					return;
				
				attributesComponent.curAttrs -= 1;
				attributesComponent.ASPD += 0.004;
			}
		});
		table.add(ASPDButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("DEF"), skin)).left();
		def_label = new Label(String.format("%.0f", attributesComponent.DEF) , skin);
		table.add(def_label).right();
		table.add(new Label("" , skin)).left();
		Button DEFButton = new Button(skin, GameScreenAssets.button1);
		DEFButton.setName("DEFButton");
		DEFButton.addListener(new UpButtonListener(AttributesEnum.DEF));
		table.add(DEFButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("AGI"), skin)).left();
		agi_label = new Label(String.format("%.0f", attributesComponent.AGI) , skin);
		table.add(agi_label).right();
		table.add(new Label("" , skin)).left();
		Button HITButton = new Button(skin, GameScreenAssets.button1);
		HITButton.setName("AGIButton");
		HITButton.addListener(new UpButtonListener(AttributesEnum.AGI));
		table.add(HITButton).right();
		
		table.row();
		table.add(new Label(i18NBundle.get("VIT"), skin)).left();
		vit_label = new Label(String.format("%.0f", attributesComponent.curVit) + "/" + String.format("%.0f", attributesComponent.maxVit) , skin);
		table.add(vit_label).right();
		table.add(new Label("" , skin)).left();
		Button VITButton = new Button(skin, GameScreenAssets.button1);
		VITButton.setName("VITButton");
		VITButton.addListener(new UpButtonListener(AttributesEnum.VIT));
		table.add(VITButton).right();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
		
		lv_label.setText(String.valueOf(attributesComponent.Lv));
		attr_label.setText(String.valueOf(attributesComponent.curAttrs));
		atk_label.setText(String.format("%.0f", attributesComponent.ATK));
		aspd_label.setText(String.format("%.0f", attributesComponent.ASPD));
		def_label.setText(String.format("%.0f", attributesComponent.DEF));
		agi_label.setText(String.format("%.0f", attributesComponent.AGI));
		vit_label.setText(String.format("%.0f", attributesComponent.curVit) + "/" + String.format("%.0f", attributesComponent.maxVit));
		
		super.draw(batch, parentAlpha);
	}
}
