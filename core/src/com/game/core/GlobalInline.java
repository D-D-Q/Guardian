package com.game.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.game.core.manager.AshleyManager;
import com.guardian.game.logs.Log;

/**
 * 存储全局变量。
 * 可以自定义划分为多个域，多个域之间的变量不冲突 
 * 
 * @author D
 * @date 2016年10月22日 上午10:21:06
 */
public class GlobalInline<T> {
	
	/**
	 * 以Screen为域划分的全局变量
	 */
	public static final GlobalInline<Class<?>> instance = new GlobalInline<>();

	/**
	 * 全局变量根据该map的key划分。划分的key称为"域"
	 */
	private ObjectMap<T, ObjectMap<Object, Object>> screenMap = new ObjectMap<>(8);
	
	/**
	 * 域的栈
	 */
	private Array<T> screenStack = new Array<>(2);
	
	private GlobalInline() {
	}
	
	/**
	 * 进入新域时候调用
	 * @param key
	 */
	public void enter(T key){
		
		if(screenStack.size != 0 && screenStack.peek() == key){
			Log.info(this, "重复当前：未压入" + key);
			return;
		}
		screenStack.add(key);
		
		Log.debug(this, "add " + key);
	}
	
	/**
	 * 退出域时候调用, 保留变量
	 */
	public void exit(){
		isEmtity();
		T key = screenStack.pop();
		
		Log.debug(this, "exit " + key);
	}
	
	/**
	 * 退出域时候调用, 销毁变量
	 */
	public void disabled(T key){
		isEmtity();
		
		screenStack.removeValue(key, true);
		ObjectMap<Object,Object> objectMap = screenMap.remove(key);
		
		Object object = objectMap.get("ashleyManager");
		if(object != null)
			((AshleyManager)object).engine.clearPools();
		
		Log.info(this, "dispose " + key);
	}
	
	/**
	 * 销毁所有域的变量
	 */
	public void disabledALL(){
		
		for(T key : screenMap.keys()){
			ObjectMap<Object,Object> objectMap = screenMap.remove(key);
			
			Object object = objectMap.get("ashleyManager");
			if(object != null)
				((AshleyManager)object).engine.clearPools();
		}
		
		Log.info(this, "dispose all");
	}
	
	/**
	 * 错误检查
	 */
	private void isEmtity(){
		if(screenStack.size == 0)
			throw new RuntimeException("GlobalInline的enter方法必须先被调用");
	}

	public void putAshleyManager(AshleyManager ashleyManager){
		isEmtity();
		
		ObjectMap<Object,Object> objectMap = screenMap.get(screenStack.peek());
		if(objectMap == null)
			screenMap.put(screenStack.peek(), (objectMap = new ObjectMap<Object,Object>(16)));
		
		objectMap.put("ashleyManager", ashleyManager);
	}
	
	public AshleyManager getAshleyManager(){
		isEmtity();
		
		ObjectMap<Object,Object> objectMap = screenMap.get(screenStack.peek());
		return (AshleyManager)objectMap.get("ashleyManager");
	}
	
	public Object put(Object key, Object value){
		isEmtity();
		
		ObjectMap<Object,Object> objectMap = screenMap.get(screenStack.peek());
		if(objectMap == null)
			screenMap.put(screenStack.peek(), (objectMap = new ObjectMap<Object,Object>(16)));
		
		return objectMap.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E get(Object key){
		isEmtity();
		
		ObjectMap<Object, Object> objectMap = screenMap.get(screenStack.peek());
		return (E)objectMap.get(key);
	}
}
