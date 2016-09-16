package com.guardian.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.guardian.game.GAME;
import com.guardian.game.GameConfig;
import com.guardian.game.assets.GameScreenAssets;

/**
 * 角色信息UI
 * 包含属性，装备，物品
 * 
 * @author D
 * @date 2016年9月8日 下午10:42:27
 */
public class CharacterUI extends Table{
	
	/**
	 * 装备栏UI
	 */
	public Table table3;
	
	/**
	 * 物品栏UI
	 */
	public Table table4;
	
	public CharacterUI(Skin skin, I18NBundle i18NBundle) {
		
		GAME.itemsSystem.setUi(this);
		GAME.equippedSystem.setUi(this);
		
		int bpad = 35; // 最外table内补丁，就是UI离屏幕距离
		
		int bagRow = 3; // 背包行数
		int bagCell = 6; // 背包列数
		
		// 最外层表格
		this.setDebug(GameConfig.UIdebug); 
		this.setName("CharacterUI");
		this.setVisible(false);
		this.setFillParent(true);
		this.pad(bpad); // 表格边内补丁
		this.defaults().width(GameConfig.width - bpad * 2).space(20, 10, 20, 10); // 表格Cell间距
		this.bottom();
	    
	    Table table2 = new Table(); // 属性面板表格
	    Cell<Table> thisCell = this.add(table2).width((GameConfig.width - bpad * 2 - 10)/2);
		table2.setDebug(GameConfig.UIdebug);
		table2.pad(15);
		table2.defaults().space(10).left();
		
	    table2.add(new Label(i18NBundle.get("Lv") + " : " , skin));
	    table2.add(new Label("1" , skin)).colspan(2);
	    table2.row().spaceTop(15);
	    table2.add(new Label(i18NBundle.get("ATK") + " : " , skin));
	    table2.add(new Label("100" , skin));
	    table2.add(new Label("+10" , skin));
	    table2.row();
	    table2.add(new Label(i18NBundle.get("DEF") + " : " , skin));
	    table2.add(new Label("100" , skin));
	    table2.add(new Label("+10" , skin));
	    table2.row();
	    table2.add(new Label(i18NBundle.get("HIT") + " : " , skin));
	    table2.add(new Label("100" , skin));
	    table2.add(new Label("+10" , skin));
	    table2.row();
	    table2.add(new Label(i18NBundle.get("AVD") + " : " , skin));
	    table2.add(new Label("100" , skin));
	    table2.add(new Label("+10" , skin));
	    table2.row();
	    table2.add(new Label(i18NBundle.get("VIT") + " : " , skin));
	    table2.add(new Label("100" , skin));
	    table2.add(new Label("+10" , skin));
	    
	    table3 = new Table(); // 装备面板表格
	    this.add(table3).width((GameConfig.width - bpad * 2 - 10)/2).fill();
	    table3.setDebug(GameConfig.UIdebug);
	    table3.defaults().size((GameConfig.width - bpad * 2 - (bagCell - 1 ) * 10)/bagCell).space(10).expand();
	    
	    Image image_temp = new Image(skin, GameScreenAssets.equipped1);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    
	    image_temp = new Image(skin, GameScreenAssets.equipped2);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    
	    image_temp = new Image(skin, GameScreenAssets.equipped3);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    table3.row();
	    image_temp = new Image(skin, GameScreenAssets.equipped4);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    
	    image_temp = new Image(skin, GameScreenAssets.equipped5);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    
	    image_temp = new Image(skin, GameScreenAssets.equipped6);
	    image_temp.setName("default_item");
	    table3.add(image_temp);
	    
	    table3.addListener(new DragListener(){  // 装备栏拖动事件
	    	
	    	private Cell<Image> cell; // 选中的装备栏
	    	private Image target; // 选中的装备栏图标
	    	
	    	@Override
	    	public void dragStart(InputEvent event, float x, float y, int pointer) {
	    		
	    		target = (Image)event.getTarget();
	    		if("default_item".equals(target.getName())){  // 默认装备栏格子背景，无物品可拖动
	    			target = null;
	    			return;
	    		}
	    		
	    		Table table = (Table)event.getListenerActor();
	    		cell = table.getCell(target);
	    		int i = table.getCells().indexOf(cell, false);
	    		
	    		cell.setActor(new Image(skin, "equipped" + (++i))); // 设置默认背景
	    		
	    		Vector2 vector2 = table.localToStageCoordinates(new Vector2(x, y));  // 转换到世界坐标
	    		
	    		table.getStage().addActor(target);
	    		target.setPosition(vector2.x - target.getWidth()/2, vector2.y - target.getHeight()/2); // 物品中心在鼠标位置
	    	}
	    	
	    	@Override
	    	public void drag(InputEvent event, float x, float y, int pointer) {
	    		if(target != null)
	    			target.setPosition(target.getX() - getDeltaX(), target.getY() - getDeltaY());
	    	}
	    	
	    	@Override
	    	public void dragStop(InputEvent event, float x, float y, int pointer) {
	    		if(target == null)
	    			return;
	    		
	    		Table table = (Table)event.getListenerActor();
	    		Vector2 vector2 = table.localToParentCoordinates(new Vector2(x, y)); // 转换成外层talbe（本类）坐标
	    		Actor hitActor = CharacterUI.this.hit(vector2.x, vector2.y, false);
	    		if(hitActor == table4 || hitActor.getParent() == table4){ // 拖动到物品栏范围
	    			
	    			Entity entity = GAME.equippedSystem.unwieldItem(table.getCells().indexOf(cell, false)); // 移除装备物品
	    			
	    			int i = -1;
	    			if(hitActor instanceof Image && "default_item".equals(hitActor.getName())){ // 放入当前hit的物品栏
	    				Cell<Image> hitCell = table4.getCell((Image)hitActor);
	    				i = GAME.itemsSystem.addItem(table4.getCells().indexOf(hitCell, false), entity);
	    			}
	    			else
	    				i = GAME.itemsSystem.addItem(entity); // 放入物品栏
	    			
	    			if(i == -1){ // 没位置了
	    				GAME.equippedSystem.equippedItem(entity);
	    				cell.setActor(target);
	    			}
	    			else
	    				target.remove();
	    			
	    			cell = null;
	    			target = null;
	    			return;
	    		}
	    		
    			cell.setActor(target);
    			
    			cell = null;
    			target = null;
	    	}
	    });
	    
	    this.row();
	    table4 = new Table(skin); // 物品栏面板表格
	    this.add(table4).colspan(2);
	    table4.defaults().size((GameConfig.width - bpad * 2 - (bagCell - 1 ) * 10)/bagCell).space(10);
	    table4.setDebug(GameConfig.UIdebug);
	    for(int j=0; j<bagRow; ++j){
	    	table4.row();
	    	for(int i=0; i<bagCell; ++i){
	    		Image image = new Image(skin, GameScreenAssets.item0);
	    		image.setName("default_item");
	    		table4.add(image);
	    	}
	    }
	    table4.getCells();
	    table4.addListener(new DragListener(){ // 物品栏拖动事件
	    	
	    	private Cell<Image> cell; // 选中的物品栏
	    	private Image target; // 选中的物品栏图标
			
	    	@Override
	    	public void dragStart(InputEvent event, float x, float y, int pointer) {
	    		
	    		target = (Image)event.getTarget();
	    		if("default_item".equals(target.getName())){  // 默认物品栏格子背景，无物品可拖动
	    			target = null;
	    			return;
	    		}
	    		
	    		Table table = (Table)event.getListenerActor();
	    		cell = table.getCell(target);
	    		
	    		Image default_item = new Image(skin, GameScreenAssets.item0);
	    		default_item.setName("default_item");
	    		cell.setActor(default_item); // 设置默认背景
	    		
	    		Vector2 vector2 = table.localToStageCoordinates(new Vector2(x, y));  // 转换到世界坐标
	    		
	    		table.getStage().addActor(target);
	    		target.setPosition(vector2.x - target.getWidth()/2, vector2.y - target.getHeight()/2); // 物品中心在鼠标位置
	    	}
	    	
	    	@Override
	    	public void drag(InputEvent event, float x, float y, int pointer) {
	    		if(target != null)
	    			target.setPosition(target.getX() - getDeltaX(), target.getY() - getDeltaY());
	    	}
	    	
	    	@Override
	    	public void dragStop(InputEvent event, float x, float y, int pointer) {
	    		
	    		if(target == null)
	    			return;
	    		
	    		Table table = (Table)event.getListenerActor();
	    		Image hit = (Image)table.hit(x, y, true);
	    		
	    		if(hit != null){ // 物品交换位置
	    			Cell<Image> hitCell = table.getCell(hit);
	    			hit.remove();
	    			cell.setActor(hit);
	    			hitCell.setActor(target);
	    			
	    			GAME.itemsSystem.exchangeEntity(table.getCells().indexOf(hitCell, false), table.getCells().indexOf(cell, false));

	    			cell = null;
	    			target = null;
	    			return;
	    		}
	    		
	    		Vector2 vector2 = table.localToParentCoordinates(new Vector2(x, y)); // 转换成外层talbe（本类）坐标
	    		Actor hitActor = CharacterUI.this.hit(vector2.x, vector2.y, false);
	    		if(hitActor == table3 || hitActor.getParent() == table3){ // 拖动到装备栏范围
	    			
	    			int i = table.getCells().indexOf(cell, false);
	    			Entity entity = GAME.itemsSystem.removeItem(i); // 移除物品
	    			Entity oldEntity = GAME.equippedSystem.equippedItem(entity); // 装备物品
	    			if(oldEntity != null)
	    				GAME.itemsSystem.addItem(i, oldEntity); // 之前装备的放入物品栏

	    			target.remove();
	    			cell = null;
	    			target = null;
	    			return;
	    		}
	    		
    			cell.setActor(target);
    			cell = null;
    			target = null;
	    	}
	    });
	    
	    this.row();
	    Table table5 = new Table(); // 最底部切换页和关闭键
	    this.add(table5).colspan(2);
	    table5.add(new Button(skin, GameScreenAssets.button1));
	}
	
	/**
	 * 物品放入物品栏
	 */
	public void addItem(int index, Image image) {
		table4.getCells().get(index).setActor(image);
	}
	
	/**
	 * 装备物品
	 */
	public void equippedItem(int index, Image image) {
		table3.getCells().get(index).setActor(image);
	}
}
