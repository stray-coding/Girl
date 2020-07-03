package com.coding.girl.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.girl.R
import com.coding.girl.adapter.AboutAdapter
import com.coding.girl.base.BaseFragment
import com.coding.girl.divider.RecycleViewDivider
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * @author: Coding.He
 * @date: 2020/7/2
 * @emil: 229101253@qq.com
 * @des:
 */
class AboutFragment : BaseFragment() {
    private val lists = listOf("author：stray-coding", "mail：229101253@qq.com", "GitHub：click me")
    private lateinit var mAdapter: AboutAdapter
    override fun bindLayout(): Int {
        return R.layout.fragment_about
    }

    override fun initData(savedInstanceState: Bundle?) {
        rv_about.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        mAdapter = AboutAdapter(ctx, lists)
        rv_about.addItemDecoration(
            RecycleViewDivider(activity, RecyclerView.HORIZONTAL)
        )
        rv_about.adapter = mAdapter
    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { _, pos ->
            when (pos) {
                2 -> {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/stray-coding/Girl")
                    )
                    startActivity(urlIntent)
                }
            }
        }
    }

}