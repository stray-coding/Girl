package com.coding.girl.base

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author: Coding.He
 * @date: 2020/7/8
 * @emil: 229101253@qq.com
 * @des:
 */
abstract class LoadMoreOnScrollListener : RecyclerView.OnScrollListener() {
    companion object {
        private const val TAG = "LoadMoreRecycler"
    }

    private var lastVisibleItemPosition = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as RecyclerView.LayoutManager
        when (manager) {
            is StaggeredGridLayoutManager -> {
                val lastPositions = IntArray(manager.spanCount)
                manager.findLastCompletelyVisibleItemPositions(lastPositions)
                if (lastPositions.isNotEmpty()) {
                    lastVisibleItemPosition = lastPositions.max()!!
                }
            }
            is LinearLayoutManager -> lastVisibleItemPosition =
                manager.findLastVisibleItemPosition()
            is GridLayoutManager -> lastVisibleItemPosition = manager.findLastVisibleItemPosition()
        }

        val visibleItemCount = manager.childCount
        val totalItemCount = recyclerView.adapter?.itemCount ?: 0
        if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1)) {
            Log.i(TAG, "onScrollStateChanged: ...")
            loadMore()
        }
    }

    abstract fun loadMore()
}