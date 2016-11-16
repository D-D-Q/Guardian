package com.guardian.game.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;

/**
 * 不同状态实现类的适配
 * State接口受DefaultStateMachine的泛型要求影响，不能用作DefaultStateMachine的泛型适配多种状态实现类
 * 
 * @author D
 * @date 2016年11月16日 下午8:58:41
 */
public interface StateAdapter extends State<Entity> {

}
