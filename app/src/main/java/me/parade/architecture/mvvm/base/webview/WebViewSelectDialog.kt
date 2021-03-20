package me.parade.architecture.mvvm.base.webview

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import me.parade.architecture.mvvm.R

/**
 * @author : parade
 * date : 2021/3/8
 * description :
 */
class WebViewSelectDialog(val mContext: Context, themeResId: Int,val listener: View.OnClickListener): Dialog(mContext, themeResId),
    View.OnClickListener {
    private lateinit var tvPhoto:TextView
    private lateinit var tvCamera:TextView
    private lateinit var tvCancel:TextView
    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val view = View.inflate(context, R.layout.dialog_webview_select, null)
        setContentView(view)
        initView(view)
    }

    private fun initView(view: View) {
        tvPhoto = findViewById(R.id.tv_photo)
        tvCamera = findViewById(R.id.tv_camera)
        tvCancel = findViewById(R.id.tv_cancel)
        tvPhoto.setOnClickListener(this)
        tvCamera.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        val displayMetrics = mContext.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        window?.let { window ->
            val params =  window.attributes.let {
                it.gravity = Gravity.BOTTOM
                it.alpha = 1.0f
                it.width = screenWidth
                it
            }
            window.attributes = params
        }
        window?.attributes?.let {

        }
    }

    override fun onClick(v: View?) {
        dismiss()
        listener.onClick(v)
    }
}