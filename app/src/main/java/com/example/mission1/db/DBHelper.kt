package com.example.mission1.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "studentdb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val studentSql = """
            CREATE TABLE tb_student (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                name NOT NULL,
                email,
                phone,
                photo,
                memo
            )
        """.trimIndent()
        db?.execSQL(studentSql)
    }

    // 혹시 db 버전이 바뀌었다면 해당 함수가 실행된다.
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS tb_student")
        onCreate(db)
    }
}