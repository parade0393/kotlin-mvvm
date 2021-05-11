# kotlin-mvvm
1.DialogFragment全屏
    * fragment布局设置背景
    * 设置style
        ```kotlin
         setStyle(STYLE_NORMAL,R.style.fgDialogStyle)
        ```
        ```xml
        <style name="fgDialogStyle" parent="AlertDialog.AppCompat">
            <item name="android:windowBackground">@android:color/transparent</item>
            <item name="android:backgroundDimEnabled">false</item>
        </style>
        ```
    * 代码设置宽高`dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,ScreenUtils.getScreenHeight()-BarUtils.getStatusBarHeight())`
2.GridLayoutManager 跟布局最好算是match_parent，高最好是wrap_content

### 使用方法及建议
    1. 网络请求尽量都放在Fragment的ViewModel中，因为全局的网络加载框和异常处理是在BaseFeagment中处理的
    2. 继承BaseActivity和BaseFragment默认只强制实现了getLayoutId加载布局资源
    3. 登陆后的用户数据使用sp保存，用户信息统一管理在UserModel中
    4. ViewModel要继承BaseViewModel
    5. 如果Activity和Fragment不想用ViewModel和Databinding，泛型请使用<NoViewModel,ViewDatabinding>
    6. Fragment中使用懒加载可重写lazyLoad方法
    7. 要求Fragment中和宿主Activity共享ViewModel，可在Fragment中重写isShareVM
    8. 如果fragment里使用多了ViewModel，那么除了泛型外的其他viewmodel的toast，showDialog等事件都要在fragment里手动观察
    9. 如果fragment里使用了SwipeRefreshLayout，那么可以重写BaseFragment的dismissDialog方法，保留父类的方法外额外加上SwipeRefreshLayout停止刷新的逻辑
    10.万能适配器添加底部视图必须放在上设置layoutManager之后