package com.coding.girl.adapter

import android.content.Context
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.base.BaseRecyclerAdapter
import com.coding.girl.base.RecyclerViewHolder
import com.coding.girl.bean.GirlBean

/**
 * @author: Coding.He
 * @date: 2020/6/19
 * @emil: 229101253@qq.com
 * @des:RecycleView的图片适配器
 */
class GirlAdapter(context: Context, dataList: List<GirlBean.DataBean>) :
    BaseRecyclerAdapter<GirlBean.DataBean>(context, dataList) {
    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_girl
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        item: GirlBean.DataBean
    ) {
        if (position == 0) {
            val layout = holder.getView(R.id.ll_layout)
            val layoutParams = LinearLayout.LayoutParams(layout.layoutParams)
            layoutParams.topMargin = 20
        }
        val img = holder.getImageView(R.id.img_girl)
        Glide.with(mContext)
            .load(item.url)
            .into(img)
        holder.setText(R.id.tv_msg, item.desc)
    }
}
