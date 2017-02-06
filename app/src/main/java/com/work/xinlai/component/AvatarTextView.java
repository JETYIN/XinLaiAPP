package com.work.xinlai.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.work.xinlai.R;

/**
 * Created by Administrator on 2016/12/22.钉钉圆形背景与文字
 */
public class AvatarTextView extends ImageView {
    //默认半径
    private final int DEFAULT_CIRCLE_RADIUS = 50;
    private String TEXT = "设置";

    public Context mContext;
    public int backGroundColor;
    public int textColor;
    public int radius;
    public int type;
    public float textStringSize;
    public String textString;

    private Paint mPaint;
    private Paint tPaint;

    public AvatarTextView(Context context) {
        this(context, null);
    }

    public AvatarTextView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.AvatarTextView);
        textColor = typedArray.getColor(R.styleable.AvatarTextView_textColor, Color.WHITE);
        backGroundColor = typedArray.getColor(R.styleable.AvatarTextView_backGroundColor, Color.BLUE);
        radius = typedArray.getDimensionPixelSize(R.styleable.AvatarTextView_radius, DEFAULT_CIRCLE_RADIUS);
        textString = typedArray.getString(R.styleable.AvatarTextView_textString);
        textStringSize = (float) typedArray.getDimensionPixelSize(R.styleable.AvatarTextView_textStringSize, 22);

        mPaint = new Paint();

        tPaint = new Paint();
    }

    //获取网络颜色设置背景填充
    public void setCircleBG(int color) {

        backGroundColor = color;
    }

    //获取网络-设置字体颜色
    public void setTextColor(int color) {
        textColor = color;
    }

    //湖区网络-设置文本
    public void setCircleText(String str) {
        textString = str;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中心为当前宽度的一半,绘制的区域在父布局的中心进行

        int center = getWidth() / 2;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backGroundColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1.5f);
        canvas.drawCircle(center, center,
                radius, mPaint);// 画圆圈

        // 绘制文字
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setStrokeWidth(1);
        tPaint.setTextSize(textStringSize);
        tPaint.setTypeface(Typeface.MONOSPACE);// 设置一系列文字属性
        tPaint.setColor(textColor);
        tPaint.setTextAlign(Paint.Align.CENTER);// 文字水平居中

        Paint.FontMetricsInt fontMetrics = tPaint.getFontMetricsInt();
        canvas.drawText(TextUtils.isEmpty(textString) ? TEXT : textString, center,
                center - (fontMetrics.top + fontMetrics.bottom) / 2, tPaint);// 设置文字竖直方向居中
        super.onDraw(canvas);

    }

}
