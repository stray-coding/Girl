package com.coding.girl.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.coding.girl.R
import kotlin.math.min


/**
 * @author: Coding.He
 * @date: 2020/6/19
 * @emil: 229101253@qq.com
 * @des:
 */
class RoundImageView(ctx: Context, attr: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(ctx, attr, defStyleAttr) {
    constructor(ctx: Context) : this(ctx, null, 0)
    constructor(ctx: Context, attr: AttributeSet?) : this(ctx, attr, 0)

    companion object {
        private const val TAG = "RoundImageView"
    }

    private var mWidth: Float = 0f
    private var mHeight: Float = 0f

    private var leftTopRadius = 0f
    private var rightTopRadius = 0f
    private var rightBottomRadius = 0f
    private var leftBottomRadius = 0f

    init {
        val array = ctx.obtainStyledAttributes(attr, R.styleable.RoundImageView)
        try {
            leftTopRadius = dp2px(array.getInt(R.styleable.RoundImageView_leftTopRadius, 5))
            rightTopRadius = dp2px(array.getInt(R.styleable.RoundImageView_rightTopRadius, 5))
            rightBottomRadius = dp2px(array.getInt(R.styleable.RoundImageView_rightBottomRadius, 5))
            leftBottomRadius = dp2px(array.getInt(R.styleable.RoundImageView_leftBottomRadius, 5))
            if (array.hasValue(R.styleable.RoundImageView_radius)) {
                val radius = dp2px(array.getInt(R.styleable.RoundImageView_radius, 5))
                leftTopRadius = radius
                rightTopRadius = radius
                rightBottomRadius = radius
                leftBottomRadius = radius
            }else{
                Log.i(TAG,"array not has attr radius")
            }
        } finally {
            array.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width.toFloat()
        mHeight = height.toFloat()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val maxRadius = min(mHeight, mWidth) / 2
        Log.i(TAG, "maxRadius:$maxRadius")
        leftTopRadius = if (leftTopRadius < maxRadius) leftTopRadius else maxRadius
        rightTopRadius = if (rightTopRadius < maxRadius) rightTopRadius else maxRadius
        rightBottomRadius = if (rightBottomRadius < maxRadius) rightBottomRadius else maxRadius
        leftBottomRadius = if (leftBottomRadius < maxRadius) leftBottomRadius else maxRadius
        Log.i(
            TAG,
            "leftTopRadius:$leftTopRadius,rightTopRadius:$rightTopRadius,rightBottomRadius:$rightBottomRadius,leftBottomRadius:$leftBottomRadius"
        )
        val path = Path()
        path.moveTo(leftTopRadius, 0f)
        path.lineTo(width - rightTopRadius, 0f)
        path.quadTo(mWidth, 0f, mWidth, rightTopRadius)
        path.lineTo(mWidth, mHeight - rightBottomRadius)
        path.quadTo(mWidth, mHeight, mWidth - rightBottomRadius, mHeight)
        path.lineTo(leftBottomRadius, mHeight)
        path.quadTo(0f, mHeight, 0f, mHeight - leftBottomRadius)
        path.lineTo(0f, leftTopRadius)
        path.quadTo(0f, 0f, leftTopRadius, 0f)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    fun setRadius(radius: Int) {
        val value = dp2px(radius)
        leftTopRadius = value
        rightTopRadius = value
        rightBottomRadius = value
        leftBottomRadius = value
        postInvalidate()
    }

    fun setRadius(leftTop: Int, rightTop: Int, rightBottom: Int, leftBottom: Int) {
        leftTopRadius = dp2px(leftTop)
        rightTopRadius = dp2px(rightTop)
        rightBottomRadius = dp2px(rightBottom)
        leftBottomRadius = dp2px(leftBottom)
        postInvalidate()
    }


    private fun dp2px(value: Int): Float {
        val scale = context.applicationContext.resources.displayMetrics.density
        return scale * value + 0.5f
    }
}