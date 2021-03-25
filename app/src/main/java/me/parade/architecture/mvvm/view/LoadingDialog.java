package me.parade.architecture.mvvm.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.parade.architecture.mvvm.R;


/**
 * author: parade岁月
 * date: 2020/6/6 22:28
 * description 网络加载dialog
 */
public class LoadingDialog extends Dialog {

    private static final int MIN_SHOW_TIME = 500;
    private static final int MIN_DELAY = 500;

    private TextView tvMessage;

    /** 真正开始加载的时间 */
    private long mStartTime = -1;
    /** 是否已经延时隐藏 */
    private boolean mPostedHide = false;
    /** 是否已经延迟显示 */
    private boolean mPostedShow = false;
    /** 是否隐藏了 */
    private boolean mDismissed = false;

    private Handler mHandler = new Handler();

    private final Runnable mDelayedHide = () -> {
        mPostedHide = false;
        mStartTime = -1;
        dismiss();
    };

    private final Runnable mDelayedShow = () -> {
        mPostedShow = false;
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis();
            show();
        }
    };


    public LoadingDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_request_loading, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        tvMessage = view.findViewById(R.id.tv_loading_tip);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;//dialog的宽占满全屏，必须配合setBackgroundDrawableResource设置透明使用，否则不会宽度全屏
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;//dialog的高
            params.gravity = Gravity.CENTER;
            window.setDimAmount(0);
            window.setAttributes(params);
        }

    }

    public void showDialog(){
        showDialog("加载中");
    }

    public void showDialog(String message) {
        tvMessage.setText(message);

        mStartTime = -1;
        mDismissed = false;
        mHandler.removeCallbacks(mDelayedHide);
        mPostedHide = false;
        if (!mPostedShow) {
            mHandler.postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    public void hideDialog() {
        mDismissed = true;
        mHandler.removeCallbacks(mDelayedShow);
        mPostedShow = false;
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            dismiss();
        } else {
            if (!mPostedHide) {
                mHandler.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.removeCallbacks(mDelayedHide);
        mHandler.removeCallbacks(mDelayedShow);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mDelayedHide);
        mHandler.removeCallbacks(mDelayedShow);
    }
}
