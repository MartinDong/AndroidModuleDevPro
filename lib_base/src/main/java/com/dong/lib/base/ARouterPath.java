package com.dong.lib.base;

/**
 * ================================================
 * 应用中所有的路由声明
 * Created by xiaoyulaoshi on 2018/3/27.
 * <p>
 * ================================================
 */

public interface ARouterPath {
    //=====================Main 组件=====================
    String ACT_MAIN_MAIN = "/main/main_act";
    //=====================Main 组件=====================

    //=====================User 组件=====================
    String ACT_USER_LOGIN = "/user/login_act";
    //=====================User 组件=====================

    //=====================Hotel 组件=====================
    String ACT_HOTEL_LIST = "/hotel/list_act";
    String ACT_HOTEL_DETAIL = "/hotel/detail_act";
    String FRG_HOTEL_MAIN_LIST = "/hotel/main_list_frg";
    String FRG_HOTEL_DETAIL_INTRODUCE = "/hotel/detail/introduce_frg";
    String FRG_HOTEL_DETAIL_COMMENT = "/hotel/detail/comment_frg";
    String FRG_HOTEL_DETAIL_ROOM = "/hotel/detail/room_frg";
    //=====================Hotel 组件=====================

    //=====================Order 组件=====================
    String ACT_ORDER_FORM = "/order/from_act";
    String FRG_ORDER_LIST = "/order/list_frg";
    String FRG_ORDER_DETAIL = "/order/detail_frg";
    String FRG_PAY_WAY = "/pay/way_frg";
    //=====================Order 组件=====================


    //=====================组件之间通信使用的服务=====================
    String SERVICE_USER_INFO = "/user_service/user_info";
    String SERVICE_HOTEL_INFO = "/hotel_service/hotel_info";
    //=====================组件之间通信使用的服务=====================
}
