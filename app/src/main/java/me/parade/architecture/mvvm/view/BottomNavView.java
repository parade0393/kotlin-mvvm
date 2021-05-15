package me.parade.architecture.mvvm.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.parade.architecture.mvvm.R;

/**
 * @author: parade岁月
 * @date: 2020/5/30 15:14
 * @description
 */
public class BottomNavView extends LinearLayout implements View.OnClickListener {

    private String[] mListText;
    private int[] mListNormalIcons;
    private int[] mListSelectedIcons;
    private int titleSize;
    private int normalColor;
    private int selectedColor;
    private int titleTopMargin;
    private int titleBottomMargin;
    private int unReadNumberTopMargin;
    private int unReadBackground;
    private int maxUnreadNumber;
    private Context context;
    private boolean haveMiddleItem;
    private List<ViewHolder> tabViewHolder;
    /** 当前选中的位置 */
    private int currentSelectedPosition = 0;
    private OnItemSelectedListener onItemSelectedListener;
    private OnItemReSelectedListener onItemReSelectedListener;
    private onMiddleItemClickListener onMiddleItemClickListener;

    public BottomNavView(Context context) {
        this(context, null);
    }

    public BottomNavView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavView);
        //统一字体大小单位为px
        titleSize = typedArray.getDimensionPixelSize(R.styleable.BottomNavView_titleSize, sp2px(9));
        normalColor = typedArray.getColor(R.styleable.BottomNavView_normalColor, Color.parseColor("#000000"));
        selectedColor = typedArray.getColor(R.styleable.BottomNavView_selectedColor, Color.parseColor("#3e97df"));
        titleTopMargin = typedArray.getDimensionPixelSize(R.styleable.BottomNavView_titleTopMargin, dp2px(5));
        titleBottomMargin = typedArray.getDimensionPixelSize(R.styleable.BottomNavView_titleBottomMargin, dp2px(5));
        unReadNumberTopMargin = typedArray.getDimensionPixelSize(R.styleable.BottomNavView_unReadNumTopMargin, dp2px(2));
        unReadBackground = typedArray.getResourceId(R.styleable.BottomNavView_unReadBackground, unReadBackground);
        maxUnreadNumber = typedArray.getInteger(R.styleable.BottomNavView_maxUnreadNum, 99);
        haveMiddleItem = typedArray.getBoolean(R.styleable.BottomNavView_haveMiddleItem, false);
        typedArray.recycle();
        setClipChildren(false);
    }


    public void build(){
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null){
            parent.setClipChildren(false);
        }
        tabViewHolder = new ArrayList<>();
        for (int i = 0; i < mListText.length; i++) {

            if ( haveMiddleItem && i == mListText.length/2){
                View bottomView = new View(context);
                LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                bottomView.setLayoutParams(layoutParams);
                bottomView.setTag(999);
                addView(bottomView);
            }

            LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_bottom_nav, this, false);

            //标题设置
            TextView textView = view.findViewById(R.id.tvTitle);
            textView.setText(mListText[i]);//设置标题文本
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);//设置标题大小
            LayoutParams params = (LayoutParams) textView.getLayoutParams();
            params.setMargins(0, titleTopMargin, 0, titleBottomMargin);//设置标题的上下边距

            //未读数量设置
            TextView number = view.findViewById(R.id.tvNoReadNum);
            RelativeLayout.LayoutParams numberLayoutParams = (RelativeLayout.LayoutParams) number.getLayoutParams();
            numberLayoutParams.setMargins(0,unReadNumberTopMargin,0,0);//设置未读数字的上边距
            if (unReadBackground != 0){
                number.setBackgroundResource(unReadBackground);//设置未读消息背景
            }

            //item布局设置
            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            view.setOrientation(LinearLayout.VERTICAL);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            view.setLayoutParams(layoutParams);
            view.setTag(i);//记录item的索引
            view.setOnClickListener(this);

            //添加view到容器
            tabViewHolder.add(new ViewHolder(view));

            addView(view);
        }
        //默认选中第一个
        tabViewHolder.get(0).icon.setImageResource(mListSelectedIcons[0]);
        tabViewHolder.get(0).title.setTextColor(selectedColor);
        for (int i = 1; i < tabViewHolder.size(); i++) {
            tabViewHolder.get(i).title.setTextColor(normalColor);
            tabViewHolder.get(i).icon.setImageResource(mListNormalIcons[i]);
        }
    }

    /**
     * 设置选中的item
     * @param position 要选中的item的索引
     */
    public void setCurrentItem(int position){
        if (currentSelectedPosition == position) return;
        //容错处理
        if (position >= mListText.length){
            position = mListText.length - 1;
        }else if (position < 0){
            position = 0;
        }

        for (int i = 0; i < mListText.length; i++) {
            if(i == position){
                //选中
                tabViewHolder.get(i).title.setTextColor(selectedColor);
                tabViewHolder.get(i).icon.setImageResource(mListSelectedIcons[i]);
            }else {
                //未选中
                tabViewHolder.get(i).title.setTextColor(normalColor);
                tabViewHolder.get(i).icon.setImageResource(mListNormalIcons[i]);
            }
        }
        currentSelectedPosition = position;//更新选中的索引
    }

    public void setUnreadNumber(int index,int number){
        TextView unreadNum = tabViewHolder.get(index).unreadNum;
        if (number > 0){
            unreadNum.setVisibility(VISIBLE);
            if (number > maxUnreadNumber){
                //大于设置最大值，则显示数字+
                unreadNum.setText(String.format(Locale.CHINA,"%d+", maxUnreadNumber));
            }else {
                //否则直接显示
                unreadNum.setText(String.format(Locale.CHINA,"%d", number));
            }
        }else {
            //未读未0或则小于0时不显示
            unreadNum.setVisibility(INVISIBLE);
        }
    }

    public BottomNavView setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public BottomNavView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public BottomNavView setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public BottomNavView setmListText(String[] mListText) {
        this.mListText = mListText;
        return this;
    }

    public BottomNavView setmListNormalIcons(int[] mListNormalIcons) {
        this.mListNormalIcons = mListNormalIcons;
        return this;
    }

    public BottomNavView setmListSelectedIcons(int[] mListSelectedIcons) {
        this.mListSelectedIcons = mListSelectedIcons;
        return this;
    }

    public BottomNavView setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
        return this;
    }

    public BottomNavView setOnItemReSelectedListener(OnItemReSelectedListener onItemReSelectedListener) {
        this.onItemReSelectedListener = onItemReSelectedListener;
        return this;
    }

    public BottomNavView setOnMiddleItemClickListener(BottomNavView.onMiddleItemClickListener onMiddleItemClickListener) {
        this.onMiddleItemClickListener = onMiddleItemClickListener;
        return this;
    }

    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    private int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (position != 999){
            if (currentSelectedPosition == position){
                //已经是选中状态
                if (onItemReSelectedListener != null){
                    onItemReSelectedListener.onItemReSelected(v,position);
                }
            }else {
                //未选中
                setCurrentItem(position);//选中
                if (onItemSelectedListener != null){
                    onItemSelectedListener.onItemSelected(v,position);
                }
            }
        }else {
          if (onMiddleItemClickListener != null){
              onMiddleItemClickListener.onMiddleItemSelected();
          }
        }

    }

    private static class ViewHolder{
        TextView title;
        ImageView icon;
        TextView unreadNum;
        View rootView;

        ViewHolder(View view){
            this.rootView = view;
            title = view.findViewById(R.id.tvTitle);
            icon = view.findViewById(R.id.ivIcon);
            unreadNum = view.findViewById(R.id.tvNoReadNum);
        }
    }

    public interface OnItemSelectedListener{
        void onItemSelected(View view, int position);
    }

    public interface OnItemReSelectedListener{
        void onItemReSelected(View view, int position);
    }

    public interface onMiddleItemClickListener{
        void onMiddleItemSelected();
    }

}
