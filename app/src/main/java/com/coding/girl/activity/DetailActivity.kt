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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.coding.girl.R
import com.coding.girl.base.BaseActivity
import com.coding.girl.util.BitmapUtil
import kotlinx.android.synthetic.main.activity_details.*


class DetailActivity : BaseActivity() {
    companion object {
        private const val REQ_PERMISSION = 200
    }

    private lateinit var mBitmap: Bitmap
    private var mPicUrl = ""
    private var mPicName = ""

    override fun bindLayout(): Int {
        return R.layout.activity_details
    }

    override fun initDataBeforeContentView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        if (intent != null && intent.extras != null) {
            mPicUrl = intent.extras!!.getString("url", "")
            mPicName = intent.extras!!.getString("name", "")
            if (!mPicName.endsWith(".jpg"))
                mPicName = "$mPicName.jpg"
        }

        Glide.with(this)
            .load(mPicUrl)
            .into(img_detail_girl)
    }

    override fun setListener() {
        tv_back.setOnClickListener {
            finish()
        }
        tv_round.setOnClickListener {
            val roundBitmap = RoundedBitmapDrawableFactory.create(
                resources,
                BitmapUtil.drawable2Bitmap(img_detail_girl.drawable)
            )
            roundBitmap.cornerRadius = 500f
            Glide.with(this)
                .load(roundBitmap)
                .into(img_detail_girl)
        }
        tv_store_pic.setOnClickListener {
            mBitmap = BitmapUtil.drawable2Bitmap(img_detail_girl.drawable)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_PERMISSION
                )
            } else {
                addBitmapToAlbum(
                    mBitmap,
                    mPicName,
                    "image/jpeg",
                    Bitmap.CompressFormat.JPEG
                )
            }
        }
    }

    private fun addBitmapToAlbum(
        bitmap: Bitmap,
        displayName: String,
        mimeType: String,
        compressFormat: Bitmap.CompressFormat
    ) {
        try {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
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
                    bitmap.compress(compressFormat, 100, outputStream)
                    outputStream.close()
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "e:$e")
            Toast.makeText(this, "未知错误，保存失败", Toast.LENGTH_SHORT).show()
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
                    addBitmapToAlbum(
                        mBitmap,
                        mPicName,
                        "image/jpeg",
                        Bitmap.CompressFormat.JPEG
                    )
                } else {
                    Toast.makeText(this, "权限不足，保存失败", Toast.LENGTH_SHORT).show()
                }
        }
    }

}