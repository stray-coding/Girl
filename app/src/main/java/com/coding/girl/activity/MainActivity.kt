package com.coding.girl.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

class MainActivity : BaseActivity() {

    private val mDataList: ArrayList<GirlBean.DataBean> = arrayListOf()
    private lateinit var mAdapter: GirlAdapter

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initDataBeforeContentView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        loadPic()
        val spanCount = 2
        rv_list.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = GirlAdapter(this@MainActivity, mDataList)
        rv_list.adapter = mAdapter
    }

    override fun setListener() {
        mAdapter.setOnItemClickListener { _, pos ->
            val bundle = Bundle()
            bundle.putString("name", mAdapter.getItem(pos)._id)
            bundle.putString("title", mAdapter.getItem(pos).title)
            bundle.putString("desc", mAdapter.getItem(pos).desc)
            bundle.putString("time", mAdapter.getItem(pos).publishedAt)
            bundle.putString("url", mAdapter.getItem(pos).url)
            startActivity(DetailActivity::class.java, bundle)
        }
    }

    private fun loadPic() {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val request = retrofit.create(GetOnePic::class.java)
            val call = request.getCall()
            call.enqueue(object : Callback<GirlBean> {
                override fun onResponse(call: Call<GirlBean>, response: Response<GirlBean>) {
                    Log.i(TAG, "{$response}")
                    Log.i(TAG, "{${response.body()}}")
                    val body = response.body() as GirlBean
                    mDataList.addAll(body.data)
                    mAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<GirlBean>, t: Throwable) {
                    Log.e(TAG, "onFailure$t")
                    Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    interface GetOnePic {
        @GET("v2/random/category/Girl/type/Girl/count/1")
        fun getCall(): Call<GirlBean>
    }

}

