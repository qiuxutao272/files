package cn.yunzhisheng.vui.assistant.tv.view;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AutoSwitchTextView extends TextView {

    private AutoTimer mAutoTimer;
    private int mCurrentTimes = 1;
    private final int CountDownInterval = 500;

    public AutoSwitchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {
        if (mAutoTimer == null) {
            mAutoTimer = new AutoTimer(Long.MAX_VALUE, CountDownInterval);
        }
        if (this.isShown()) {
            mAutoTimer.start();
        } else {
            mAutoTimer.resetTime();
            mAutoTimer.cancel();
        }
    }

    private class AutoTimer extends CountDownTimer {

        public AutoTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

        }

        public void resetTime() {
            mCurrentTimes = 1;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mCurrentTimes % 3 == 0) {
                mCurrentTimes = 0;
                setText("...");
            } else if (mCurrentTimes % 3 == 1) {
                setText(".");
            } else {
                setText("..");
            }
            mCurrentTimes++;
        }
    }
}
