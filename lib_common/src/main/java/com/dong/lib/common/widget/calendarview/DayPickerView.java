package com.dong.lib.common.widget.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.dong.lib.common.R;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 日历规则
 * 1、不可选择当前日期之前的日期
 * 2、选择某一日期为入住日期后，再选择该日期之前的日期，则新选择的日期为入住日期，之前选择的入住日期取消；
 * 3、选择某一日期为入住日期后选择之后的日期，则新选择的日期为离店日期，同时回到上一页面（民宿详情页面不用跳转）。
 * 4、若某一日期不可选，则划掉该日期，同时在日期下方显示“不可订”，点击时弹出提示：MM-DD已经被人预订了哦，换一天吧
 * 5、若用户选择的入住日期与离店日期之间有不可预订日期，选择离店日期是弹出提示：MM-DD已经被人预订了哦，换一天吧，其中MM-DD显示距离入住时间最近的不可订日期。
 * 6、上一位客人离店日期是否下一位客人入住根据商户后台维护规则确定是否允许选择（离店日是否允许下一位用户入住）
 * 7、入住/离店日期不允许为同一天
 */
public class DayPickerView extends RecyclerView {
    /**
     * 视图朝向
     */
    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /**
     * 日期列表朝向
     */
    private int mOrientation = 1;

    /**
     * 是否开启横向滚动的效果
     */
    private boolean isPagerSnap;

    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private DatePickerController mController;
    protected int mCurrentScrollState = 0;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;

    private DataModel dataModel;


    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);

        isPagerSnap = typedArray.getBoolean(R.styleable.DayPickerView_isPagerSnap, false);
        int index = typedArray.getInt(R.styleable.DayPickerView_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        init(context);
    }

    /**
     * 设置视图的朝向
     *
     * @param orientation 方向的参数
     */
    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }


    /**
     * 初始化列表
     *
     * @param paramContext 上下文
     */
    public void init(Context paramContext) {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(paramContext);

        linearLayoutManager.setAutoMeasureEnabled(true);
        //根据设置来初始化列表朝向
        if (mOrientation == 0) {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }

        setLayoutManager(linearLayoutManager);

        //是否开启ViewPager的滚动效果
        if (isPagerSnap) {
            // 将SnapHelper attach 到RecyclrView
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(this);
        }

        mContext = paramContext;
        setUpListView();

        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }

                mPreviousScrollPosition = dy;
                mPreviousScrollState = mCurrentScrollState;
            }
        };
    }

    protected void setUpAdapter() {
        mAdapter = new SimpleMonthAdapter(getContext(), typedArray, mController, dataModel);
        setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);
    }

    /**
     * 设置参数
     *
     * @param dataModel   数据
     * @param mController 回调监听
     */
    public void setParameter(DataModel dataModel, DatePickerController mController) {
        if (dataModel == null) {
            Log.e("crash", "请设置参数");
            return;
        }
        this.dataModel = dataModel;
        this.mController = mController;
        setUpAdapter();
        // 跳转到入住日期所在的月份
        scrollToSelectedPosition(dataModel.selectedDays, dataModel.monthStart);
    }

    /**
     * 设置日历数据模型
     *
     * @param dataModel 日期模型
     */
    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
        setUpAdapter();
    }

    /**
     * 设置控制器
     *
     * @param mController 点击日历触发的控制器
     */
    public void setController(DatePickerController mController) {
        this.mController = mController;
        setUpAdapter();
    }

    private void scrollToSelectedPosition(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays, int monthStart) {
        if (selectedDays != null && selectedDays.getFirst() != null && selectedDays.getFirst().month > monthStart) {
            int position = selectedDays.getFirst().month - monthStart;
            scrollToPosition(position);
        }
    }

    public static class DataModel implements Serializable {
        public int yearStart;                                      // 日历开始的年份
        public int monthStart;                                     // 日历开始的月份
        public int monthCount;                                     // 要显示几个月
        public List<SimpleMonthAdapter.CalendarDay> invalidDays;   // 无效的日期
        public List<SimpleMonthAdapter.CalendarDay> busyDays;      // 被占用的日期
        public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays;  // 默认选择的日期
        public int leastDaysNum;                                   // 至少选择几天
        public int mostDaysNum;                                    // 最多选择几天
        public List<SimpleMonthAdapter.CalendarDay> tags;          // 日期下面对应的标签
        public String defTag;                                      // 默认显示的标签
        public boolean canSelectSameDay;                           // 是否可以选择同一天
    }
}