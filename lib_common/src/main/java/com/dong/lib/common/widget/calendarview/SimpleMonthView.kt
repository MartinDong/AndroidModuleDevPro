package com.dong.lib.common.widget.calendarview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.text.format.Time
import android.view.MotionEvent
import android.view.View
import com.dong.lib.common.R
import java.security.InvalidParameterException
import java.text.DateFormatSymbols
import java.util.*

/**
 * 每个月作为一个ItemView
 * @param context
 * @param typedArray
 * @param dataModel
 */
class SimpleMonthView(context: Context, typedArray: TypedArray, dataModel: DayPickerView.DataModel)
    : View(context) {

    private val mInvalidDays: List<SimpleMonthAdapter.CalendarDay>          // 禁用的日期
    private val mBusyDays: List<SimpleMonthAdapter.CalendarDay>             // 被占用的日期
    private var mNearestDay: SimpleMonthAdapter.CalendarDay? = null         // 比离入住日期大且是最近的已被占用或者无效日期
    private val mCalendarTags: List<SimpleMonthAdapter.CalendarDay>         // 日期下面的标签
    private var mDefTag = ""

    protected var mPadding = 0

    private val mDayOfWeekTypeface: String                  // 头部星期的字体样式
    private val mMonthTitleTypeface: String                 // 头部年月的字体样式

    protected lateinit var mWeekTextPaint: Paint                     // 头部星期几的字体画笔
    protected lateinit var mDayTextPaint: Paint                      // 日期文字画笔
    protected lateinit var mTagTextPaint: Paint                      // 日期底部的文字画笔
    protected lateinit var mYearMonthPaint: Paint                    // 头部的年月字体画笔
    protected lateinit var mSelectedDayBgPaint: Paint                // 被选中的日期的底色画笔
    protected lateinit var mSelectedDayRangeBgPaint: Paint           // 被选中的日期的底色画笔
    protected lateinit var mBusyDayBgPaint: Paint                    // 被出租的日期的底色画笔
    protected lateinit var mInValidDayBgPaint: Paint                 // 被禁用的日期的底色画笔
    protected var mCurrentDayTextColor: Int = 0                 // 今天的字体颜色
    protected var mYearMonthTextColor: Int = 0                  // 头部年份和月份字体颜色
    protected var mWeekTextColor: Int = 0                       // 头部星期几字体颜色
    protected var mDayTextColor: Int = 0                        // 日期字体颜色
    protected var mTagTextColor: Int = 0                        // 普通的Tag字体颜色
    protected var mSelectedDayTextColor: Int = 0                // 被选中的日期字体颜色
    protected var mPreviousDayTextColor: Int = 0                // 过去的字体颜色
    protected var mSelectedDaysBgColor: Int = 0                 // 选中的日期背景颜色
    protected var mSelectedDaysRangeBgColor: Int = 0            // 选中的日期区间内的背景颜色
    protected var mBusyDaysBgColor: Int = 0                     // 被占用的日期背景颜色
    protected var mInValidDaysBgColor: Int = 0                  // 禁用的日期背景颜色
    protected var mBusyDaysTextColor: Int = 0                   // 被占用的日期字体颜色
    protected var mBusyTagTextColor: Int = 0                    // 被占用的日期地下的Tag字体颜色
    protected var mInValidDaysTextColor: Int = 0                // 禁用的日期字体颜色

    protected var mBgStartDay: Drawable? = null                     // 选中开始时间的背景色
    protected var mBgEndDay: Drawable? = null                       // 选中结束时间的背景色
    protected var mBgStartAndEndDay: Drawable? = null               // 选中开始结束时间的背景色

    protected var mTextSelectStartDay: String? = null               // 选中开始时间的下面的文字
    protected var mTextSelectEndDay: String? = null                 // 选中结束时间的下面的文字
    protected var mTextSelectStartEndDay: String? = null            // 选中开始结束时间相同的下面的文字

    protected var mTextBusyTag: String? = null                       // 不可预定的日期下面的标签
    protected var mTextInvalidTag: String? = null                    // 禁用的日期下面的标签

    protected var mHasToday = false                // 是否包含今天
    protected var mToday = -1                          // 今天的日期，数值区间1~31
    protected var mWeekStart = 1                       // 一周的第一天（不同国家的一星期的第一天不同）
    protected var mNumDays = 7                         // 一行几列
    protected var mNumCells: Int = 0                            // 一个月有多少天
    protected var mDayOfWeekStart = 0                    // 日期对应星期几
    protected var mRowHeight = DEFAULT_HEIGHT          // 行高
    protected var mWidth: Int = 0                               // simpleMonthView的宽度

    protected var mYear: Int = 0                                // 当前的年份
    protected var mMonth: Int = 0                               // 当前的月份
    internal val today: Time                                   // 今天，用来标记今天之前的日期不可选择。

    private val mCalendar: Calendar
    private val mDayLabelCalendar: Calendar           // 用于显示星期几
    private val isPrevDayEnabled: Boolean           // 今天以前的日期是否能被操作

    private val isShowTag: Boolean?                    // 是否显示标签

    private var mNumRows: Int = 0                               // 每个月的日期占用的行数

    private val mDateFormatSymbols = DateFormatSymbols()

    private var mOnDayClickListener: OnDayClickListener? = null     // 日期被选中的监听

    internal var mStartDate: SimpleMonthAdapter.CalendarDay? = null  // 选中的入住日期
    internal var mEndDate: SimpleMonthAdapter.CalendarDay? = null    // 选中的退房日期

    internal var cellCalendar: SimpleMonthAdapter.CalendarDay        // cell 的对应的日期

    /**
     * 获取年份和月份
     *
     * @return
     */
    private val monthAndYearString: String
        get() {
            val flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_MONTH_DAY
            val millis = mCalendar.timeInMillis
            return DateUtils.formatDateRange(context, millis, millis, flags)
        }

    init {

        val resources = context.resources

        setBackgroundColor(resources.getColor(android.R.color.white))

        mDayLabelCalendar = Calendar.getInstance()
        mCalendar = Calendar.getInstance()
        today = Time(Time.getCurrentTimezone())
        today.setToNow()
        mDayOfWeekTypeface = resources.getString(R.string.sans_serif)
        mMonthTitleTypeface = resources.getString(R.string.sans_serif)
        mCurrentDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorCurrentDay, resources.getColor(R.color.normal_day))
        mYearMonthTextColor = typedArray.getColor(R.styleable.DayPickerView_colorYearMonthText, resources.getColor(R.color.normal_day))
        mWeekTextColor = typedArray.getColor(R.styleable.DayPickerView_colorWeekText, resources.getColor(R.color.normal_day))
        mDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorNormalDayText, resources.getColor(R.color.normal_day))
        mTagTextColor = typedArray.getColor(R.styleable.DayPickerView_colorNormalTagText, resources.getColor(R.color.normal_day))
        mPreviousDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorPreviousDayText, resources.getColor(R.color.selected_day_background))
        mSelectedDaysBgColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayBackground, resources.getColor(R.color.selected_day_background))
        mSelectedDaysRangeBgColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayRangeBackground, resources.getColor(R.color.selected_day_background))
        mSelectedDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayText, resources.getColor(R.color.selected_day_text))
        mBusyDaysBgColor = typedArray.getColor(R.styleable.DayPickerView_colorBusyDaysBg, Color.GRAY)
        mInValidDaysBgColor = typedArray.getColor(R.styleable.DayPickerView_colorInValidDaysBg, Color.GRAY)
        mBusyDaysTextColor = typedArray.getColor(R.styleable.DayPickerView_colorBusyDaysText, resources.getColor(R.color.normal_day))
        mBusyTagTextColor = typedArray.getColor(R.styleable.DayPickerView_colorBusyTagText, resources.getColor(R.color.normal_day))
        mInValidDaysTextColor = typedArray.getColor(R.styleable.DayPickerView_colorInValidDaysText, resources.getColor(R.color.normal_day))

        mBgStartDay = typedArray.getDrawable(R.styleable.DayPickerView_bgStartDay)
        mBgEndDay = typedArray.getDrawable(R.styleable.DayPickerView_bgEndDay)
        mBgStartAndEndDay = typedArray.getDrawable(R.styleable.DayPickerView_bgStartAndEndDay)

        mTextSelectStartDay = typedArray.getString(R.styleable.DayPickerView_textSelectStartDay)
        mTextSelectStartDay = if (mTextSelectStartDay.isNullOrEmpty()) {
            ""
        } else {
            mTextSelectStartDay
        }

        mTextSelectEndDay = typedArray.getString(R.styleable.DayPickerView_textSelectEndDay)
        mTextSelectEndDay = if (mTextSelectEndDay.isNullOrEmpty()) {
            ""
        } else {
            mTextSelectEndDay
        }

        mTextSelectStartEndDay = typedArray.getString(R.styleable.DayPickerView_textSelectStartAndEndDay)
        mTextSelectStartEndDay = if (mTextSelectStartEndDay.isNullOrEmpty()) {
            ""
        } else {
            mTextSelectStartEndDay
        }

        mTextBusyTag = typedArray.getString(R.styleable.DayPickerView_textBusyTag)
        mTextBusyTag = if (mTextBusyTag.isNullOrEmpty()) {
            ""
        } else {
            mTextBusyTag
        }

        mTextInvalidTag = typedArray.getString(R.styleable.DayPickerView_textInvalidTag)
        mTextInvalidTag = if (mTextInvalidTag.isNullOrEmpty()) {
            ""
        } else {
            mTextInvalidTag
        }

        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDay, resources.getDimensionPixelSize(R.dimen.text_size_day))
        TAG_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeTag, resources.getDimensionPixelSize(R.dimen.text_size_tag))
        YEAR_MONTH_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeYearMonth, resources.getDimensionPixelSize(R.dimen.text_size_month))
        WEEK_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeWeek, resources.getDimensionPixelSize(R.dimen.text_size_day_name))
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.DayPickerView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height))
        DAY_SELECTED_HEIGHT = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_selectedDayHeight, resources.getDimensionPixelOffset(R.dimen.selected_day_radius))
        DAY_SELECTED_WIDTH = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_selectedDayWidth, resources.getDimensionPixelOffset(R.dimen.selected_day_radius))

        mRowHeight = (typedArray.getDimensionPixelSize(R.styleable.DayPickerView_calendarHeight,
                resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE - ROW_SEPARATOR) / 6

        isPrevDayEnabled = typedArray.getBoolean(R.styleable.DayPickerView_enablePreviousDay, false)

        isShowTag = typedArray.getBoolean(R.styleable.DayPickerView_tagShow, false)

        mInvalidDays = dataModel.invalidDays
        mBusyDays = dataModel.busyDays
        mCalendarTags = dataModel.tags
        mDefTag = dataModel.defTag

        cellCalendar = SimpleMonthAdapter.CalendarDay()

        initView()
    }

    /**
     * 计算每个月的日期占用的行数
     *
     * @return
     */
    private fun calculateNumRows(): Int {
        val offset = findDayOffset()
        val dividend = (offset + mNumCells) / mNumDays
        val remainder = (offset + mNumCells) % mNumDays
        return dividend + if (remainder > 0) 1 else 0
    }

    /**
     * 绘制头部的一行星期几
     *
     * @param canvas
     */
    private fun drawMonthDayLabels(canvas: Canvas) {
        val y = MONTH_HEADER_SIZE - WEEK_TEXT_SIZE / 2
        // 一个cell的二分之宽度
        val dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2)

        for (i in 0 until mNumDays) {
            val calendarDay = (i + mWeekStart) % mNumDays
            val x = (2 * i + 1) * dayWidthHalf + mPadding
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay)
            canvas.drawText(mDateFormatSymbols.shortWeekdays[mDayLabelCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()),
                    x.toFloat(), y.toFloat(), mWeekTextPaint)
        }
    }

    /**
     * 绘制头部（年份月份，星期几）
     *
     * @param canvas
     */
    private fun drawMonthTitle(canvas: Canvas) {
        val x = (mWidth + 2 * mPadding) / 2
        val y = MONTH_HEADER_SIZE / 2 + YEAR_MONTH_TEXT_SIZE / 2
        val stringBuilder = StringBuilder(monthAndYearString.toLowerCase())
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder[0]))
        canvas.drawText(stringBuilder.toString(), x.toFloat(), y.toFloat(), mYearMonthPaint)
    }

    /**
     * 每个月第一天是星期几
     *
     * @return
     */
    private fun findDayOffset(): Int {
        return (if (mDayOfWeekStart < mWeekStart) mDayOfWeekStart + mNumDays else mDayOfWeekStart) - mWeekStart
    }

    /**
     * 日期点击事件
     *
     * @param calendarDay
     */
    private fun onDayClick(calendarDay: SimpleMonthAdapter.CalendarDay) {
        if (mOnDayClickListener != null && (isPrevDayEnabled || !prevDay(calendarDay.day, today))) {
            mOnDayClickListener!!.onDayClick(this, calendarDay)
        }
    }

    /**
     * 比较日期是否是同一天
     *
     * @param monthDay
     * @param time
     * @return
     */
    private fun sameDay(monthDay: Int, time: Time): Boolean {
        return mYear == time.year && mMonth == time.month && monthDay == time.monthDay
    }

    /**
     * 判断是否是已经过去的日期
     *
     * @param monthDay
     * @param time
     * @return
     */
    private fun prevDay(monthDay: Int, time: Time): Boolean {
        return mYear < time.year || mYear == time.year && mMonth < time.month ||
                mYear == time.year && mMonth == time.month && monthDay < time.monthDay
    }

    /**
     * 绘制所有的cell
     *
     * @param canvas
     */
    protected fun drawMonthCell(canvas: Canvas) {
        var y = MONTH_HEADER_SIZE + ROW_SEPARATOR + mRowHeight / 2
        val paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays)
        var dayOffset = findDayOffset()
        var day = 1

        while (day <= mNumCells) {
            val x = paddingDay * (1 + dayOffset * 2) + mPadding

            mDayTextPaint.color = mDayTextColor
            mDayTextPaint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            mTagTextPaint.color = mTagTextColor

            cellCalendar.setDay(mYear, mMonth, day)

            // 当天
            var isToady = false
            if (mHasToday && mToday == day) {
                isToady = true
            }

            // 已过去的日期
            var isPrevDay = false
            if ((!isPrevDayEnabled) && prevDay(day, today)) {
                isPrevDay = true
                mDayTextPaint.color = mPreviousDayTextColor
                canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
            }


            // 绘制起始日期的方格
            var isBeginDay = false

            // 绘制结束日期的方格
            var isLastDay = false

            //开始结束时间不相等
            if (mStartDate != mEndDate) {
                if (mStartDate != null && cellCalendar == mStartDate && mStartDate != mEndDate) {
                    isBeginDay = true
                    drawStartDayBg(canvas, x, y)
                    mDayTextPaint.color = mSelectedDayTextColor
                    mTagTextPaint.color = mSelectedDayTextColor
                    if (isToady) {
                        canvas.drawText("今天", x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    } else {
                        canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    }
                    canvas.drawText(mTextSelectStartDay!!, x.toFloat(), getTextYCenter(mDayTextPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                }

                if (mEndDate != null && cellCalendar == mEndDate && mStartDate != mEndDate) {
                    isLastDay = true
                    drawEndDayBg(canvas, x, y)
                    mDayTextPaint.color = mSelectedDayTextColor
                    mTagTextPaint.color = mSelectedDayTextColor
                    canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    canvas.drawText(mTextSelectEndDay!!, x.toFloat(), getTextYCenter(mDayTextPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                }
            }
            //开始结束时间相等
            else {
                if (mStartDate != null && cellCalendar == mStartDate) {
                    isBeginDay = true
                    drawStartAndEndDayBg(canvas, x, y)
                    mDayTextPaint.color = mSelectedDayTextColor
                    mTagTextPaint.color = mSelectedDayTextColor
                    if (isToady) {
                        canvas.drawText("今天", x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    } else {
                        canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    }
                    canvas.drawText(mTextSelectStartEndDay!!, x.toFloat(), getTextYCenter(mDayTextPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                }
            }


            // 在入住和退房之间的日期
            if (cellCalendar.after(mStartDate) && cellCalendar.before(mEndDate)) {
                mDayTextPaint.color = mDayTextColor
                drawRangeDayBg(canvas, x, y, mSelectedDayRangeBgPaint)
                // 标签变为白色
                mTagTextPaint.color = mTagTextColor
            }

            // 被占用的日期
            var isBusyDay = false
            for (calendarDay in mBusyDays) {
                if (cellCalendar == calendarDay && !isPrevDay) {
                    isBusyDay = true
                    // 选择了入住和退房日期，退房日期等于mNearestDay的情况
                    if (mStartDate != null && mEndDate != null && mNearestDay != null &&
                            mEndDate == mNearestDay && mEndDate == calendarDay) {

                    } else {
                        // 选择了入住日期，没有选择退房日期，mNearestDay变为可选且不变灰色
                        if (mStartDate != null && mEndDate == null && mNearestDay != null && cellCalendar == mNearestDay) {
                            mDayTextPaint.color = mDayTextColor
                        } else {
                            drawRangeDayBg(canvas, x, y, mBusyDayBgPaint)
                            mDayTextPaint.color = mBusyDaysTextColor
                        }
                        mTagTextPaint.color = mBusyTagTextColor
                        canvas.drawText(mTextBusyTag!!, x.toFloat(), getTextYCenter(mBusyDayBgPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                    }

                    mDayTextPaint.color = mBusyDaysTextColor
                    if (isToady) {
                        canvas.drawText("今天", x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    } else {
                        canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    }
                }
            }

            // 禁用的日期
            val isInvalidDays = false
            for (calendarDay in mInvalidDays) {

                if (cellCalendar == calendarDay && !isPrevDay) {
                    isBusyDay = true
                    // 选择了入住和退房日期，退房日期等于mNearestDay的情况
                    if (mStartDate != null && mEndDate != null && mNearestDay != null &&
                            mEndDate == mNearestDay && mEndDate == calendarDay) {

                    } else {
                        // 选择了入住日期，没有选择退房日期，mNearestDay变为可选且不变灰色
                        if (mStartDate != null && mEndDate == null && mNearestDay != null && cellCalendar == mNearestDay) {
                            mDayTextPaint.color = mDayTextColor
                        } else {
                            drawRangeDayBg(canvas, x, y, mInValidDayBgPaint)
                            mDayTextPaint.color = mInValidDaysTextColor
                        }
                        mTagTextPaint.color = mInValidDaysTextColor
                        canvas.drawText(mTextInvalidTag!!, x.toFloat(), getTextYCenter(mInValidDayBgPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                    }
                    mDayTextPaint.color = mInValidDaysTextColor
                    if (isToady) {
                        canvas.drawText("今天", x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    } else {
                        canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                    }
                }
            }

            // 把入住日期之前和不可用日期之后的日期全部灰掉(思路)：
            // 1: 入住日期和退房日期不能同一天
            // 2：只选择了入住日期且没有选择退房日期
            // XXXXXXXXX这个功能已关闭XXXXXXXXXXX3：比入住日期小全部变灰且不可点击 cellCalendar.before(mStartDate) ||
            // 4：比入住日期大且离入住日期最近的被禁用或者被占用的日期
            if (mStartDate != null && mEndDate == null && mStartDate != mEndDate && !isInvalidDays && !isBusyDay) {
                if (mNearestDay != null && cellCalendar.after(mNearestDay)) {
                    drawRangeDayBg(canvas, x, y, mBusyDayBgPaint)
                }
            }

            // 绘制标签
            if (isShowTag!!) {
                if (!isPrevDay && !isInvalidDays && !isBusyDay && !isBeginDay && !isLastDay) {
                    var isCalendarTag = false
                    for (calendarDay in mCalendarTags) {
                        if (cellCalendar == calendarDay) {
                            isCalendarTag = true
                            canvas.drawText(calendarDay.tag, x.toFloat(), getTextYCenter(mTagTextPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                        }
                    }
                    if (!isCalendarTag) {
                        canvas.drawText(mDefTag, x.toFloat(), getTextYCenter(mTagTextPaint, y + DAY_SELECTED_HEIGHT / 3), mTagTextPaint)
                    }
                }
            }

            // 绘制日期
            if (!isPrevDay && !isInvalidDays && !isBusyDay && !isBeginDay && !isLastDay) {
                if (isToady) {
                    canvas.drawText("今天", x.toFloat(), getTextYCenter(mDayTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                } else {
                    canvas.drawText(String.format("%d", day), x.toFloat(), getTextYCenter(mTagTextPaint, y - DAY_SELECTED_HEIGHT / 3), mDayTextPaint)
                }
            }

            dayOffset++
            if (dayOffset == mNumDays) {
                dayOffset = 0
                y += mRowHeight
            }
            day++
        }
    }

    /**
     * 根据坐标获取对应的日期
     *
     * @param x
     * @param y
     * @return
     */
    fun getDayFromLocation(x: Float, y: Float): SimpleMonthAdapter.CalendarDay? {
        val padding = mPadding
        if (x < padding || x > mWidth - mPadding) {
            return null
        }

        val yDay = (y - MONTH_HEADER_SIZE).toInt() / mRowHeight
        val day = 1 + (((x - padding) * mNumDays / (mWidth - padding - mPadding)).toInt() - findDayOffset()) + yDay * mNumDays

        if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1)
            return null

        var calendar = SimpleMonthAdapter.CalendarDay(mYear, mMonth, day)

        // 获取日期下面的tag
        var flag = false
        for (calendarTag in mCalendarTags) {
            if (calendarTag.compareTo(calendar) == 0) {
                flag = true
                calendar = calendarTag
            }
        }
        if (!flag) {
            calendar.tag = mDefTag
        }
        return calendar
    }

    /**
     * 初始化一些paint
     */
    protected fun initView() {
        // 头部年份和月份的字体paint
        mYearMonthPaint = Paint()
        mYearMonthPaint.isFakeBoldText = true
        mYearMonthPaint.isAntiAlias = true
        mYearMonthPaint.textSize = YEAR_MONTH_TEXT_SIZE.toFloat()
        mYearMonthPaint.typeface = Typeface.create(mMonthTitleTypeface, Typeface.BOLD)
        mYearMonthPaint.color = mYearMonthTextColor
        mYearMonthPaint.textAlign = Align.CENTER
        mYearMonthPaint.style = Style.FILL

        // 头部星期几字体paint
        mWeekTextPaint = Paint()
        mWeekTextPaint.isAntiAlias = true
        mWeekTextPaint.textSize = WEEK_TEXT_SIZE.toFloat()
        mWeekTextPaint.color = mWeekTextColor
        mWeekTextPaint.typeface = Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL)
        mWeekTextPaint.style = Style.FILL
        mWeekTextPaint.textAlign = Align.CENTER

        // 被选中的日期背景paint
        mSelectedDayBgPaint = Paint()
        mSelectedDayBgPaint.isFakeBoldText = true
        mSelectedDayBgPaint.isAntiAlias = true
        mSelectedDayBgPaint.color = mSelectedDaysBgColor
        mSelectedDayBgPaint.textAlign = Align.CENTER
        mSelectedDayBgPaint.style = Style.FILL

        // 被选中的日期区间背景paint
        mSelectedDayRangeBgPaint = Paint()
        mSelectedDayRangeBgPaint.isFakeBoldText = true
        mSelectedDayRangeBgPaint.isAntiAlias = true
        mSelectedDayRangeBgPaint.color = mSelectedDaysRangeBgColor
        mSelectedDayRangeBgPaint.textAlign = Align.CENTER
        mSelectedDayRangeBgPaint.style = Style.FILL

        // 被占用的日期paint
        mBusyDayBgPaint = Paint()
        mBusyDayBgPaint.isAntiAlias = true
        mBusyDayBgPaint.color = mBusyDaysBgColor
        mBusyDayBgPaint.textSize = TAG_TEXT_SIZE.toFloat()
        mBusyDayBgPaint.textAlign = Align.CENTER
        mBusyDayBgPaint.style = Style.FILL
        mBusyDayBgPaint.alpha = SELECTED_CIRCLE_ALPHA

        // 禁用的日期paint
        mInValidDayBgPaint = Paint()
        mInValidDayBgPaint.isAntiAlias = true
        mInValidDayBgPaint.color = mInValidDaysBgColor
        mInValidDayBgPaint.textSize = TAG_TEXT_SIZE.toFloat()
        mInValidDayBgPaint.textAlign = Align.CENTER
        mInValidDayBgPaint.style = Style.FILL
        mInValidDayBgPaint.alpha = SELECTED_CIRCLE_ALPHA

        // 日期字体paint
        mDayTextPaint = Paint()
        mDayTextPaint.isAntiAlias = true
        mDayTextPaint.color = mDayTextColor
        mDayTextPaint.textSize = MINI_DAY_NUMBER_TEXT_SIZE.toFloat()
        mDayTextPaint.style = Style.FILL
        mDayTextPaint.textAlign = Align.CENTER
        mDayTextPaint.isFakeBoldText = false

        // 标签字体paint
        mTagTextPaint = Paint()
        mTagTextPaint.isAntiAlias = true
        mTagTextPaint.color = mTagTextColor
        mTagTextPaint.textSize = TAG_TEXT_SIZE.toFloat()
        mTagTextPaint.style = Style.FILL
        mTagTextPaint.textAlign = Align.CENTER
        mTagTextPaint.isFakeBoldText = false
    }

    override fun onDraw(canvas: Canvas) {
        DAY_SELECTED_WIDTH = mWidth / 14

        drawMonthTitle(canvas)
        drawMonthCell(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置simpleMonthView的宽度和高度
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + MONTH_HEADER_SIZE + ROW_SEPARATOR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val calendarDay = getDayFromLocation(event.x, event.y) ?: return true

            for (day in mInvalidDays) {
                // 选择了入住日期，这时候比入住日期大且离入住日期最近的不可用日期变为可选
                if (calendarDay == day && !(mEndDate == null && mNearestDay != null && calendarDay == mNearestDay)) {
                    return true
                }
            }

            for (day in mBusyDays) {
                // 选择了入住日期，这时候比入住日期大且离入住日期最近的不可用日期变为可选
                if (calendarDay == day && !(mEndDate == null && mNearestDay != null && calendarDay == mNearestDay)) {
                    return true
                }
            }
            onDayClick(calendarDay)
        }
        return true
    }

    /**
     * 设置传递进来的参数
     *
     * @param params
     */
    fun setMonthParams(params: HashMap<String, Any>) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw InvalidParameterException("You must specify month and year for this view")
        }
        tag = params

        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_DATE)) {
            mStartDate = if (null != params[VIEW_PARAMS_SELECTED_BEGIN_DATE]) {
                params[VIEW_PARAMS_SELECTED_BEGIN_DATE] as SimpleMonthAdapter.CalendarDay
            } else {
                null
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_DATE)) {
            mEndDate = if (null != params[VIEW_PARAMS_SELECTED_LAST_DATE]) {
                params[VIEW_PARAMS_SELECTED_LAST_DATE] as SimpleMonthAdapter.CalendarDay
            } else {
                null
            }
        }

        if (params.containsKey(VIEW_PARAMS_NEAREST_DATE)) {
            mNearestDay = if (null != params[VIEW_PARAMS_NEAREST_DATE]) {
                params[VIEW_PARAMS_NEAREST_DATE] as SimpleMonthAdapter.CalendarDay
            } else {
                null
            }
        }

        mMonth = params[VIEW_PARAMS_MONTH] as Int
        mYear = params[VIEW_PARAMS_YEAR] as Int

        mHasToday = false
        mToday = -1

        mCalendar.set(Calendar.MONTH, mMonth)
        mCalendar.set(Calendar.YEAR, mYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK)

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            mWeekStart = params[VIEW_PARAMS_WEEK_START] as Int
        } else {
            mWeekStart = mCalendar.firstDayOfWeek
        }

        //获取一个月中有多少天，工具类中提供了，闰年的2月份的天数的判断
        mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear)
        for (i in 0 until mNumCells) {
            val day = i + 1
            if (sameDay(day, today)) {
                mHasToday = true
                mToday = day
            }
        }

        mNumRows = calculateNumRows()
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    interface OnDayClickListener {
        fun onDayClick(simpleMonthView: SimpleMonthView, calendarDay: SimpleMonthAdapter.CalendarDay)
    }

    /**
     * 绘制 开始时间 cell
     *
     * @param canvas
     * @param x
     * @param y
     */
    private fun drawStartDayBg(canvas: Canvas, x: Int, y: Int) {
        var drawable = mBgStartDay
        if (null == drawable) {
            drawable = resources.getDrawable(R.drawable.shape_calendar_left_select)
        }

        drawable!!.setBounds(x - DAY_SELECTED_WIDTH, y - DAY_SELECTED_HEIGHT,
                x + DAY_SELECTED_WIDTH, y + DAY_SELECTED_HEIGHT)
        drawable.draw(canvas)
    }

    /**
     * 绘制 结束时间 cell
     *
     * @param canvas
     * @param x
     * @param y
     */
    private fun drawEndDayBg(canvas: Canvas, x: Int, y: Int) {
        var drawable = mBgEndDay
        if (null == drawable) {
            drawable = resources.getDrawable(R.drawable.shape_calendar_right_select)
        }
        drawable!!.setBounds(x - DAY_SELECTED_WIDTH, y - DAY_SELECTED_HEIGHT,
                x + DAY_SELECTED_WIDTH, y + DAY_SELECTED_HEIGHT)
        drawable.draw(canvas)
    }

    /**
     * 绘制 开始结束时间相等的时候 cell
     *
     * @param canvas
     * @param x
     * @param y
     */
    private fun drawStartAndEndDayBg(canvas: Canvas, x: Int, y: Int) {
        var drawable = mBgStartAndEndDay
        if (null == drawable) {
            drawable = resources.getDrawable(R.drawable.shape_calendar_select)
        }
        drawable!!.setBounds(x - DAY_SELECTED_WIDTH, y - DAY_SELECTED_HEIGHT,
                x + DAY_SELECTED_WIDTH, y + DAY_SELECTED_HEIGHT)
        drawable.draw(canvas)
    }

    /**
     * 绘制 cell
     *
     * @param canvas
     * @param x
     * @param y
     */
    private fun drawRangeDayBg(canvas: Canvas, x: Int, y: Int, paint: Paint) {
        val rectF = RectF((x - DAY_SELECTED_WIDTH).toFloat(), (y - DAY_SELECTED_HEIGHT).toFloat(),
                (x + DAY_SELECTED_WIDTH).toFloat(), (y + DAY_SELECTED_HEIGHT).toFloat())
        canvas.drawRoundRect(rectF, 0.0f, 0.0f, paint)
    }

    /**
     * 在使用drawText方法时文字不能根据y坐标居中，所以重新计算y坐标
     *
     * @param paint
     * @param y
     * @return
     */
    private fun getTextYCenter(paint: Paint, y: Int): Float {
        val fontMetrics = paint.fontMetrics
        val fontTotalHeight = fontMetrics.bottom - fontMetrics.top
        val offY = fontTotalHeight / 2 - fontMetrics.bottom
        return y + offY
    }

    companion object {

        val VIEW_PARAMS_SELECTED_BEGIN_DATE = "selected_begin_date"
        val VIEW_PARAMS_SELECTED_LAST_DATE = "selected_last_date"
        val VIEW_PARAMS_NEAREST_DATE = "mNearestDay"

        val VIEW_PARAMS_MONTH = "month"
        val VIEW_PARAMS_YEAR = "year"
        val VIEW_PARAMS_WEEK_START = "week_start"

        protected val SELECTED_CIRCLE_ALPHA = 128                   // 0~255透明度值
        protected var DEFAULT_HEIGHT = 32                           // 默认一行的高度
        protected var DAY_SELECTED_WIDTH: Int = 0                   // 选中圆角矩形宽度
        protected var DAY_SELECTED_HEIGHT: Int = 0                  // 选中圆角矩形高度
        protected var ROW_SEPARATOR = 12                            // 每行中间的间距
        protected var MINI_DAY_NUMBER_TEXT_SIZE: Int = 0            // 日期字体的最小尺寸
        protected var TAG_TEXT_SIZE: Int = 0                        // 标签字体大小
        protected var MONTH_HEADER_SIZE: Int = 0                    // 头部的高度（包括年份月份，星期几）
        protected var YEAR_MONTH_TEXT_SIZE: Int = 0                 // 头部年份月份的字体大小
        protected var WEEK_TEXT_SIZE: Int = 0                       // 头部年份星期的字体大小
    }
}