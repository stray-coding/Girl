package com.coding.girl.base

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.coding.girl.R
import com.coding.girl.util.StatusBarUtils

/**
 * Created by HDL on 2018/1/15.
 */
abstract class BaseActivity : AppCompatActivity() {
    val TAG: String = javaClass.name

    /**
     * 是否沉浸状态栏
     */
    private var isSetStatusBar = true

    /**
     * 是否允许全屏
     */
    private var mAllowFullScreen = false

    /**
     * 是否禁止旋转屏幕
     */
    private var isAllowScreenRotate = false

    /**
     * 当前Activity渲染的视图View
     */
    private var mContextView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        Log.d(TAG, "BaseActivity-->onCreate()")
        if (mContextView == null) mContextView =
            LayoutInflater.from(this).inflate(bindLayout(), null)
        initDataBeforeSetContentView(savedInstanceState)
        if (mAllowFullScreen) {
            //隐藏action bar
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            //隐藏顶部状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        if (isSetStatusBar) steepStatusBar()
        if (!isAllowScreenRotate) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(mContextView)
        initDataAfterSetContentView(savedInstanceState)
        setListener()
    }

    /**
     * [沉浸状态栏]
     */
    @TargetApi(19)
    private fun steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setLightStatusBar(this, true)//黑色文字
            StatusBarUtils.setStatusBarColor(this, R.color.white)//白色背景
        } else {
            //6.0以下默认白色文字
            StatusBarUtils.setStatusBarColor(this, R.color.black)//黑色背景
        }
    }

    /**
     * [绑定布局]
     *
     * @return
     */
    abstract fun bindLayout(): Int

    /**
     * [加载数据]
     */
    abstract fun initDataBeforeSetContentView(savedInstanceState: Bundle?)
    abstract fun initDataAfterSetContentView(savedInstanceState: Bundle?)
    abstract fun setListener()

    /**
     * [页面跳转]
     *
     * @param clz
     */
    fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clz!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startActivityAndCloseThis(clz: Class<*>?) {
        startActivity(clz)
        this.finish()
    }

    fun startActivityAndCloseThis(clz: Class<*>?, bundle: Bundle?) {
        startActivity(clz, bundle)
        this.finish()
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    fun startActivityForResult(
        cls: Class<*>?,
        bundle: Bundle?,
        requestCode: Int
    ) {
        val intent = Intent()
        intent.setClass(this, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onRestart() {
        super.onRestart()
        Log.w(TAG, "onRestart()")
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

    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "onDestroy()")
    }

    override fun finish() {
        super.finish()
        Log.w(TAG, "finish()")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRotate
     */
    fun setScreenRotate(isAllowScreenRotate: Boolean) {
        this.isAllowScreenRotate = isAllowScreenRotate
    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    fun setAllowFullScreen(allowFullScreen: Boolean) {
        mAllowFullScreen = allowFullScreen
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    fun setSteepStatusBar(isSetStatusBar: Boolean) {
        this.isSetStatusBar = isSetStatusBar
    }
}