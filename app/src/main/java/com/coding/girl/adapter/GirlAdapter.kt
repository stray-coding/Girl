package com.coding.girl.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        when (val manager = recyclerView.layoutManager as RecyclerView.LayoutManager) {
            is GridLayoutManager -> manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isFooter(position)) {
                        manager.spanCount
                    } else 1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (isFooter(holder.layoutPosition)) {
            when (val p = holder.itemView.layoutParams) {
                is StaggeredGridLayoutManager.LayoutParams -> p.isFullSpan = true
            }
        }
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return when (viewType) {
            RecyclerViewType.CONTENT.value -> R.layout.item_girl
            RecyclerViewType.FOOTER.value -> R.layout.foot_load_view
            else -> R.layout.item_girl
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

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!isFooter(position)) {
            bindData(holder, position, mData[position])
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (isFooter(position)) {
            RecyclerViewType.FOOTER.value
        } else {
            RecyclerViewType.CONTENT.value
        }
    }

    override fun getItemCount(): Int {
        return if (mData.size == 0) 0 else mData.size + 1
    }

    private fun isFooter(pos: Int): Boolean {
        return pos == itemCount - 1
    }
}
