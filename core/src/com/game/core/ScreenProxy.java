package com.game.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.badlogic.gdx.Screen;
import com.guardian.game.logs.Log;

/**
 * 创建Screen的代理类，方便使用GlobalInline
 * 此类不兼容gwt，因为libgdx编译js，不支持jdk的反射
 * 
 * @author D
 * @date 2016年11月13日 下午7:40:28
 */
public class ScreenProxy{
	
	public static final ScreenProxy instance = new ScreenProxy();
	
	private ScreenProxy() {
	}
	
	/**
	 * 创建Screen的动态代理对象
	 * 
	 * @param screenClass
	 * @return
	 */
	public Screen createScreen(Class<? extends Screen> screenClass){
		
		Screen proxy = null;

		Class<?>[] interfaces = screenClass.getInterfaces();
		for(Class<?> i : interfaces){
			if(i.equals(Screen.class)){
				proxy = (Screen) Proxy.newProxyInstance(screenClass.getClassLoader(), interfaces, new ScreenHandler(screenClass)); // 自己实现了Screen接口
				break;
			}
		}
		// 使用父类的Screen接口
		if(proxy == null)
			proxy = (Screen) Proxy.newProxyInstance(screenClass.getClassLoader(), screenClass.getSuperclass().getInterfaces(), new ScreenHandler(screenClass)); 
		
		return proxy;
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
		private Screen screen;
		
		public ScreenHandler(Class<? extends Screen> screenClass) {
			try {
				this.screen = screenClass.newInstance();
			} 
			catch (Exception e) {
				e.printStackTrace();
				Log.error(this, screenClass + "创建失败:" + e.getMessage());
			}
		}

		/**
		 * 执行Screen的目标方法
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			
			GlobalInline.instance.enter(screen); // 压入全局变量的key
			
			Object object = method.invoke(screen, args); // 使用真实对象执行
			
			GlobalInline.instance.exit(); // 移除全局变量的Key
			
			return object;
		}
	}
}