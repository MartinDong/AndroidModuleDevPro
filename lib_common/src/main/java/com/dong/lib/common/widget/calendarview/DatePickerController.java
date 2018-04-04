package com.dong.lib.common.widget.calendarview;

import java.util.List;

public interface DatePickerController {

    enum FailEven {
        CONTAIN_NO_SELECTED("日期范围内有被占用的日期"),
        CONTAIN_INVALID("日期范围内有无效的日期"),
        NO_REACH_LEAST_DAYS("日期范围不能小于最小限制"),
        NO_REACH_MOST_DAYS("日期范围不能大于最大限制"),
        BEGIN_CANT_EQUALS_END("开始/结束日期不允许为同一天"),
        END_MT_START("");

        private final String failMessage;

        FailEven(final String failMessage) {
            this.failMessage = failMessage;
        }

        @Override
        public String toString() {
            return failMessage;
        }
    }

    void onDayOfMonthSelected(SimpleMonthAdapter.CalendarDay calendarDay);          // 点击日期回调函数，月份记得加1

    void onDateRangeSelected(List<SimpleMonthAdapter.CalendarDay> selectedDays);    // 选择范围回调函数，月份记得加1

    void alertSelectedFail(FailEven even);
}