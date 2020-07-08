package com.coding.girl.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.base.BaseRecyclerAdapter
import com.coding.girl.base.RecyclerViewHolder
import com.coding.girl.bean.PageGirlBean

/**
 * @author: Coding.He
 * @date: 2020/6/19
 * @emil: 229101253@qq.com
 * @des:RecycleView的图片适配器
 */
class GirlAdapter(context: Context, dataList: List<PageGirlBean.DataBean>) :
    BaseRecyclerAdapter<PageGirlBean.DataBean>(context, dataList) {

    override fun getItemLayoutId(viewType: Int): Int {
        return when (viewType) {
            RecyclerViewType.CONTENT.value -> R.layout.item_girl
            RecyclerViewType.FOOTER.value -> R.layout.foot_load_view
            else -> R.layout.item_girl
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (position < mData.size) {
            bindData(holder, position, mData[position])
        }
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        item: PageGirlBean.DataBean
    ) {
        if (getItemViewType(position) == RecyclerViewType.CONTENT.value) {
            val img = holder.getImageView(R.id.img_girl)
            Glide.with(mContext)
                .load(item.url)
                .centerCrop()
                .into(img)
            holder.setText(R.id.tv_msg, item.desc)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < mData.size) {
            RecyclerViewType.CONTENT.value
        } else {
            RecyclerViewType.FOOTER.value
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }
}
