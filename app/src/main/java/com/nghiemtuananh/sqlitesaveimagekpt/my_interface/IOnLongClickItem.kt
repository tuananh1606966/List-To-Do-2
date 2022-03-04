package com.nghiemtuananh.sqlitesaveimagekpt.my_interface

interface IOnLongClickItem {

    fun onLongClickListener(tenCV: String, moTa: String, hinh: ByteArray, id: Int)
}