package com.example.toggleview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.toggleview.R;

/**
 * Created by Administrator on 2016/6/18.
 */
public class ToggleView extends View {

    private Bitmap slideBg;
    private Bitmap slideBlock;
    private int currentViewWidth;
    private int currentViewHeight;
    private int slideBlockWidth;

    private static final String MAXTEXT = "1080P";
    private static final String MINTEXT = "720P";
    /**
     * 状态为1080p
     */
    public static final int STATS_TOGGLE_MAX = 2;
    /**
     * 状态为720p
     */
    public static final int STATS_TOGGLE_MIN = 1;

    private int statsToggle = STATS_TOGGLE_MIN;
    private int lastStats = STATS_TOGGLE_MIN;

    private int textY;

    private Paint mPaint;
    private int slideBlockLeft = 0;
    private OnToggleViewSwitchListener listener;


    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleView);
        int slideBgId = ta.getResourceId(R.styleable.ToggleView_toggle_background, -1);
        int slideBlockId = ta.getResourceId(R.styleable.ToggleView_toggle_slideBlock, -1);
        int textSize = ta.getDimensionPixelSize(R.styleable.ToggleView_textSize, 0);
        ta.recycle();

        if (slideBgId == -1 || slideBlockId == -1) {
            throw new RuntimeException("必须指定:toggle_background和toggle_slideBlock资源图片");
        }

        slideBg = BitmapFactory.decodeResource(getResources(), slideBgId);
        slideBlock = BitmapFactory.decodeResource(getResources(), slideBlockId);

        //当前的控件的大小是取背景图片的大小
        currentViewWidth = slideBg.getWidth();
        currentViewHeight = slideBg.getHeight();
        //滑块的宽
        slideBlockWidth = slideBlock.getWidth();


        //设置画笔的参数
        mPaint = new Paint();
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/digital-7.ttf");
        mPaint.setTypeface(typeface);
        mPaint.setAntiAlias(true);
        if (textSize != 0) {
            mPaint.setTextSize(textSize);
        } else {
            mPaint.setTextSize(currentViewHeight / 2);
        }
        mPaint.setColor(Color.RED);

        //算出画文字时左下角的Y坐标值
        Rect rect = new Rect();
        mPaint.getTextBounds(MAXTEXT, 0, MAXTEXT.length(), rect);
        textY = currentViewHeight / 2 + rect.height() / 2;

    }

    /**
     * 设置文字的大小，默认是当前控件高度的一半
     *
     * @param size
     */
    public void setTextSize(int size) {
        mPaint.setTextSize(size);
        Rect rect = new Rect();
        mPaint.getTextBounds(MAXTEXT, 0, MAXTEXT.length(), rect);
        textY = currentViewHeight / 2 + rect.height() / 2;
    }

    /**
     * 设置文件的颜色，默认是红色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            //当控件的宽高设置为warp_cotent时，当前控件的大小就是背景图片的大小
            setMeasuredDimension(currentViewWidth, currentViewHeight);

        } else if (widthMode == MeasureSpec.EXACTLY) {
            //当控年的宽高设置是指定的大小时，要把背景图片和滑块进行处理。故目前暂不做处理

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画背景
        canvas.drawBitmap(slideBg, 0, 0, mPaint);
        //画文字
        if (statsToggle == STATS_TOGGLE_MAX) {
            canvas.drawText(MAXTEXT, slideBlockWidth / 2, textY, mPaint);
        } else if (statsToggle == STATS_TOGGLE_MIN) {
            canvas.drawText(MINTEXT, slideBlockWidth, textY, mPaint);
        }
        //画滑块
        canvas.drawBitmap(slideBlock, slideBlockLeft, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flushView((int) event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                flushView((int) event.getX());
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                if (upX > (currentViewWidth / 2)) {
                    flushView(currentViewWidth);
                    statsToggle = STATS_TOGGLE_MAX;
                } else {
                    flushView(0);
                    statsToggle = STATS_TOGGLE_MIN;
                }
                //回调
                if (null != listener && statsToggle != lastStats) {
                    listener.onSwitch(statsToggle);
                    lastStats = statsToggle;
                }
                break;
        }
        return true;
    }

    /**
     * 重绘
     *
     * @param x
     */
    private void flushView(int x) {
        slideBlockLeft = x - (slideBlockWidth / 2);
        if (slideBlockLeft < 0) {
            slideBlockLeft = 0;
        } else if (slideBlockLeft > currentViewWidth - slideBlockWidth) {
            slideBlockLeft = currentViewWidth - slideBlockWidth;
        }
        invalidate();
    }

    /**
     * 设置开关监听
     *
     * @param listener
     */
    public void setOnToggleViewSiwtchListener(OnToggleViewSwitchListener listener) {
        this.listener = listener;
    }

    /**
     * 开关切换回调监听
     */
    public interface OnToggleViewSwitchListener {

        /**
         * {@link #STATS_TOGGLE_MAX}    1080p
         * {@link #STATS_TOGGLE_MIN}    720p
         *
         * @param stats 当前开关状态
         */
        void onSwitch(int stats);
    }
}
