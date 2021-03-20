package com.jianqi.wuye.base.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.mvvmdemo.basemoudle.base.NoViewModel
import com.permissionx.guolindev.PermissionX
import me.parade.architecture.mvvm.R
import me.parade.architecture.mvvm.base.BaseActivity
import me.parade.architecture.mvvm.base.webview.PhotoUtils
import me.parade.architecture.mvvm.base.webview.WebViewSelectDialog
import me.parade.architecture.mvvm.databinding.ActivityBaseWebviewBinding
import me.parade.architecture.mvvm.util.ext.logd
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

/**
 * @author : parade
 * date : 2021/3/8
 * description :基础WebView
 */
private const val TAG = "parade0393"
private const val REQUEST_SELECT_FILE = 100
private const val REQUEST_CAMERA = 102
private const val REQUEST_PHOTO = 103

class BaseWebViewActivity : BaseActivity<NoViewModel, ActivityBaseWebviewBinding>(),
    View.OnClickListener {

    private val selectDialog by lazy {
        WebViewSelectDialog(
            this,
            R.style.popup_dialog_anim,
            this
        )
    }
    private val webViewSetting by lazy { mBinding.webViewBase.settings }
    private val fileTypeList by lazy {
        arrayListOf("txt", "doc", "docx", "xls", "xlsx", "pdf", "ppt", "apk", "zip", "rar")
    }
    private var uploadMessage: ValueCallback<Array<Uri>>? = null

    private var path_photo: String? = null


    override fun getLayoutId() = R.layout.activity_base_webview


    override fun initView(savedInstanceState: Bundle?) {
        initWebViewSetting()
        intent.getStringExtra("url")?.let {
            mBinding.webViewBase.loadUrl(it)
        }
    }

    override fun initEvent() {
        mBinding.ivBackWeb.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initWebViewSetting() {
        webViewSetting.apply {
            userAgentString = "User-Agent:Android"
            loadsImagesAutomatically = true
            //localStorage需要的缓存
            setAppCacheEnabled(true)
            setAppCachePath(externalCacheDir?.absolutePath)
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true//可以读取文件缓存
        }
        mBinding.webViewBase.apply {
            webChromeClient = MyWebChromeClient()
            webViewClient = MyWebViewClient()
            addJavascriptInterface(BaseWebViewInterface(this@BaseWebViewActivity), "androidBridge")
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)//Android5.0
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val s = request?.url.toString()
            var isFile = false
            kotlin.run {
                fileTypeList.forEach { fileType ->
                    if (s.endsWith(fileType, true)) {
                        isFile = true
                        return@run
                    }
                }
            }
            if (s.startsWith("tel:")) {
                Intent(Intent.ACTION_VIEW, Uri.parse(s)).let {
                    view?.context?.startActivity(it)
                }
                return true
            } else if (isFile) {
                dowLoadByBrowser(s)
                return true
            } else {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        //设置标题
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            "BaseWebViewActivity onPageStarted-----${url}".logd()
        }

        @RequiresApi(Build.VERSION_CODES.M)//6.0
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            error?.errorCode?.let {
                if (it != -6) {
                    "error------code:${error.errorCode}descript:${error.description}"
                }
            }
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
           mBinding.tvTitleWeb.text = title?:""
            "BaseWebViewActivity onReceivedTitle-----title：${title}-----url：${view?.url}".logd()
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress < 100) {
                if (mBinding.progressHorizontal.visibility == View.GONE) {
                    mBinding.progressHorizontal.visibility = View.VISIBLE
                    mBinding.progressHorizontal.progress = newProgress
                }
            } else {
                mBinding.progressHorizontal.visibility = View.GONE
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            uploadMessage = filePathCallback
            var isImage = false
            fileChooserParams?.let {
                kotlin.run {
                    it.acceptTypes.forEach { acceptType ->
                        if (acceptType.equals("image/*")) {
                            isImage = true
                            return@run
                        }
                    }
                }
            }
            if (isImage) {
                PermissionX.init(this@BaseWebViewActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            showSelectDialog()
                        }
                    }
            } else {
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }.also {
                    startActivityForResult(
                        Intent.createChooser(it, "File Chooser"),
                        REQUEST_SELECT_FILE
                    )
                }
            }
            return true
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            "onJsAlert${message}".logd()
            return super.onJsAlert(view, url, message, result)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            "level::${consoleMessage?.messageLevel()}--message::${consoleMessage?.message()}--sourceId::${consoleMessage?.sourceId()}--lineNumber::${consoleMessage?.lineNumber()}"
            return super.onConsoleMessage(consoleMessage)
        }
    }

    private fun showSelectDialog() {
        if (!selectDialog.isShowing) {
            selectDialog.show()
        }
    }

    private fun dowLoadByBrowser(s: String) {
        Intent(Intent.ACTION_VIEW).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse(s)
        }.also {
            startActivity(it)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_camera -> {
                PermissionX.init(this)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            startCamera(this)
                        }
                    }
            }
            R.id.tv_photo -> {
                PermissionX.init(this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            startAlbum(this)
                        }
                    }
            }
            R.id.tv_cancel -> {
                uploadMessage?.onReceiveValue(null)
                uploadMessage = null
            }
        }
    }

    private fun startAlbum(context: Activity) {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .apply {
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            }.also {
                context.startActivityForResult(
                    it,
                    REQUEST_PHOTO
                )
            }
    }

    private fun startCamera(context: Activity) {
        path_photo = "${getSdCardDirectory(context)}/temp.png"
        val temp = File(path_photo!!)
        val parentFile = temp.parentFile
        parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        if (temp.exists()) {
            temp.delete()
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //大于7.0
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            //通过FileProvider创建一个content类型的Uri
            val uri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", temp)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp))
        }
        context.startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun getSdCardDirectory(context: Context): String {
        val externalCacheDir = context.externalCacheDir
        val file = File(externalCacheDir, "wuye")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.path
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_FILE -> {
                    if (uploadMessage == null) {
                        return
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        uploadMessage!!.onReceiveValue(
                            WebChromeClient.FileChooserParams.parseResult(
                                resultCode,
                                data
                            )
                        )
                    }
                    uploadMessage = null
                }
                REQUEST_CAMERA -> {
                    compressPicture(this, path_photo!!, object : OnPictureCompressListener {
                        override fun start() {

                        }

                        override fun onSuccess(file: File?) {
                            file?.let {
                                uploadMessage?.onReceiveValue(arrayOf(Uri.fromFile(it)))
                            }
                        }

                        override fun onError(throwable: Throwable?) {
                            uploadMessage = null
                        }

                    })
                }
                REQUEST_PHOTO -> {
                    val result = data?.data
                    val path: String = PhotoUtils.getPath(this, result)
                    if (path == null) {
                        uploadMessage!!.onReceiveValue(null)
                    } else {
                        compressPicture(this, path, object : OnPictureCompressListener {
                            override fun start() {
                            }

                            override fun onSuccess(file: File?) {
                                uploadMessage!!.onReceiveValue(arrayOf(Uri.fromFile(file)))
                            }

                            override fun onError(throwable: Throwable?) {
                                uploadMessage!!.onReceiveValue(null)
                            }
                        })
                    }

                }
            }
        } else {
            uploadMessage?.onReceiveValue(null)
        }
    }

    private fun compressPicture(
        context: Context,
        path: String,
        listener: OnPictureCompressListener
    ) {
        Luban.with(context)
            .load(path)
            .ignoreBy(100)
            .setTargetDir(getSdCardDirectory(context))
            .filter { it ->
                !(TextUtils.isEmpty(it) || it!!.toLowerCase(Locale.ROOT).endsWith(".gif"))
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    listener.start()
                }

                override fun onSuccess(file: File?) {
                    listener.onSuccess(file)
                }

                override fun onError(e: Throwable?) {
                    listener.onError(e)
                }
            }).launch()
    }

    override fun onBackPressed() {
        if (mBinding.webViewBase.canGoBack()) {
            mBinding.webViewBase.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        webViewSetting.javaScriptEnabled = true
    }

    companion object {
        fun actionStart(context: Context, url: String) {
            Intent(context, BaseWebViewActivity::class.java)
                .apply {
                    putExtra("url", url)
                }.also {
                    context.startActivity(it)
                }
        }
    }

    interface OnPictureCompressListener {
        fun start()
        fun onSuccess(file: File?)
        fun onError(throwable: Throwable?)
    }
}