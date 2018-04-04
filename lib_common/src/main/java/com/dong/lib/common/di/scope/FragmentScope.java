package com.dong.lib.common.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ================================================
 * <p>
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the fragment to be memorized in the
 * correct component.
 * <p>
 * Created by xiaoyulaoshi on 2018/3/22.
 * <p>
 * ================================================
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {
}
