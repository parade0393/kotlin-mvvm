package me.parade.architecture.mvvm.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import me.parade.architecture.mvvm.R

/**
 * @author : parade
 * date : 2021/5/11
 * description : 上矩形，下弧形的view,宽度和高度需要是准确值或者match_parent
 */
class RectArcView:View{
    private var mWidth = 0
    private var mHeight = 0
    private var mArcHeight = 0
    private var mBgColor = 0
    private var mPaint = Paint()
    private lateinit var rect: Rect
    private  var mPath = Path()
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context,attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        context?.let {
            val array = it.obtainStyledAttributes(attrs, R.styleable.RectArcView)
            mArcHeight =array.getDimensionPixelOffset(R.styleable.RectArcView_m_arcHeight,0)
            mBgColor =
                array.getColor(R.styleable.RectArcView_m_bgcolor, Color.parseColor("#CE241D"))
            mPaint.apply {
                style = Paint.Style.FILL
                isAntiAlias = true
                color = mBgColor
            }
            array.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        rect.apply {
            left = 0
            top = 0
            right = mWidth
            bottom = mHeight -mArcHeight
        }
        canvas?.drawRect(rect,mPaint)//画矩形
        mPath.moveTo(0f,(mHeight - mArcHeight).toFloat())
        mPath.quadTo(mWidth/2.toFloat(),mHeight.toFloat(),mWidth.toFloat(),(mHeight - mArcHeight).toFloat())
        canvas?.drawPath(mPath,mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = width
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize
        }
        setMeasuredDimension(mWidth,mHeight)
    }
}