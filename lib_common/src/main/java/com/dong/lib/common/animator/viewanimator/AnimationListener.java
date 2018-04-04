package com.dong.lib.common.animator.viewanimator;

import android.view.View;

/**
 * ================================================
 * 类说明
 * Created by xiaoyulaoshi on 2018/3/30.
 * <p>
 * ================================================
 */
public class AnimationListener {
    private AnimationListener(){}

    public interface Start{
        void onStart();
    }

    public interface Stop{
        void onStop();
    }

    public interface Update<V extends View>{
        void update(V view, float value);
    }
}
