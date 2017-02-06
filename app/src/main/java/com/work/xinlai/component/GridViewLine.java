package com.work.xinlai.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.work.xinlai.R;
import com.work.xinlai.util.Logger;


/**
 * gridview的分割线
 * Created by Administrator on 2016/12/13.
 */
public class GridViewLine extends GridView {
    //Color.parseColor("#3F51B5");00完全透明，ff完全不透明
    private int LINE_COLOR;
    private int DEFAULT_COLOR = 0xBC3F51B5;

    private int RowCount;
    private int AllCount;

    public GridViewLine(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public GridViewLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**获取attr属性**/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineGridView);
        LINE_COLOR = typedArray.getColor(R.styleable.LineGridView_LineColor, DEFAULT_COLOR);
        typedArray.recycle();
    }

    public GridViewLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //行数
    public void setRowNum(int count) {
        RowCount = count;
    }

    //设置总的数据长度
    public void setAllCount(int count) {
        AllCount = count;
    }


    private int getRowCount() {

        return AllCount % RowCount == 0 ? AllCount / RowCount : AllCount / RowCount + 1;
    }

    private int getColCount() {
        //return AllCount % getRowCount() == 0 ? AllCount / getRowCount() - 1 : AllCount / getRowCount();
        return RowCount - 1;
    }

    /**
     * 绘制分割线 absListView中的方法
     **/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        /**画笔设置分割线颜色**/
        localPaint.setColor(LINE_COLOR);
        //绘制行数
        for (int i = 0; i < getRowCount(); i++) {
            View child = getChildAt(RowCount * i);
            //用于获取margin，bootom
            ViewGroup.LayoutParams params = child.getLayoutParams();

            float start_X = 0f;
            //总宽度

            float stop_X = getWidth();
            float start_Y = child.getBottom();
            float stopy_Y = start_Y ;
            Logger.e("params",String.valueOf(params.height));
            Logger.e("start_X_row", String.valueOf(start_X));
            Logger.e("stop_X_row", String.valueOf(stop_X));
            Logger.e("start_Y_row", String.valueOf(start_Y));
            Logger.e("stopy_Y_row", String.valueOf(stopy_Y));
            canvas.drawLine(start_X, start_Y, stop_X, stopy_Y, localPaint);

        }
        //绘制列数

        for (int i = 0; i < getColCount(); i++) {
            View child = getChildAt(i);
            //用于获取margin，bootom
            ViewGroup.LayoutParams params = child.getLayoutParams();

            float start_X = child.getRight();
            //总宽度

            float stop_X = start_X ;
            float start_Y = 0;
            float stopy_Y = getChildAt(AllCount - 1).getBottom();
            Logger.e("start_X_col", String.valueOf(start_X));
            Logger.e("stop_X_col", String.valueOf(stop_X));
            Logger.e("start_Y_col", String.valueOf(start_Y));
            Logger.e("stopy_Y_col", String.valueOf(stopy_Y));
            canvas.drawLine(start_X, start_Y, stop_X, stopy_Y, localPaint);

        }


    }
}


