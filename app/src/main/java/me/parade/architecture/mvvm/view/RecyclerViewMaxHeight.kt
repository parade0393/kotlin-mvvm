package me.parade.architecture.mvvm.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : parade
 * date : 2021/6/4
 * description : 不显示高度的RecyclerView
 */
class RecyclerViewMaxHeight:RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        //设置最大高度，AT_MOST参数表示控件可以自由调整大小，最大不超过Integer.MAX_VALUE/4
        val expandSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, expandSpec)
    }
}