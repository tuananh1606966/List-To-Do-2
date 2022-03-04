package com.nghiemtuananh.sqlitesaveimagekpt

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_them_do_vat.*
import java.io.ByteArrayOutputStream

class ThemDoVatActivity : AppCompatActivity() {
    val REQUEST_CODE_CAMERA = 1
    val REQUEST_CODE_FOLDER = 2
    var check = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_them_do_vat)

        btn_add.setOnClickListener {
            // chuyển data của iv sang mảng byte

            if (edt_tendovat.text.toString().equals("") || edt_mota.text.toString()
                    .equals("") || check
            ) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show()
            } else {
                var bitmapDrawable: BitmapDrawable = iv_hinh.drawable as BitmapDrawable
                var bitmap: Bitmap = bitmapDrawable.bitmap
                var byteArray: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
                var hinhAnh: ByteArray = byteArray.toByteArray()

                hinhAnh = reSizeImg(hinhAnh)

                MainActivity.database.insertDoVat(
                    edt_tendovat.text.toString().trim(),
                    edt_mota.text.toString().trim(),
                    hinhAnh
                )
                Toast.makeText(this, "Đã thêm", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        btn_huy.setOnClickListener {
            finish()
        }

        ibtn_camera.setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        ibtn_folder.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_FOLDER)
        }
    }

    private fun reSizeImg(hinhAnh: ByteArray): ByteArray {
        var hinh: ByteArray = hinhAnh
        while (hinh.size > 500000) {
            val bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.size)
            val resized = Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * 0.8).toInt(), (bitmap.height * 0.8).toInt(), true)
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            hinh = stream.toByteArray()
        }
        return hinh
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            var bitmap: Bitmap = data.extras!!.get("data") as Bitmap
            iv_hinh.setImageBitmap(bitmap)
            check = false
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            var uri: Uri = data.data as Uri
            var inputStream = contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            iv_hinh.setImageBitmap(bitmap)
            check = false
        }
    }
}