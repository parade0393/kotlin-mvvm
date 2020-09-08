package me.parade.architecture.mvvm.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author : parade
 * date : 2020/9/7
 * description : 屏幕相关工具
 */
public class ScreenUtils {
    //get real height of screen
    public static int getRealScreenHeight(Context mContext) {
        if (mContext == null){
            return 0;
        }
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Gets the size of the display,
            display.getRealSize(outPoint);
        } else {
            //Gets the size of the display except virtual navigation's height
            display.getSize(outPoint);
        }
        int mRealSizeHeight;
        mRealSizeHeight = outPoint.y;
        return mRealSizeHeight;
    }

}
