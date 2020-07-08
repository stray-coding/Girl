package com.coding.girl.base

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author: Coding.He
 * @date: 2020/7/8
 * @emil: 229101253@qq.com
 * @des:
 */
abstract class LoadMoreRecyclerOnScrollListener: RecyclerView.OnScrollListener() {
    private val TAG = "LoadMoreRecycler"
    private var lastVisibleItemPosition = 0
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val manager = recyclerView.layoutManager as StaggeredGridLayoutManager
            val visibleItemCount = manager.childCount
            val totalItemCount = recyclerView.adapter!!.itemCount
            if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                Log.i(TAG, "onScrollStateChanged: ...")
                loadMore()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as StaggeredGridLayoutManager
        val lastPositions = IntArray(manager.spanCount)
        manager.findLastCompletelyVisibleItemPositions(lastPositions)
        if (lastPositions.isNotEmpty()) {
            lastVisibleItemPosition = lastPositions.max()!!
        }
    }

    abstract fun loadMore()
}