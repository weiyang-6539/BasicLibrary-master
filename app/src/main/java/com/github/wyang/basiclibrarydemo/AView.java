package com.github.wyang.basiclibrarydemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fxb on 2020/8/27.
 */
public class AView extends View {
    private int height = 30;//控件高度，可增加自定义属性设置
    private int radius = height >> 1;//圆角半径
    private int spacing = 5;//中间空白，可增加自定义属性设置

    private float ratio = 1.0f;//左边占比，内部增加方法设置
    private float angle = 60;//尖角的角度，可增加自定义属性设置
    private float diffValue;//上下两边的差，可根据angle角度计算得到

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    private int leftColor = Color.parseColor("#F5BB42");//左边颜色，可增加自定义属性设置
    private int rightColor = Color.parseColor("#6B7AD9");//右边颜色，可增加自定义属性设置

    public AView(Context context) {
        super(context);
        init();
    }

    public AView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff000000);

        diffValue = (float) (height / Math.tan(angle * Math.PI / 180));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            throw new RuntimeException("请设定控件的宽！");
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int width = (int) (getMeasuredWidth() - height - diffValue - spacing);//计算百分比所用宽度

        //绘制左
        mPaint.setColor(leftColor);
        path.arcTo(new RectF(0, 0, height, height), -90, -180);
        path.lineTo(width * ratio + radius, height);
        path.lineTo(width * ratio + radius + diffValue, 0);
        path.close();
        canvas.drawPath(path, mPaint);

        path.reset();

        //绘制右
        mPaint.setColor(rightColor);
        path.arcTo(new RectF(getMeasuredWidth() - height, 0, getMeasuredWidth(), height), 90, -180);
        path.lineTo(width * ratio + radius + spacing + diffValue, 0);
        path.lineTo(width * ratio + radius + spacing, height);
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
