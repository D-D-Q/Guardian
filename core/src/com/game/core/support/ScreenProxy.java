package com.game.core.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ObjectMap;
import com.guardian.game.logs.Log;

/**
 * 创建Screen的代理类
 * 
 * 方便使用GlobalInline。通过ScreenProxy创建的Screen, 执行方法时候会自动调用enter和exit。
 * 所有外部调用Screen方法都要使用代理对象才可以，例如消息系统
 * 
 * 此类不兼容gwt，因为libgdx编译js，不支持jdk的反射
 * 
 * @author D
 * @date 2016年11月13日 下午7:40:28
 */
public class ScreenProxy{
	
	public static final ScreenProxy instance = new ScreenProxy();
	
	private ObjectMap<Class<?>, Object> proxys = new ObjectMap<>(8);
	
	private ScreenProxy() {
	}
	
	/**
	 * 创建Screen的动态代理对象
	 * 
	 * @param screenClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T createScreen(Class<? extends Screen> screenClass){
		
		// 创建代理对象
		Class<?>[] this_interfaces = screenClass.getInterfaces(); // 自己的接口
		Class<?>[] super_interfaces = screenClass.getSuperclass().getInterfaces(); // 父类的接口
		
		Class<?>[] interfaces = new Class<?>[this_interfaces.length + super_interfaces.length];
		System.arraycopy(this_interfaces, 0, interfaces, 0, this_interfaces.length);  
		System.arraycopy(super_interfaces, 0, interfaces, this_interfaces.length, super_interfaces.length);  
		
		ScreenHandler screenHandler = new ScreenHandler();
		Object proxy = (Screen) Proxy.newProxyInstance(screenClass.getClassLoader(), interfaces, screenHandler);
		
		proxys.put(screenClass, proxy); // 保存
		
		// 最后创建真实对象，可以在构造函数里获得代理
		screenHandler.setScreen(screenClass);
		
		return (T)proxy;
	}
	
	/**
	 * 获得代理对象
	 * 
	 * @param screenClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<? extends Screen> screenClass){
		return (T)proxys.get(screenClass);
	}
	
	/**
	 * 销毁代理
	 * @param screenClass
	 */
	public void disabledProxy(Class<? extends Screen> screenClass){
		proxys.remove(screenClass);
	}

	/**
	 * Screen的方法拦截
	 * 
	 * @author D
	 * @date 2016年11月13日 下午9:02:11
	 */
	private class ScreenHandler implements InvocationHandler{
		
		/**
		 * 真实对象
		 */
		private Object screen;
		
		public ScreenHandler() {
		}
		
		public void setScreen(Class<? extends Screen> screenClass){
			
			GlobalInline.instance.enter(screenClass); // 压入全局变量的key
			
			try {
				this.screen = screenClass.newInstance(); // 构造方法
			} 
			catch (Exception e) {
				Log.error(this, screenClass + "创建失败");
				e.printStackTrace();
				Gdx.app.exit();
			}
			
			GlobalInline.instance.exit(); // 移除全局变量的Key
		}

		/**
		 * 执行Screen的目标方法
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			
			GlobalInline.instance.enter(screen.getClass()); // 压入全局变量的key
			
			Object object = method.invoke(screen, args); // 目标方法。使用真实对象执行
			
			GlobalInline.instance.exit(); // 移除全局变量的Key
			
			return object;
		}
	}
}