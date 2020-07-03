package com.coding.girl.base

/**
 * @author: Coding.He
 * @date: 2020/7/3
 * @emil: 229101253@qq.com
 * @des:
 *
 *
 */

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


/**
 * Created by HDL on 2018/1/16.
 */
abstract class BaseFragment : Fragment() {
    private var mRootView: View? = null
    protected val TAG = this.javaClass.simpleName
    protected lateinit var ctx: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        Log.w(TAG, "onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w(TAG, "onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.w(TAG, "onCreateView()")
        if (mRootView == null) {
            mRootView = LayoutInflater.from(ctx).inflate(bindLayout(), null)
        }

        return mRootView!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.w(TAG, "onActivityCreated()")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
        initListener()
    }

    override fun onStart() {
        super.onStart()
        Log.w(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.w(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.w(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.w(TAG, "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.w(TAG, "onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.w(TAG, "onDetach()")
    }

    protected fun showTip(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    abstract fun bindLayout(): Int
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun initListener()
}