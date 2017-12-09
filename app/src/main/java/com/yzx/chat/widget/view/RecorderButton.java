package com.yzx.chat.widget.view;

import android.content.Context;
import android.graphics.Outline;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

/**
 * Created by YZX on 2017年12月08日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */

public class RecorderButton extends ImageView {

    private onRecorderTouchListener mListener;
    private boolean isCancel;

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecorderButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCircle();
    }

    private void setCircle() {
        this.setClipToOutline(true);
        this.setOutlineProvider(new CircleOutlineProvider());
    }

    private static class CircleOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            int width = view.getWidth();
            int height = view.getHeight();
            int minSize = Math.min(width, height);
            int left = (width - minSize) / 2;
            int top = (height - minSize) / 2;
            outline.setRoundRect(left, top, left + minSize, top + minSize, minSize / 2f);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mListener.onStart();
                isCancel = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isCancel) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
                        isCancel = true;
                        mListener.onCancel();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isCancel) {
                    mListener.onStop();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mListener.onCancel();
                break;
        }


        return true;
    }

    public void setOnRecorderTouchListener(onRecorderTouchListener listener) {
        mListener = listener;
    }

    public interface onRecorderTouchListener {
        void onStart();

        void onStop();

        void onCancel();
    }
}
