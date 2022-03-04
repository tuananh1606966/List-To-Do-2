package com.nghiemtuananh.sqlitesaveimagekpt

import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nghiemtuananh.sqlitesaveimagekpt.my_interface.IOnLongClickItem
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var database: Database
    }
    var listDoVat: ArrayList<DoVat> = arrayListOf()
    lateinit var adapter: DoVatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var linearLayoutManager = LinearLayoutManager(this)
        adapter = DoVatAdapter(this, R.layout.dong_do_vat, listDoVat, object : IOnLongClickItem{
            override fun onLongClickListener(tenCV: String, moTa: String, hinh: ByteArray, id: Int) {
                dialogAction(tenCV, moTa, hinh, id)
            }
        })
        rcv_dovat.layoutManager = linearLayoutManager
        var itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rcv_dovat.addItemDecoration(itemDecoration)
        rcv_dovat.adapter = adapter

        database = Database(this, "Quanly.sqlite", null, 1)

        database.queryData("CREATE TABLE IF NOT EXISTS DoVat(Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(150), MoTa VARCHAR(250), HinhAnh BLOB)")
//        database.queryData("DELETE FROM DoVat")

        btn_them.setOnClickListener {
            startActivity(Intent(this, ThemDoVatActivity::class.java))
        }
        getDataDoVat()
    }

    private fun getDataDoVat() {
        var cursor: Cursor = database.getData("SELECT * FROM DoVat")
        listDoVat.clear()
        while (cursor.moveToNext()) {
            listDoVat.add(DoVat(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3)
            ))
        }
        adapter.notifyDataSetChanged()
    }

    private fun dialogAction(tenCV: String, moTa: String, hinh: ByteArray, id: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_chinh_sua)
        val btnHuy: ImageButton = dialog.findViewById(R.id.ibtn_cancel)
        val btnXoa: Button = dialog.findViewById(R.id.btn_xoa_dialog)
        val btnSua: Button = dialog.findViewById(R.id.btn_sua_dialog)
        val txtTitle: TextView = dialog.findViewById(R.id.tv_title_dialog)
        btnSua.setOnClickListener {
            dialog.dismiss()
            intent = Intent(this, SuaDoVatActivity::class.java)
            var bundle = Bundle()
            bundle.putString("tenCV", tenCV)
            bundle.putString("moTa", moTa)
            bundle.putByteArray("hinh", hinh)
            bundle.putInt("id", id)
            intent.putExtra("dulieu", bundle)
            startActivity(intent)
        }
        btnXoa.setOnClickListener {
            database.queryData("DELETE FROM DoVat WHERE Id = '$id'")
            getDataDoVat()
            dialog.dismiss()
        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        txtTitle.setText("Bạn có muốn chỉnh sửa công việc với $tenCV không?")
        dialog.show()
    }
}