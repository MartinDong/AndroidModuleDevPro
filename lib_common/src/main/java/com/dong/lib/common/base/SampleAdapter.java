package com.dong.lib.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ================================================
 * 基类 {@link RecyclerView.Adapter} ,如果需要实现非常复杂的 {@link RecyclerView} ,请尽量使用其他优秀的三方库
 * Created by xiaoyulaoshi on 2018/3/20.
 * <p>
 * ================================================
 */

public abstract class SampleAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    protected List<T> mInfos;

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    private BaseHolder<T> mHolder;

    public SampleAdapter(List<T> infos) {
        super();
        this.mInfos = infos;
    }

    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        //填充条目视图
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);

        //初始化Holder
        mHolder = getHolder(view, viewType);
        //设置Item点击事件
        mHolder.setOnViewClickListener((view1, position) -> {
            if (mOnItemClickListener != null && getItemCount() > 0) {
                mOnItemClickListener.onItemClick(view, viewType, mInfos.get(position), position);
            }
        });

        return mHolder;
    }

    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.setData(mInfos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    /**
     * 让子类实现用来提供给 {@link BaseHolder}
     *
     * @param view     视图
     * @param viewType 视图类型（用来处理一个列表多种展示类型的情况）
     * @return 处理后的ViewHolder
     */
    public abstract BaseHolder<T> getHolder(View view, int viewType);

    /**
     * 提供于 {@code item } 布局的 {@code layoutId}
     *
     * @param viewType 视图类型
     * @return 获取构建列表视图使用的 LayoutId
     */
    public abstract int getLayoutId(int viewType);

    public List<T> getInfos() {
        return mInfos;
    }

    /**
     * 获取某个 {@code position}上的 item 的数据
     *
     * @param position 要获取的位置
     * @return 对应位置的数据实体
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 遍历所有的 {@link BaseHolder },释放他们需要释放的资源
     *
     * @param recyclerView 要释放的 RecycleView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        //循环列表中的所有子视图，进行释放操作
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);

            if (viewHolder != null && viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }

    }

    private interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, int viewType, T data, int position);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
