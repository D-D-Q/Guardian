package com.guardian.game.ui.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.guardian.game.GAME;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.logs.Log;
import com.guardian.game.tools.AttributesEnum;
import com.guardian.game.tools.MapperTools;

public class UpButtonListener extends ClickListener {
	
	private AttributesEnum type;
	
	public UpButtonListener(AttributesEnum type) {
		this.type = type;
	}

	@Override
	public void clicked(InputEvent event, float x, float y) {
		
		Actor target = event.getTarget();
		
		Log.info(this, "属性提升事件" + target.getName());
		
		Table table = (Table)target.getParent();
		Cell<Actor> targetCell = table.getCell(target);
		Cell cell = table.getCells().get(targetCell.getRow() * table.getColumns() + 1);// 属性值列的index是1
		Label label = (Label)cell.getActor(); 
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
		label.setText(String.valueOf(attributesComponent.update(type, 1)));
	}
}
