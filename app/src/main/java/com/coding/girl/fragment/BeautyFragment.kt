package com.coding.girl.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.coding.girl.R
import com.coding.girl.activity.DetailAct
import com.coding.girl.adapter.GirlAdapter
import com.coding.girl.base.BaseFragment
import com.coding.girl.base.LoadMoreOnScrollListener
import com.coding.girl.bean.PageGirlBean
import kotlinx.android.synthetic.main.fragment_beauty.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @author: Coding.He
 * @date: 2020/7/2
 * @emil: 229101253@qq.com
 * @des:
 */
class BeautyFragment : BaseFragment() {
    private var isLoadingData = false

    /*获取Girl图片URL的相关参数*/
    private val mCategory = "Girl"
    private val mType = "Girl"
    private val mCount = 10
    private var mPage = 1

    private val mDataList: ArrayList<PageGirlBean.DataBean> = arrayListOf()
    private lateinit var mAdapter: GirlAdapter
    override fun bindLayout(): Int {
        return R.layout.fragment_beauty
    }

    override fun initData(savedInstanceState: Bundle?) {
        srl_refresh.isRefreshing = true
        loadPic(true)
        val spanCount = 2
        rv_girl.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = GirlAdapter(ctx, mDataList)
        rv_girl.adapter = mAdapter
        srl_refresh.setColorSchemeResources(R.color.black, R.color.green, R.color.blue)
    }

    override fun initListener() {
        srl_refresh.setOnRefreshListener {
            Log.i(TAG,"重新加载图片")
            loadPic(true)
        }
        mAdapter.setOnItemClickListener { _, pos ->
            val intent = Intent()
            intent.setClass(context!!, DetailAct::class.java)
            val bundle = Bundle()
            bundle.putString("name", mAdapter.getItemData(pos)._id)
            bundle.putString("title", mAdapter.getItemData(pos).title)
            bundle.putString("desc", mAdapter.getItemData(pos).desc)
            bundle.putString("time", mAdapter.getItemData(pos).publishedAt)
            bundle.putString("url", mAdapter.getItemData(pos).url)
            intent.putExtras(bundle)
            activity?.startActivity(intent)
        }

        rv_girl.addOnScrollListener(object : LoadMoreOnScrollListener() {
            override fun loadMore() {
                if (srl_refresh.isRefreshing) {
                    Log.i(TAG, "刷新中,禁止加载更多数据")
                    return
                }
                loadPic(false)
            }
        })
    }

    @Synchronized
    private fun loadPic(isRefresh: Boolean) {
        if (isLoadingData) {
            Log.i(TAG, "数据正在加载中，请勿重复刷新")
            return
        }
        try {
            isLoadingData = true
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val request = retrofit.create(GetOnePic::class.java)
            if (isRefresh) mPage = 1 else mPage++
            val url = getRequestUrl(mCategory, mType, mPage, mCount)
            val call = request.getCall(url)
            call.enqueue(object : Callback<PageGirlBean> {
                override fun onResponse(
                    call: Call<PageGirlBean>,
                    response: Response<PageGirlBean>
                ) {
                    Log.i(TAG, "response: {$response}")
                    Log.i(TAG, "response.body(): {${response.body()}}")
                    val body = response.body() as PageGirlBean
                    if (isRefresh) {
                        mDataList.clear()
                    }
                    for (item in body.data) {
                        if (!mDataList.contains(item)) {
                            Log.i(TAG, "图片添加至list:{${item._id}}")
                            mDataList.add(item)
                        } else {
                            Log.i(TAG, "数据中已含有该图片:{${item._id}}")
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                    srl_refresh.isRefreshing = false
                    isLoadingData = false
                }

                override fun onFailure(call: Call<PageGirlBean>, t: Throwable) {
                    Log.e(TAG, "onFailure$t")
                    showTip("数据加载失败")
                    srl_refresh.isRefreshing = false
                    isLoadingData = false
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            showTip("数据加载失败")
            srl_refresh.isRefreshing = false
            isLoadingData = false
        }
    }

    interface GetOnePic {
        @GET
        fun getCall(@Url url: String): Call<PageGirlBean>
    }

    private fun getRequestUrl(category: String, type: String, page: Int, count: Int): String {
        return "v2/data/category/$category/type/$type/page/$page/count/$count"
    }

}