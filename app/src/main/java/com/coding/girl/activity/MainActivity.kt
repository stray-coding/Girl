package com.coding.girl.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.coding.girl.R
import com.coding.girl.adapter.GirlAdapter
import com.coding.girl.base.BaseActivity
import com.coding.girl.bean.GirlBean
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


class MainActivity : BaseActivity() {
    private var isLoadData = false

    /*获取Girl图片URL的相关参数*/
    private val mCategory = "Girl"
    private val mType = "Girl"
    private val mCount = 8
    private var mPage = 1

    private val mDataList: ArrayList<GirlBean.DataBean> = arrayListOf()
    private lateinit var mAdapter: GirlAdapter

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initDataBeforeSetContentView(savedInstanceState: Bundle?) {

    }

    override fun initDataAfterSetContentView(savedInstanceState: Bundle?) {
        loadPic(false)
        val spanCount = 2
        rv_list.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = GirlAdapter(this@MainActivity, mDataList)
        rv_list.adapter = mAdapter
        srl_refresh.setColorSchemeResources(R.color.black, R.color.green, R.color.bule)
    }

    override fun setListener() {
        srl_refresh.setOnRefreshListener {
            loadPic(true)
        }
        mAdapter.setOnItemClickListener { _, pos ->
            val bundle = Bundle()
            bundle.putString("name", mAdapter.getItem(pos)._id)
            bundle.putString("title", mAdapter.getItem(pos).title)
            bundle.putString("desc", mAdapter.getItem(pos).desc)
            bundle.putString("time", mAdapter.getItem(pos).publishedAt)
            bundle.putString("url", mAdapter.getItem(pos).url)
            startActivity(DetailActivity::class.java, bundle)
        }

        var lastVisibleItemPosition = 0
        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = rv_list.layoutManager as StaggeredGridLayoutManager
                    val visibleItemCount = manager.childCount
                    val totalItemCount = rv_list.adapter!!.itemCount
                    if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                        Log.i(TAG, "onScrollStateChanged: ...");
                        loadPic(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = rv_list.layoutManager as StaggeredGridLayoutManager
                val lastPositions = IntArray(manager.spanCount)
                manager.findLastCompletelyVisibleItemPositions(lastPositions)
                if (lastPositions.isNotEmpty()) {
                    lastVisibleItemPosition = lastPositions.max()!!
                }
            }
        })
    }

    @Synchronized
    private fun loadPic(isRefresh: Boolean) {
        if (isLoadData) {
            Log.i(TAG, "数据正在加载中，请勿重复刷新")
            return
        }
        try {
            isLoadData = true
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val request = retrofit.create(GetOnePic::class.java)
            val url = getRequestUrl(mCategory, mType, mPage, mCount)
            val call = request.getCall(url)
            call.enqueue(object : Callback<GirlBean> {
                override fun onResponse(call: Call<GirlBean>, response: Response<GirlBean>) {
                    Log.i(TAG, "{$response}")
                    Log.i(TAG, "{${response.body()}}")
                    val body = response.body() as GirlBean
                    if (isRefresh) {
                        mDataList.clear()
                        mPage = 1
                    }
                    mPage++
                    for (item in body.data) {
                        if (!mDataList.contains(item)) {
                            Log.i(TAG, "图片添加至list:{${item._id}}")
                            mDataList.add(item)
                        } else {
                            Log.i(TAG, "数据中已含有该图片:{${item._id}}")
                        }

                    }
                    //mDataList.addAll(body.data)
                    mAdapter.notifyDataSetChanged()
                    srl_refresh.isRefreshing = false
                    isLoadData = false
                }

                override fun onFailure(call: Call<GirlBean>, t: Throwable) {
                    Log.e(TAG, "onFailure$t")
                    Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
                    srl_refresh.isRefreshing = false
                    isLoadData = false
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            srl_refresh.isRefreshing = false
            Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
            isLoadData = false
        }
    }

    interface GetOnePic {
        @GET
        fun getCall(@Url url: String): Call<GirlBean>
    }

    private fun getRequestUrl(category: String, type: String, page: Int, count: Int): String {
        return "v2/data/category/$category/type/$type/page/$page/count/$count"
    }

}

