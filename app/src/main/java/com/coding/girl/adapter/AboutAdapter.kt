package com.coding.girl.adapter

import android.content.Context
import com.coding.girl.base.BaseRecyclerAdapter
import com.coding.girl.base.RecyclerViewHolder

/**
 * @author: Coding.He
 * @date: 2020/6/19
 * @emil: 229101253@qq.com
 * @des:RecycleView的图片适配器
 */
class AboutAdapter(context: Context, dataList: List<String>) :
    BaseRecyclerAdapter<String>(context, dataList) {
    override fun getItemLayoutId(viewType: Int): Int {
        return android.R.layout.simple_list_item_1
    }

    override fun bindData(
        holder: RecyclerViewHolder,
        position: Int,
        item: String
    ) {
        holder.setText(android.R.id.text1, item)
    }
}
