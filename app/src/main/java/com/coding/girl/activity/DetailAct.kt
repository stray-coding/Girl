package com.coding.girl.activity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.base.BaseActivity
import com.coding.girl.util.BitmapUtil
import kotlinx.android.synthetic.main.activity_details.*


class DetailAct : BaseActivity() {
    companion object {
        private const val REQ_PERMISSION = 200
    }

    private lateinit var mBitmap: Bitmap
    private var mPicUrl = ""
    private var mPicName = ""
    private var mDesc = ""

    override fun bindLayout(): Int {
        return R.layout.activity_details
    }

    override fun initDataBeforeSetContentView(savedInstanceState: Bundle?) {

    }

    override fun initDataAfterSetContentView(savedInstanceState: Bundle?) {
        if (intent != null && intent.extras != null) {
            mPicUrl = intent.extras!!.getString("url", "")
            mPicName = intent.extras!!.getString("name", "")
            if (!mPicName.endsWith(".jpg"))
                mPicName = "$mPicName.jpg"
            mDesc = intent.extras!!.getString("desc", "").replace("\n", "")
        }

        Glide.with(this)
            .load(mPicUrl)
            .into(img_detail_girl)

        tv_desc.text = mDesc
    }

    override fun setListener() {
        img_back.setOnClickListener {
            finish()
        }

        img_details.setOnClickListener {
            val dialogBuild = AlertDialog.Builder(this)
            dialogBuild.setTitle("保存图片")
                .setMessage("是否将图片保存到本地相册？")
                .setPositiveButton(
                    "保存"
                ) { dialog, which ->
                    mBitmap = BitmapUtil.drawable2Bitmap(img_detail_girl.drawable)
                    if (ActivityCompat.checkSelfPermission(
                            this@DetailAct,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@DetailAct,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_PERMISSION
                        )
                    } else {
                        addBitmapToAlbum(mBitmap, mPicName)
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    private fun addBitmapToAlbum(bitmap: Bitmap, displayName: String) {
        try {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            } else {
                values.put(
                    MediaStore.MediaColumns.DATA,
                    "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
                )
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                    showTip("保存成功")
                }
            } else {
                Log.i(TAG, "保存失败,该图片已在图库中")
                showTip("保存失败,该图片已在图库中")
            }
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            showTip("未知错误，保存失败")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_PERMISSION ->
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addBitmapToAlbum(mBitmap, mPicName)
                } else {
                    showTip("权限不足，保存失败")
                }
        }
    }

}