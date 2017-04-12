package com.wxk.myviewpagerindicate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/11
 */
public class ColorTrackTextView extends TextView {

    //普通画笔
    private Paint mNormalPaint;
    //变色画笔
    private Paint mChangePaint;

    private int mNormalColor = Color.BLACK;
    private int mChangeColor = Color.RED;

    private float mCurrentProgress = 0.0f;

    //从左到右or从右到左
    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    public enum Direction {
        RIGHT_TO_LEFT, LEFT_TO_RIGHT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        mNormalColor = array.getColor(R.styleable.ColorTrackTextView_normalColor, mNormalColor);
        mChangeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, mChangeColor);
        array.recycle();
        init();
    }

    private void init() {

        mNormalPaint = getPaintByColor(mNormalColor);
        mChangePaint = getPaintByColor(mChangeColor);
    }

    private Paint getPaintByColor(int color) {

        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        String text = getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        //位置 = 进度 *宽度
        int middle = (int) (mCurrentProgress * getWidth());

        //绘制不变色字体
        drawOriginText(text, canvas, middle);
        //绘制变色字体
        drawSlidingText(text, canvas, middle);

//        drawNormalText((int) middle, canvas);
//        drawChangeText((int) middle, canvas);
    }

    private void drawOriginText(String text, Canvas canvas, int middle) {

        if (mDirection == Direction.LEFT_TO_RIGHT)
            drawText(text, canvas, mNormalPaint, middle, getWidth());
        else
            drawText(text, canvas, mNormalPaint, 0, getWidth() - middle);
    }

    private void drawSlidingText(String text, Canvas canvas, int middle) {

        if (mDirection == Direction.LEFT_TO_RIGHT)
            drawText(text, canvas, mChangePaint, 0, middle);
        else
            drawText(text, canvas, mChangePaint, getWidth() - middle, getWidth());
    }

    private void drawText(String text, Canvas canvas, Paint paint, int start, int end) {

        canvas.save();

        //绘制截取的部分
        canvas.clipRect(new Rect(start, 0, end, getHeight()));

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.width();
        int x = getWidth() / 2 - textWidth / 2;

//        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
//        int dy = (fontMetricsInt.ascent + fontMetricsInt.descent) / 2 - fontMetricsInt.bottom;
//        int baseLine = getHeight() / 2 - dy;
//        int dy = rect.height() / 2 - fontMetricsInt.bottom;
        int dy = rect.height() / 2 - rect.bottom;
        int baseLine = (getHeight() + rect.height()) / 2 - dy;
        canvas.drawText(text, x, baseLine, paint);
        canvas.restore();
    }

    //绘制不变色字体
    private void drawNormalText(int middle, Canvas canvas) {

        //范围 middle--getWidth()

        canvas.save();

        canvas.clipRect(new Rect(middle, 0, getWidth(), getHeight()));
        String text = getText().toString();
        Rect bounds = new Rect();
        mNormalPaint.getTextBounds(text, 0, text.length(), bounds);
        int textWidth = bounds.width();
        int x = getWidth() / 2 - textWidth / 2;
        canvas.drawText(text, x, getHeight() / 2, mNormalPaint);

        canvas.restore();
    }

    //绘制变色字体
    private void drawChangeText(int middle, Canvas canvas) {

        //范围 0--middle

        canvas.save();
        //截取变色的部分
        canvas.clipRect(new Rect(0, 0, middle, getHeight()));
        String text = getText().toString();

        //测量字体宽度
        Rect rect = new Rect();
        mChangePaint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.width();
        // x = 宽度/2 - 字体宽度/2
        int x = getWidth() / 2 - textWidth / 2;

        //x字体的第一个位置,y基线
        canvas.drawText(text, x, getHeight() / 2, mChangePaint);

        canvas.restore();
    }

    //进度
    public void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    //朝向
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setNormalColor(int normalColor){
        this.mNormalColor = normalColor;
        mNormalPaint.setColor(mNormalColor);
    }

    public void setChangeColor(int changeColor){
        this.mChangeColor = changeColor;
        mChangePaint.setColor(mChangeColor);
    }
}
