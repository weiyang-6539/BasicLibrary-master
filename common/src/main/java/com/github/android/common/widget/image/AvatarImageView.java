package com.github.android.common.widget.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.android.common.R;
import com.github.android.common.utils.ViewUtil;

import java.util.Arrays;

/**
 * Created by fxb on 2020/9/6.
 */
public class AvatarImageView extends AppCompatImageView {

    public static final int[] COLORS = {
            0xff44bb66,
            0xff55ccdd,
            0xffbb7733,
            0xffff6655,
            0xffffbb44,
            0xff44aaff};

    private int mBorderWidth; // 边框宽度
    private int mBorderColor = Color.parseColor("#e2e2e2"); // 边框颜色

    private final float[] radii = new float[8];
    private int maskColor; // 遮罩颜色

    private final Path mClipPath = new Path();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF;

    private final Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final String mText = "魏洋";
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint.FontMetrics mFontMetrics;

    private final Paint mPaintTextBackground = new Paint(Paint.ANTI_ALIAS_FLAG);

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.FILL);

        //边框线使用图层叠加得到，界面效果更好
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setColor(mBorderColor);

        mTextPaint.setColor(Color.WHITE);

        mPaintTextBackground.setColor(COLORS[0]);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView);

            setCornersTopLeftRadius(a.getDimensionPixelSize(R.styleable.AvatarImageView_aivCornersTopLeftRadius, 0));
            setCornersTopRightRadius(a.getDimensionPixelSize(R.styleable.AvatarImageView_aivCornersTopRightRadius, 0));
            setCornersBotRightRadius(a.getDimensionPixelSize(R.styleable.AvatarImageView_aivCornersBotRightRadius, 0));
            setCornersBotLeftRadius(a.getDimensionPixelSize(R.styleable.AvatarImageView_aivCornersBotLeftRadius, 0));
            setCornersRadius(a.getDimensionPixelSize(R.styleable.AvatarImageView_aivCornersRadius, 0));
            mBorderWidth = a.getDimensionPixelSize(R.styleable.AvatarImageView_aivBorderWidth, 0);
            mBorderColor = a.getColor(R.styleable.AvatarImageView_aivBorderColor, mBorderColor);

            maskColor = a.getColor(R.styleable.AvatarImageView_mask_color, maskColor);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
        } else {
            int maxSize = Math.max(widthSize, heightSize);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxSize + 2 * mBorderWidth, MeasureSpec.EXACTLY);
        }
        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTextPaint.setTextSize(0.4f * 2 * w / 2);
        mFontMetrics = mTextPaint.getFontMetrics();

        rectF = new RectF(0, 0, w, h);

        mClipPath.reset();
        mClipPath.addRoundRect(rectF, radii, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mClipPath, mBorderPaint);

        int size = getMeasuredWidth();
        float ratio = (size - 2 * mBorderWidth) * 1f / size;
        canvas.scale(ratio, ratio, size >> 1, size >> 1);
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG);

        //绘制背景和文本
        //canvas.drawPath(mClipPath, mPaintTextBackground);
        //canvas.drawText(mText, 0, mText.length(), getMeasuredWidth() >> 4, (getMeasuredHeight() >> 1) + Math.abs(mFontMetrics.top + mFontMetrics.bottom) / 2, mTextPaint);

        //原ImageView绘制图片的逻辑
        super.onDraw(canvas);

        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    private void setCornersTopLeftRadius(float cornersTopLeftRadius) {
        radii[0] = radii[1] = cornersTopLeftRadius;
    }

    private void setCornersTopRightRadius(float cornersTopRightRadius) {
        radii[2] = radii[3] = cornersTopRightRadius;
    }

    private void setCornersBotRightRadius(float cornersBotRightRadius) {
        radii[4] = radii[5] = cornersBotRightRadius;
    }

    private void setCornersBotLeftRadius(float cornersBotLeftRadius) {
        radii[6] = radii[7] = cornersBotLeftRadius;
    }

    private void setCornersRadius(float cornersRadius) {
        if (cornersRadius != 0) {
            Arrays.fill(radii, cornersRadius);
        }
    }

    private int dp2px(float dpValue) {
        return ViewUtil.dp2px(getContext(), dpValue);
    }
}