package com.game.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.game.core.manager.AshleyManager;
import com.guardian.game.logs.Log;

/**
 * 存储变量。
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
	 * 全局变量
	 */
	private final ObjectMap<Object, Object> globalMap = new ObjectMap<>(16);

	/**
	 * 全局变量根据该map的key划分。划分的key称为"域"
	 */
	private final ObjectMap<T, ObjectMap<Object, Object>> domainMap = new ObjectMap<>(8);
	
	/**
	 * 域的栈
	 */
	private final Array<T> domainStack = new Array<>(2);
	
	/**
	 * 标记的域
	 */
	private T markDomain;
	
	private GlobalInline() {
	}
	
	/**
	 * 进入新域时候调用
	 * @param domain
	 */
	public void enter(T domain){
		
		if(domainStack.size != 0 && domainStack.peek() == domain){
			Log.info(this, "重复当前：未压入" + domain);
			return;
		}
		domainStack.add(domain);
		
		Log.debug(this, "add " + domain);
	}
	
	/**
	 * 退出域时候调用, 保留变量
	 */
	public void exit(){
		if(domainStack.size == 0)
			throw new RuntimeException("GlobalInline的enter必须先被调用");
		
		T domain = domainStack.pop();
		
		Log.debug(this, "exit " + domain);
	}
	
	/**
	 * 标记当前域，当没入进入域的时候操作，就使用标记的域, 比如UI的事件
	 */
	public void mark(){
		if(domainStack.size == 0)
			throw new RuntimeException("GlobalInline的enter方法必须先被调用");
		
		markDomain = domainStack.peek();
		
		Log.debug(this, "mark " + markDomain);
	}
	
	/**
	 * 清楚标记域, 不是本域的标记不会清除成功
	 * 
	 * @return
	 */
	public boolean clearMark(){
		
		if(domainStack.size == 0)
			throw new RuntimeException("GlobalInline的enter方法必须先被调用");
		
		if(markDomain != domainStack.peek()) // 只能清除自己的标记
			return false;
		
		markDomain = null;
		
		Log.debug(this, "clear mark " + markDomain);
		
		return true;
	}
	
	/**
	 * 销毁当前域所有变量
	 */
	public void disabled(){
		
		if(domainStack.size == 0)
			throw new RuntimeException("GlobalInline的enter方法必须先被调用");
		
		T domain = domainStack.peek();
		
		// 先获取销毁元素
		ObjectMap<Object,Object> objectMap = domainMap.get(domain);
		if(objectMap == null){
			Log.info(this, "not have to disabled " + domain);
			return;
		}
		
		Object object = objectMap.get("ashleyManager");
		if(object != null)
			((AshleyManager)object).disabled();
		
		domainMap.remove(domain); // 再移除
		
		Log.info(this, "disabled " + domain);
	}
	
	/**
	 * 销毁所有域的变量
	 */
	public void disabledALL(){
		
		Entries<T,ObjectMap<Object,Object>> iterator = domainMap.iterator();
		
		while(iterator.hasNext){
			Entry<T,ObjectMap<Object,Object>> next = iterator.next();
			
			Object object = next.value.get("ashleyManager");
			if(object != null)
				((AshleyManager)object).engine.clearPools();
			
			iterator.remove();
		}
		
		Log.info(this, "dispose all");
	}
	
	/**
	 * 获得当前操作域
	 * @return
	 */
	public T getCurDomain(){
		
		T domain = markDomain;
		if(domainStack.size != 0)
			domain = domainStack.peek();
		if(domain == null)
			throw new RuntimeException("GlobalInline的enter或mark方法必须先被调用");
		
		return domain;
	}
	
	/**
	 * 添加全局变量
	 * 
	 * @param key
	 * @param value
	 */
	public void putGlobal(Object key, Object value){
		globalMap.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getGlobal(Object key){
		return (E)globalMap.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E removeGlobal(Object key){
		return (E)globalMap.remove(key);
	}
	
	/**
	 * 当前域的AshleyManager
	 * 
	 * @param ashleyManager
	 */
	public void putAshleyManager(AshleyManager ashleyManager){
		
		ObjectMap<Object,Object> objectMap = domainMap.get(getCurDomain());
		if(objectMap == null)
			domainMap.put(domainStack.peek(), (objectMap = new ObjectMap<Object,Object>(16)));
		
		objectMap.put("ashleyManager", ashleyManager);
	}
	
	/**
	 * 当前域的AshleyManager
	 * 
	 * @return
	 */
	public AshleyManager getAshleyManager(){
		
		ObjectMap<Object,Object> objectMap = domainMap.get(getCurDomain());
		return (AshleyManager)objectMap.get("ashleyManager");
	}
	
	/**
	 * 添加变量，当前域
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(Object key, Object value){

		ObjectMap<Object,Object> objectMap = domainMap.get(getCurDomain());
		if(objectMap == null)
			domainMap.put(domainStack.peek(), (objectMap = new ObjectMap<Object,Object>(16)));
		
		return objectMap.put(key, value);
	}
	
	/**
	 * 获得变量，当前域
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(Object key){
		
		ObjectMap<Object, Object> objectMap = domainMap.get(getCurDomain());
		return (E)objectMap.get(key);
	}
	
	/**
	 * 移出变量，当前域
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E remove(Object key){
		
		ObjectMap<Object, Object> objectMap = domainMap.get(getCurDomain());
		return (E)objectMap.remove(key);
	}
}
