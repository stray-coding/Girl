package com.coding.girl.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @author: Coding.He
 * @date: 2020/6/19
 * @emil: 229101253@qq.com
 * @des:
 */
class StrokeImg(ctx: Context, attr: AttributeSet?, defStyleAttr: Int) :
    ImageView(ctx, attr, defStyleAttr) {
    constructor(ctx: Context) : this(ctx, null, 0)
    constructor(ctx: Context, attr: AttributeSet?) : this(ctx, attr, 0)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}