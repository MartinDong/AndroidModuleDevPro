package com.dong.lib.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.dong.lib.common.R;

/**
 * <p>圆角圆形ImageView</P>
 * Created by 董宏宇 on 2018/3/13.
 */
public class RoundImageView extends AppCompatImageView {

    //圆形模式
    private static final int MODE_CIRCLE = 1;

    //普通模式
    private static final int MODE_NONE = 0;

    //圆角模式
    private static final int MODE_ROUND = 2;

    //用来重绘的画笔
    private Paint mPaint;

    //当前要处理的模式
    private int currentMode = MODE_NONE;

    //绘制圆角的半径
    private int allRadius = dp2px(10);

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initViews();
    }

    /**
     * 获取用户设置的属性
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认属性
     */
    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取样式属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);

        //获取图片要处理的模式
        currentMode = a.hasValue(R.styleable.RoundImageView_type) ? a.getInt(R.styleable.RoundImageView_type, MODE_NONE) : MODE_NONE;

        //获取设置的圆角半径
        allRadius = a.hasValue(R.styleable.RoundImageView_radius) ? a.getDimensionPixelSize(R.styleable.RoundImageView_radius, allRadius) : allRadius;

        //释放资源，防止内存泄露，最终会导致内存溢出OOM
        a.recycle();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        //初始化画笔，并绘制时启用抗锯齿功能的绘制标志。|在blitting时启用抖动的Paint标志。
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  宽度测量规格
     * @param heightMeasureSpec 高度测量规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 当模式为圆形模式的时候，我们强制让宽高一致
        if (currentMode == MODE_CIRCLE) {
            //对比当前视图的宽高，取最小的一个当作原型的直径
            int result = Math.min(getMeasuredHeight(), getMeasuredWidth());
            //此方法必须由 #onMeasure（int，int）调用以存储测量宽度和测量高度。否则会在测量时触发异常。
            setMeasuredDimension(result, result);
        }
    }

    /**
     * 重写绘制方法，将四个角进行弧度处理
     *
     * @param canvas 画笔
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //该视图的drawable;如果没有drawable被赋值，则返回null
        Drawable mDrawable = getDrawable();
        //Matrix类拥有一个3x3矩阵来转换坐标。
        /*
         * 返回视图的可选矩阵。, 这被应用到绘制时的视图的drawable。
         * 如果没有矩阵，这个方法将返回一个单位矩阵。
         * 不要改变这个矩阵，而是复制一份。
         * 如果你想要一个不同的矩阵应用于drawable，一定要调用setImageMatrix（）。
         */
        Matrix mDrawMatrix = getImageMatrix();

        if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        //getIntrinsicWidth()获得固有宽度,getIntrinsicHeight()获得固有高度
        if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
            return;     // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
            mDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                //返回此ImageView是否剪裁为填充。
                if (getCropToPadding()) {
                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    canvas.clipRect(
                            scrollX + getPaddingLeft(),
                            scrollY + getPaddingTop(),
                            scrollX + getRight() - getLeft() - getPaddingRight(),
                            scrollY + getBottom() - getTop() - getPaddingBottom()
                    );
                }
            }

            //用指定的转换对当前矩阵进行预处理
            canvas.translate(getPaddingLeft(), getPaddingTop());

            //处理圆形模式
            if (currentMode == MODE_CIRCLE) {
                //将Drawable对象转成位图
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                //Shader着色器是在绘制过程中返回水平跨度的对象的基础类。
                //Shader.TileMode.CLAMP如果着色器在其原始边界之外绘制，则复制边缘颜色
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                //绘制圆形
                canvas.drawCircle(
                        getWidth() / 2,
                        getHeight() / 2,
                        getWidth() / 2,
                        mPaint
                );
            }
            //处理圆角
            else if (currentMode == MODE_ROUND) {
                //将Drawable对象转成位图
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                //Shader着色器是在绘制过程中返回水平跨度的对象的基础类。
                //Shader.TileMode.CLAMP如果着色器在其原始边界之外绘制，则复制边缘颜色
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                //绘制圆形矩形
                canvas.drawRoundRect(
                        new RectF(
                                getPaddingLeft(),
                                getPaddingTop(),
                                getWidth() - getPaddingRight(),
                                getHeight() - getPaddingBottom()
                        ),
                        allRadius,
                        allRadius,
                        mPaint
                );
            }
            //处理普通的视图
            else {
                if (mDrawMatrix != null) {
                    canvas.concat(mDrawMatrix);
                }
                mDrawable.draw(canvas);
            }

            //恢复到计数
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * 将Drawable转成Bitmap
     *
     * @param drawable 要转换的Drawable
     * @return Bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        //创建位图，指定当前图片的宽高，以及色域为ARGB_8888，这种配置非常灵活，并提供最好的质量。应尽可能使用它。
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //以Bitmap创建画布
        Canvas canvas = new Canvas(bitmap);

        //根据传递的scaletype获取matrix对象，设置给bitmap
        Matrix matrix = getImageMatrix();

        if (matrix != null) {
            canvas.concat(matrix);
        }
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * 将dp转成px使用
     *
     * @param value 目标单位
     * @return 转换后的单位
     */
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
