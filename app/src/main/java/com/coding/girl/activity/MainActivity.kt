package com.coding.girl.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.base.BaseRecyclerAdapter
import com.coding.girl.base.RecyclerViewHolder
import com.coding.girl.bean.GirlPicBean
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val mDataList: ArrayList<GirlPicBean.DataBean> = arrayListOf()
    private lateinit var mAdapter: GirlAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPic()
        val spanCount = 2
        rv_list.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = GirlAdapter(this@MainActivity, mDataList)
        rv_list.adapter = mAdapter
        mAdapter.setOnItemClickListener { itemView, pos ->
            val intent = Intent()
            intent.setClass(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("url",mAdapter.getItem(pos).url)
            intent.putExtra("name",mAdapter.getItem(pos)._id)
            startActivity(intent)
        }
    }

    class GirlAdapter(context: Context, dataList: ArrayList<GirlPicBean.DataBean>) :
        BaseRecyclerAdapter<GirlPicBean.DataBean>(context, dataList) {
        override fun getItemLayoutId(viewType: Int): Int {
            return R.layout.item_girl
        }

        override fun bindData(
            holder: RecyclerViewHolder,
            position: Int,
            item: GirlPicBean.DataBean?
        ) {
            if (position==0){
                val layout = holder.getView(R.id.ll_layout)
                val layoutParams= LinearLayout.LayoutParams(layout.layoutParams)
                layoutParams.topMargin = 20
            }
            val img = holder.getImageView(R.id.iv_girl)
            Glide.with(mContext)
                .load(item!!.url)
                .into(img)
            holder.setText(R.id.tv_msg, item.desc)
        }
    }

    fun loadPic() {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val request = retrofit.create(Get_One_Pic::class.java)
            val call = request.getCall()
            call.enqueue(object : Callback<GirlPicBean> {
                override fun onResponse(call: Call<GirlPicBean>, response: Response<GirlPicBean>) {
                    Log.i(TAG, "{$response}")
                    Log.i(TAG, "{${response.body()}}")
                    val body = response.body() as GirlPicBean
                    mDataList.addAll(body.data)
                    mAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<GirlPicBean>, t: Throwable) {
                    Log.e(TAG, "onFailure$t")
                    Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            Toast.makeText(this@MainActivity, "数据加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    interface Get_One_Pic {
        @GET("v2/random/category/Girl/type/Girl/count/20")
        fun getCall(): Call<GirlPicBean>
    }

}

