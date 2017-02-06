package com.work.xinlai.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.work.xinlai.R;

/**
 * gridview的分割线
 * Created by Administrator on 2016/12/13.
 */
public class LineGridView extends GridView {
    //Color.parseColor("#3F51B5");00完全透明，ff完全不透明
    private int LINE_COLOR;
    private int DEFAULT_COLOR = 0xBC3F51B5;

    public LineGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**获取attr属性**/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineGridView);
        LINE_COLOR = typedArray.getColor(R.styleable.LineGridView_LineColor, DEFAULT_COLOR);
        typedArray.recycle();
    }

    public LineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    /**
     * 绘制分割线 absListView中的方法
     **/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();
        int childCount = getChildCount();
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        /**画笔设置分割线颜色**/
        localPaint.setColor(LINE_COLOR);
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            /**该列的最后一个item只绘制底部**/
            if ((i + 1) % column == 0) {
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            } /**最后一行有数据的item绘制右边和底部**/
            else if ((i + 1) > (childCount - (childCount % column))) {
                /**绘制右边**/
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }/**有数据的非最后一行绘制底部和右边**/
            else {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }
        }
        /**最后一行空白item绘制分割线**/
        if (childCount % column != 0) {
            for (int j = 0; j <= (column - childCount % column); j++) {
                View lastView = getChildAt(childCount - 1);
                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
                canvas.drawLine(lastView.getLeft() + lastView.getWidth() * j, lastView.getBottom(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
            }
        }
    }
}

