package com.dong.lib.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ================================================
 * {@link RecyclerView.ViewHolder} 基类
 * Created by xiaoyulaoshi on 2018/3/20.
 * <p>
 * ================================================
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected final String TAG = this.getClass().getSimpleName();

    private OnViewClickListener mOnViewClickListener;

    public BaseHolder(View itemView) {
        super(itemView);
        //设置点击事件
        itemView.setOnClickListener(this);

        //TODO 这里MVPArms进行了自动适配的处理，我们这里先不处理适配问题
    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getLayoutPosition());
        }
    }

    /**
     * 设置数据
     *
     * @param data     数据集合
     * @param position 位置
     */
    public abstract void setData(T data, int position);

    /**
     * 释放资源；
     * 条目中有动画的动画；
     * 加载的图片内存缓存；
     * ……
     */
    protected void onRelease(){}

    /**
     * 视图点击监听
     */
    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    /**
     * 设置视图点击监听
     *
     * @param listener 视图点击监听器
     */
    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}
