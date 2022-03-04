package com.nghiemtuananh.sqlitesaveimagekpt

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sua_do_vat.*
import kotlinx.android.synthetic.main.activity_sua_do_vat.btn_huy
import kotlinx.android.synthetic.main.activity_sua_do_vat.edt_mota
import kotlinx.android.synthetic.main.activity_sua_do_vat.edt_tendovat
import kotlinx.android.synthetic.main.activity_sua_do_vat.ibtn_camera
import kotlinx.android.synthetic.main.activity_sua_do_vat.ibtn_folder
import kotlinx.android.synthetic.main.activity_sua_do_vat.iv_hinh
import kotlinx.android.synthetic.main.activity_them_do_vat.*
import java.io.ByteArrayOutputStream

class SuaDoVatActivity : AppCompatActivity() {
    val REQUEST_CODE_CAMERA = 1
    val REQUEST_CODE_FOLDER = 2
    var tenCV = ""
    var moTa = ""
    lateinit var hinh: ByteArray
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sua_do_vat)
        var bundle = intent.getBundleExtra("dulieu")
        if (bundle != null) {
            tenCV = bundle.getString("tenCV").toString()
            moTa = bundle.getString("moTa").toString()
            hinh = bundle.getByteArray("hinh")!!
            id = bundle.getInt("id")
            edt_tendovat.setText(tenCV)
            edt_mota.setText(moTa)
            var bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh!!.size)
            iv_hinh.setImageBitmap(bitmap)
        }

        btn_sua.setOnClickListener {
            tenCV = edt_tendovat.text.toString().trim()
            moTa = edt_mota.text.toString().trim()
            if (tenCV.equals("") || moTa.equals("")) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show()
            } else {
                var bitmapDrawable: BitmapDrawable = iv_hinh.drawable as BitmapDrawable
                var bitmap: Bitmap = bitmapDrawable.bitmap
                var byteArray: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
                hinh = byteArray.toByteArray()

                hinh = reSizeImg(hinh)

                MainActivity.database.update(hinh, tenCV, moTa, id)
//                MainActivity.database.queryData("UPDATE DoVat SET Ten = '$tenCV', MoTa = '$moTa', HinhAnh = '$hinh' WHERE Id = '$id'")
//                MainActivity.database.queryData("DELETE FROM DoVat WHERE Id = '$id'")
//                MainActivity.database.insertDoVat(tenCV, moTa, hinh)

                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_LONG).show()
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

    private fun reSizeImg(hinh: ByteArray): ByteArray {
        var hinhAnh: ByteArray = hinh
        while (hinhAnh.size > 500000) {
            val bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.size)
            val resized = Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * 0.8).toInt(), (bitmap.height * 0.8).toInt(), true)
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            hinhAnh = stream.toByteArray()
        }
        return hinhAnh
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            var bitmap: Bitmap = data.extras!!.get("data") as Bitmap
            iv_hinh.setImageBitmap(bitmap)
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            var uri: Uri = data.data as Uri
            var inputStream = contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            iv_hinh.setImageBitmap(bitmap)
        }
    }
}