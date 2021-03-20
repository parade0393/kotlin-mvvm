package com.jianqi.wuye.base.webview

import android.app.Activity
import android.webkit.JavascriptInterface

/**
 * @author : parade
 * date : 2021/3/8
 * description :
 */
class BaseWebViewInterface(val context: Activity) {
    @JavascriptInterface
    public fun closePage() {
        (context as? BaseWebViewActivity)?.onBackPressed()
    }
}