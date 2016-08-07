
package com.example.jammy.floatingview;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Jammy
 */
public class FloatingView extends RelativeLayout {
    private ViewDragHelper mDragger;
    FloatingActionButton fb;
    Context context ;
    int floatingX;
    int floatingY;


    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            /**
             * tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
             * 必须是点击到ViewGroup里面的View才调用
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child == fb)
                    return true;
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                mDragger.settleCapturedViewAt(floatingX, floatingY);
                invalidate();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.v("onInter", String.valueOf(mDragger.shouldInterceptTouchEvent(event)));
        return mDragger.shouldInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        if(event.getAction()==MotionEvent.ACTION_MOVE&&mDragger.isViewUnder(fb, (int) event.getX(), (int) event.getY())){
            Toast.makeText(context, "FloatingBtn移动了", Toast.LENGTH_SHORT).show();
        }
        return mDragger.isViewUnder(fb, (int) event.getX(), (int) event.getY());
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 绘制结束之后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fb = (FloatingActionButton) findViewById(R.id.fb);
    }


    /**
     * 用于得到View的初始化位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        floatingX = fb.getLeft();
        floatingY = fb.getTop();
    }

}