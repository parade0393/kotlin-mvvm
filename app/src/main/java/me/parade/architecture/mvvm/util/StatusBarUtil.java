package me.parade.architecture.mvvm.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.parade.architecture.mvvm.R;

/**
 * @author : parade
 * date : 2020/8/28
 * description :
 */
public class StatusBarUtil {

    private static void setStatusBarColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.translucentStatusBar(activity);
        }
    }

    /** 状态栏高度px */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }



    /**
     * 设置状态栏颜色
     * 状态栏字体颜色 font color
     */
    public static void setStatusBarLightMode(Activity activity, int color) {
        StatusBarUtil.setStatusBarColor(activity, color);
        StatusBarUtil.lintBuildStatusBarMode(activity, color);
    }

    /**
     * 检测 Android sdk 版本 设置 状态栏字体显示颜色
     */
    private static void lintBuildStatusBarMode(Activity activity, int color) {
        if (isLightColor(color)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 判断颜色是不是亮色
     */
    private static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class EyesLollipop{
        /**
         * 设置状态栏的颜色
         * 1.取消状态栏透明
         * 2.设置状态栏颜色
         * 3.view 不根据系统窗口调整自己的布局
         */
        static void setStatusBarColor(Activity activity, int statusColor) {
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }

        /**
         * 透明状态栏
         * 1.取消状态栏透明
         * 2.设置状态栏颜色
         * 3.view 不根据系统窗口调整自己的布局
         */
        static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
            Window window = activity.getWindow();
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground) {
                //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置状态栏为透明
                window.setStatusBarColor(Color.TRANSPARENT);
                //设置window的状态栏不可见
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置系统状态栏处于可见状态
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            //view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }


        /**
         * 判断颜色是不是亮色
         */
        private static boolean isLightColor(@ColorInt int color) {
            return ColorUtils.calculateLuminance(color) >= 0.5;
        }

        /**
         * MIUI的沉浸支持透明白色字体和透明黑色字体
         * https://dev.mi.com/console/doc/detail?pId=1159
         */
        static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
            try {
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                Class<? extends Window> clazz = activity.getWindow().getClass();
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
         */
        static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkmode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        private static int getStatusBarHeight(Context context) {
            int result = 0;
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = context.getResources().getDimensionPixelOffset(resId);
            }
            return result;
        }

        static void setStatusBarColorForCollapsingToolbar(final Activity activity, final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout,
                                                          final Toolbar toolbar, final int statusColor) {
            final Window window = activity.getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ////添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置状态栏为不可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            //通过OnApplyWindowInsetsListener()使Layout在绘制过程中将View向下偏移了,使collapsingToolbarLayout可以占据状态栏
            ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, (v, insets) -> insets);

            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            //view不根据系统窗口来调整自己的布局
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }

            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
            //设置状态栏的颜色
            collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
            toolbar.setFitsSystemWindows(false);
            //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.


            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight(activity);
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());


            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                private final static int EXPANDED = 0;
                private final static int COLLAPSED = 1;
                private int appBarLayoutState;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    //toolbar被折叠时显示状态栏
                    if (Math.abs(verticalOffset) > collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                        if (appBarLayoutState != COLLAPSED) {
                            appBarLayoutState = COLLAPSED;//修改状态标记为折叠
                            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                            int statusBarHeight = getStatusBarHeight(activity);
                            lp.height -= statusBarHeight;
                            toolbar.setLayoutParams(lp);
                            toolbar.setPadding(0, 0, 0, 0);
                            if (statusColor == Color.WHITE) {
                                if (MIUISetStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                                    //设置状态栏为指定颜色
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                                        activity.getWindow().setStatusBarColor(statusColor);
                                        ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                                        View mChildView = mContentView.getChildAt(0);
                                        if (mChildView != null) {
                                            mChildView.setFitsSystemWindows(false);
                                            ViewCompat.requestApplyInsets(mChildView);
                                        }
                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                                        //调用修改状态栏颜色的方法
                                        setStatusBarColor(activity, statusColor);
                                    }
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    activity.getWindow().setStatusBarColor(statusColor);
                                    // 如果亮色，设置状态栏文字为黑色
                                    if (isLightColor(statusColor)) {
                                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                    } else {
                                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                    }

                                    ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                                    View mChildView = mContentView.getChildAt(0);
                                    if (mChildView != null) {
                                        mChildView.setFitsSystemWindows(false);
                                        ViewCompat.requestApplyInsets(mChildView);
                                    }
                                } else {
                                    setStatusBarColor(activity, statusColor);
                                }
                            } else {
                                setStatusBarColor(activity, statusColor);
                            }
                        }
                    } else {
                        if (appBarLayoutState != EXPANDED) {
                            //toolbar显示时同时显示状态栏
                            appBarLayoutState = EXPANDED;//修改状态标记为展开
                            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                            int statusBarHeight = getStatusBarHeight(activity);
                            lp.height += statusBarHeight;
                            toolbar.setLayoutParams(lp);
                            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight,
                                    toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                            translucentStatusBar(activity, true);
                        }
                    }
                }
            });
        }

        static void setStatusBarWhiteForCollapsingToolbar(final Activity activity, final AppBarLayout appBarLayout,
                                                          final CollapsingToolbarLayout collapsingToolbarLayout, final Toolbar toolbar, final int statusBarColor) {
            final Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, (v, insets) -> insets);

            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }

            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(false);
            if (toolbar.getTag() == null) {
                CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                int statusBarHeight = getStatusBarHeight(activity);
                lp.height += statusBarHeight;
                toolbar.setLayoutParams(lp);
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                toolbar.setTag(true);
            }

            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
            collapsingToolbarLayout.setStatusBarScrimColor(statusBarColor);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                private final static int EXPANDED = 0;
                private final static int COLLAPSED = 1;
                private int appBarLayoutState;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    //toolbar被折叠时显示状态栏
                    if (Math.abs(verticalOffset) > collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                        if (appBarLayoutState != COLLAPSED) {
                            appBarLayoutState = COLLAPSED;//修改状态标记为折叠
                            StatusBarUtil.setStatusBarLightMode(activity, statusBarColor);
                        }
                    } else {
                        //toolbar显示时同时显示状态栏
                        if (appBarLayoutState != EXPANDED) {
                            appBarLayoutState = EXPANDED;//修改状态标记为展开
                            translucentStatusBar(activity, true);
                        }
                    }
                }
            });
        }
    }

    static class EyesKitKat{
        private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
        private static final String TAG_MARGIN_ADDED = "marginAdded";

        static void setStatusBarColor(Activity activity, int statusColor) {
            Window window = activity.getWindow();
            //设置Window为全透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            //获取父布局
            View mContentChild = mContentView.getChildAt(0);
            //获取状态栏高度
            int statusBarHeight = getStatusBarHeight(activity);

            //如果已经存在假状态栏则移除，防止重复添加
            removeFakeStatusBarViewIfExist(activity);
            //添加一个View来作为状态栏的填充
            addFakeStatusBarView(activity, statusColor, statusBarHeight);
            //设置子控件到状态栏的间距
            addMarginTopToContentChild(mContentChild, statusBarHeight);
            //不预留系统栏位置
            if (mContentChild != null) {
                mContentChild.setFitsSystemWindows(false);
            }
            //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
            int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
            View view = activity.findViewById(action_bar_id);
            if (view != null) {
                TypedValue typedValue = new TypedValue();
                if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                    int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                    StatusBarUtil.setContentTopPadding(activity, actionBarHeight);
                }
            }
        }

        static void translucentStatusBar(Activity activity) {
            Window window = activity.getWindow();
            //设置Window为透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mContentChild = mContentView.getChildAt(0);

            //移除已经存在假状态栏则,并且取消它的Margin间距
            removeFakeStatusBarViewIfExist(activity);
            removeMarginTopOfContentChild(mContentChild, getStatusBarHeight(activity));
            if (mContentChild != null) {
                //fitsSystemWindow 为 false, 不预留系统栏位置.
                mContentChild.setFitsSystemWindows(false);
            }
        }

        static void setStatusBarColorForCollapsingToolbar(final Activity activity, final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout,
                                                          Toolbar toolbar, int statusColor) {
            Window window = activity.getWindow();
            //设置Window为全透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);

            //AppBarLayout,CollapsingToolbarLayout,ToolBar,ImageView的fitsSystemWindow统一改为false, 不预留系统栏位置.
            View mContentChild = mContentView.getChildAt(0);
            mContentChild.setFitsSystemWindows(false);
            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);

            toolbar.setFitsSystemWindows(false);
            //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
            if (toolbar.getTag() == null) {
                CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                int statusBarHeight = getStatusBarHeight(activity);
                lp.height += statusBarHeight;
                toolbar.setLayoutParams(lp);
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                toolbar.setTag(true);
            }
            //移除已经存在假状态栏则,并且取消它的Margin间距
            int statusBarHeight = getStatusBarHeight(activity);
            removeFakeStatusBarViewIfExist(activity);
            removeMarginTopOfContentChild(mContentChild, statusBarHeight);
            //添加一个View来作为状态栏的填充
            final View statusView = addFakeStatusBarView(activity, statusColor, statusBarHeight);

            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
                int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    statusView.setAlpha(1f);
                } else {
                    statusView.setAlpha(0f);
                }
            } else {
                statusView.setAlpha(0f);
            }
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                        //toolbar被折叠时显示状态栏
                        if (statusView.getAlpha() == 0) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                    } else {
                        //toolbar展开时显示状态栏
                        if (statusView.getAlpha() == 1) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                    }
                }
            });
        }

        static void setStatusBarWhiteForCollapsingToolbar(final Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final int statusBarColor) {
            final Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mContentChild = mContentView.getChildAt(0);
            mContentChild.setFitsSystemWindows(false);
            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(false);

            if (toolbar.getTag() == null) {
                CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                int statusBarHeight = getStatusBarHeight(activity);
                lp.height += statusBarHeight;
                toolbar.setLayoutParams(lp);
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                toolbar.setTag(true);
            }

            int statusBarHeight = getStatusBarHeight(activity);
            int color = Color.BLACK;
            try {
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                color = statusBarColor;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                color = statusBarColor;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            final View statusView = addFakeStatusBarView(activity, color, statusBarHeight);
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
                int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    statusView.setAlpha(1f);
                } else {
                    statusView.setAlpha(0f);
                }
            } else {
                statusView.setAlpha(0f);
            }

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                private final static int EXPANDED = 0;
                private final static int COLLAPSED = 1;
                private int appBarLayoutState;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Math.abs(verticalOffset) >= (appBarLayout.getTotalScrollRange() - StatusBarUtil.getPxFromDp(activity, 56))) {
                        if (appBarLayoutState != COLLAPSED) {
                            appBarLayoutState = COLLAPSED;
                            if (statusView.getAlpha() == 0) {
                                statusView.animate().cancel();
                                statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                            }
                        }
                    } else {
                        if (appBarLayoutState != EXPANDED) {
                            appBarLayoutState = EXPANDED;
                            if (statusView.getAlpha() == 1) {
                                statusView.animate().cancel();
                                statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                            }
                            translucentStatusBar(activity);
                        }
                    }
                }
            });
        }

        private static void removeFakeStatusBarViewIfExist(Activity activity) {
            Window window = activity.getWindow();
            ViewGroup mDecorView = (ViewGroup) window.getDecorView();

            View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
            if (fakeView != null) {
                mDecorView.removeView(fakeView);
            }
        }

        private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
            Window window = activity.getWindow();
            ViewGroup mDecorView = (ViewGroup) window.getDecorView();

            View mStatusBarView = new View(activity);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            layoutParams.gravity = Gravity.TOP;
            mStatusBarView.setLayoutParams(layoutParams);
            mStatusBarView.setBackgroundColor(statusBarColor);
            mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

            mDecorView.addView(mStatusBarView);
            return mStatusBarView;
        }

        private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
            if (mContentChild == null) {
                return;
            }
            if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                lp.topMargin += statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag(TAG_MARGIN_ADDED);
            }
        }

        private static int getStatusBarHeight(Context context) {
            int result = 0;
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = context.getResources().getDimensionPixelOffset(resId);
            }
            return result;
        }

        private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
            if (mContentChild == null) {
                return;
            }
            if (TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                lp.topMargin -= statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag(null);
            }
        }
    }
}
