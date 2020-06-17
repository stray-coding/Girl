package com.coding.girl.activity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.coding.girl.R
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailActivity"
        private const val REQ_PERMISSION = 200
    }

    lateinit var bitmap: Bitmap
    var pic_url = ""
    var pic_name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent != null) {
            if (intent.extras != null) {
                pic_url = intent.extras!!.getString("url", "")
                pic_name = intent.extras!!.getString("name", "") + ".jpg"
            }
        }

        Glide.with(this)
            .load(pic_url)
            .into(iv_detail_girl)
        iv_detail_girl.setOnLongClickListener { v ->
            bitmap = ((v as ImageView).drawable as BitmapDrawable).bitmap
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
                    bitmap,
                    pic_name,
                    "image/jpeg",
                    Bitmap.CompressFormat.JPEG
                )
            }
            true
        }
    }

    fun addBitmapToAlbum(
        bitmap: Bitmap,
        displayName: String?,
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
            Log.e(TAG,"e:$e")
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addBitmapToAlbum(
                        bitmap,
                        pic_name,
                        "image/jpeg",
                        Bitmap.CompressFormat.JPEG
                    )
                } else {
                    Toast.makeText(this, "权限不足，保存失败", Toast.LENGTH_SHORT).show()
                }
        }
    }
}