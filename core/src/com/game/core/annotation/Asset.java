package com.game.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源注解，指定资源类
 * 如果要额外知道加载参数，使用其他特定资源的注解
 * Skin皮肤资源会最后加载，一次只能加载一个皮肤
 * 
 * @author D
 * @date 2016年9月13日 上午6:27:09
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Asset {

	Class<?> value();
}