package com.game.core.support;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.guardian.game.GAME;

/**
 * 攻击字幕效果
 * 
 * @author D
 * @date 2016年11月9日
 */
public class ATKWordEffect extends Label implements Poolable {

	public ATKWordEffect() {
		
		super("", GAME.skin);
		
		this.setOrigin(Align.center);
		
		this.setScale(0.1f);
		this.setFontScale(0.1f);
		
		RepeatAction repeatAction = Actions.forever(null);
		repeatAction.setAction(Actions.run(new Runnable() {
			public void run() {
				if (ATKWordEffect.this.getScaleX() >= 1 && ATKWordEffect.this.getScaleY() >= 1)
					repeatAction.finish();
				else
					ATKWordEffect.this.setFontScale(ATKWordEffect.this.getScaleX(), ATKWordEffect.this.getScaleY());
			}
		}));
		
		this.addAction(
				Actions.parallel(Actions.moveBy(0, 100, 0.4f, Interpolation.bounceOut),
						Actions.scaleTo(1, 1, 0.4f, Interpolation.bounceOut), repeatAction));
		
		this.addAction(Actions.after(Actions.delay(0.2f, Actions.run(new Runnable() {
			public void run() {
				ATKWordEffect.this.remove();
			}
		}))));
	}

	@Override
	public void reset() {
		this.setText("");
	}
}
