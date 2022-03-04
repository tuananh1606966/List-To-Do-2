package com.nghiemtuananh.sqlitesaveimagekpt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement

class Database(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {

    fun queryData(sql: String) {
        var database: SQLiteDatabase = writableDatabase
        database.execSQL(sql)
    }

    fun getData(sql: String): Cursor {
        var database: SQLiteDatabase = readableDatabase
        return database.rawQuery(sql, null)
    }

    fun insertDoVat(ten: String, moTa: String, hinh: ByteArray) {
        var database: SQLiteDatabase = writableDatabase
        var sql: String = "INSERT INTO DoVat VALUES(null, ?, ?, ?)"
        var statement: SQLiteStatement = database.compileStatement(sql)
        statement.clearBindings()
        statement.bindString(1, ten)
        statement.bindString(2, moTa)
        statement.bindBlob(3, hinh)

        statement.executeInsert()
    }

    fun update(hinh: ByteArray, ten: String, moTa: String, id: Int) {
        var database: SQLiteDatabase = writableDatabase
        var cvHinh = ContentValues()
        var cvTen = ContentValues()
        var cvMoTa = ContentValues()
        cvMoTa.put("MoTa", moTa)
        cvTen.put("Ten", ten)
        cvHinh.put("HinhAnh", hinh)
        database.update("DoVat", cvHinh, "Id = $id", null)
        database.update("DoVat", cvTen, "Id = $id", null)
        database.update("DoVat", cvMoTa, "Id = $id", null)
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}