package com.nghiemtuananh.sqlitesaveimagekpt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nghiemtuananh.sqlitesaveimagekpt.my_interface.IOnLongClickItem

class DoVatAdapter(
    var context: Context,
    var layout: Int,
    var listDoVat: List<DoVat>,
    var iOnLongClickItem: IOnLongClickItem,
) : RecyclerView.Adapter<DoVatAdapter.DoVatViewHolder>() {
    inner class DoVatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTen: TextView
        var tvMoTa: TextView
        var imgHinh: ImageView
        var viewDoVat: LinearLayout

        init {
            tvTen = itemView.findViewById(R.id.tv_ten_custom)
            tvMoTa = itemView.findViewById(R.id.tv_mota_custom)
            imgHinh = itemView.findViewById(R.id.iv_hinh_custom)
            viewDoVat = itemView.findViewById(R.id.ll_dovat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoVatViewHolder {
        var view = LayoutInflater.from(context).inflate(layout, parent, false)
        return DoVatViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoVatViewHolder, position: Int) {
        var doVat = listDoVat.get(position)
        if (doVat == null) {
            return
        }
        holder.tvTen.setText(doVat.ten)
        holder.tvMoTa.setText(doVat.moTa)
        // chuyển mảng byte sang kiểu bitmap
        var bitmap: Bitmap? = BitmapFactory.decodeByteArray(doVat.hinh, 0, doVat.hinh.size)
        holder.imgHinh.setImageBitmap(bitmap)
        holder.viewDoVat.setOnLongClickListener {
            iOnLongClickItem.onLongClickListener(doVat.ten, doVat.moTa, doVat.hinh, doVat.id)
            return@setOnLongClickListener false
        }
    }

    override fun getItemCount(): Int {
        return listDoVat.size
    }
}