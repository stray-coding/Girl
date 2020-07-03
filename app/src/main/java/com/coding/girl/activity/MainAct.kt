package com.coding.girl.activity

import android.os.Bundle
import com.coding.girl.R
import com.coding.girl.base.BaseActivity
import com.coding.girl.base.BaseFragment
import com.coding.girl.fragment.AboutFragment
import com.coding.girl.fragment.BeautyFragment
import com.coding.girl.fragment.SingleFragment
import kotlinx.android.synthetic.main.bottom_bar.*

/**
 * @author: Coding.He
 * @date: 2020/7/3
 * @emil: 229101253@qq.com
 * @des:
 */
class MainAct : BaseActivity() {
    private val aboutFragment = AboutFragment()
    private val beautyFragment = BeautyFragment()
    private val singleFragment = SingleFragment()
    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initDataBeforeSetContentView(savedInstanceState: Bundle?) {

    }

    override fun initDataAfterSetContentView(savedInstanceState: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_main, beautyFragment, "beauty")
        transaction.add(R.id.fl_main, singleFragment, "single")
        transaction.add(R.id.fl_main, aboutFragment, "about")
        transaction.commit()
        show(beautyFragment)
    }

    override fun setListener() {
        tv_beauty.setOnClickListener {
            show(beautyFragment)
        }
        tv_single.setOnClickListener {
            show(singleFragment)
        }
        tv_about.setOnClickListener {
            show(aboutFragment)
        }
    }

    private fun show(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(beautyFragment)
        transaction.hide(singleFragment)
        transaction.hide(aboutFragment)
        transaction.show(fragment)
        transaction.commit()
    }
}