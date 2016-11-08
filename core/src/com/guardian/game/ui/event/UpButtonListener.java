package com.guardian.game.ui.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.guardian.game.GAME;
import com.guardian.game.components.AttributesComponent;
import com.guardian.game.tools.AttributesEnum;
import com.guardian.game.tools.MapperTools;

/**
 * 属性提升事件
 * 
 * @author D
 * @date 2016年9月11日 上午11:51:20
 */
public class UpButtonListener extends ClickListener {
	
	private AttributesEnum type;
	
	public UpButtonListener(AttributesEnum type) {
		this.type = type;
	}

	@Override
	public void clicked(InputEvent event, float x, float y) {
		
		AttributesComponent attributesComponent = MapperTools.attributesCM.get(GAME.hero);
		if(attributesComponent.curAttrs <= 0)
			return;
		
		attributesComponent.curAttrs -= 1;
		attributesComponent.update(type, 1);
		
//		Actor target = event.getTarget();
//		
//		Table table = (Table)target.getParent();
//		Cell<Actor> targetCell = table.getCell(target);
//		Cell<?> cell = table.getCells().get(targetCell.getRow() * table.getColumns() + 1);// 属性值列的index是1
//		Label label = (Label)cell.getActor(); 
//		
//		attributesComponent.curAttrs -= 1;
//		String newValue = String.format("%.0f", attributesComponent.update(type, 1));
//		
//		if(AttributesEnum.VIT == type)
//			label.setText(newValue);
//		else
//			label.setText(newValue);
	}
}
