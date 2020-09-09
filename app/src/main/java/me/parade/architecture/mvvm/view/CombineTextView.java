package me.parade.architecture.mvvm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import me.parade.architecture.mvvm.R;

/**
 * Created by parade岁月 on 2019/8/21 12:22
 * 必填TextView，比如文字前有*，前缀字体可定制，颜色可定制
 */
public class CombineTextView extends AppCompatTextView {

    private String mPrefix;
    private int mPrefixColor = Color.RED;
    private String mMiddleText;
    private int mMiddleTextColor;
    private String mContent;

    public CombineTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CombineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CombineTextView);
        mPrefix = array.getString(R.styleable.CombineTextView_prefix);
        mPrefixColor = array.getInteger(R.styleable.CombineTextView_prefix_text_color, Color.RED);
        mMiddleText = array.getString(R.styleable.CombineTextView_middle_text);
        mMiddleTextColor = array.getColor(R.styleable.CombineTextView_middle_text_color, mPrefixColor);
        mContent = array.getString(R.styleable.CombineTextView_android_text);
        if (null ==mPrefix) mPrefix = "";
        if (null == mMiddleText) mMiddleText = "";
        if (null == mContent) mContent = "";
        setTextContent();
        array.recycle();
    }

    private void setTextContent() {
        Spannable span = new SpannableString(mPrefix +mMiddleText+ mContent);
        span.setSpan(new ForegroundColorSpan(mPrefixColor), 0, mPrefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(mMiddleTextColor),mPrefix.length(),mPrefix.length()+mMiddleText.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);
    }


}
