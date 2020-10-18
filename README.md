# kotlin-mvvm

1. 增加了二级展开收缩的adapter ExpandableAdapter
2. 树形列表适配器使用注意
    * TreeHelper 63行和66行需要根据不同的数据修改判断逻辑
3.DialogFragment全屏
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
4.GridLayoutManager 跟布局最好算是match_parent，高最好是wrap_content