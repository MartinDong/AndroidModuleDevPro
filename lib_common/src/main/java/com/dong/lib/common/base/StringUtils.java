package com.dong.lib.common.base;

/**
 * <p>字符串相关工具类</p>
 * Created by xiaoyulaoshi on 2018/2/26.
 */

public class StringUtils {

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }
}
