package com.dong.lib.common.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ================================================
 * {@link Scope}: 作用域。Scopes非常有用，Dagger2通过自定义注解来限定作用域。
 * 这是一个非常强大的功能，所有的对象都不再需要知道怎么管理它自己的实例。
 * Dagger2中有一个默认的作用域注解@Singleton，通常在Android中用来标记在App整个生命周期内存活的实例。
 * 也可以自定义一个@PerActivity注解，用来表明生命周期与Activity一致。
 * 换句话说，我们可以自定义作用域的粒度（比如@PerFragment, @PerUser等等）
 * <p>
 * <p>
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memorized in the
 * correct component.
 * <p>
 * Created by xiaoyulaoshi on 2018/3/22.
 * <p>
 * ================================================
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
