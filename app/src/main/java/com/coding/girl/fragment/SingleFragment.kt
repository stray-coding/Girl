package com.coding.girl.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.activity.DetailAct
import com.coding.girl.base.BaseFragment
import com.coding.girl.bean.RandomGirlBean
import kotlinx.android.synthetic.main.fragment_single.*
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
class SingleFragment : BaseFragment() {

    /*获取Girl图片URL的相关参数*/
    private val mCategory = "Girl"
    private val mType = "Girl"
    private val mCount = 1
    private var mGirlData: RandomGirlBean.DataBean? = null

    override fun bindLayout(): Int {
        return R.layout.fragment_single
    }

    override fun initData(savedInstanceState: Bundle?) {
        loadPic()
    }

    override fun initListener() {
        img_single.setOnClickListener {
            val intent = Intent()
            intent.setClass(context!!, DetailAct::class.java)
            val bundle = Bundle()
            bundle.putString("name", mGirlData?._id)
            bundle.putString("title", mGirlData?.title)
            bundle.putString("desc", mGirlData?.desc)
            bundle.putString("time", mGirlData?.publishedAt)
            bundle.putString("url", mGirlData?.url)
            intent.putExtras(bundle)
            activity?.startActivity(intent)
        }
        btn_refresh.setOnClickListener {
            loadPic()
        }
    }

    @Synchronized
    private fun loadPic() {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val request = retrofit.create(GetOnePic::class.java)

            val url = getRequestUrl(mCategory, mType, mCount)
            val call = request.getCall(url)
            call.enqueue(object : Callback<RandomGirlBean> {
                override fun onResponse(
                    call: Call<RandomGirlBean>,
                    response: Response<RandomGirlBean>
                ) {
                    Log.i(TAG, "{$response}")
                    Log.i(TAG, "{${response.body()}}")
                    val body = response.body() as RandomGirlBean
                    for (item in body.data) {
                        mGirlData = item
                        Glide.with(ctx)
                            .load(item.url)
                            .into(img_single)
                    }
                }

                override fun onFailure(call: Call<RandomGirlBean>, t: Throwable) {
                    Log.e(TAG, "onFailure$t")
                    showTip("图片刷新失败")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            showTip("图片刷新失败")
        }
    }

    interface GetOnePic {
        @GET
        fun getCall(@Url url: String): Call<RandomGirlBean>
    }

    private fun getRequestUrl(category: String, type: String, count: Int): String {
        return "v2/random/category/$category/type/$type/count/$count"
    }
}