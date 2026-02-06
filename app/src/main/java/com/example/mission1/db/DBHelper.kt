package com.example.mission1.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "studentdb", null, 2) {
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

        // student_id는 tb_student의 _id를 가져간다. : 어느 학생의 시험점수인지 알기 위해서
        val scoreSql = """
            CREATE TABLE tb_score (
            _id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id NOT NULL,
            date,
            score
            )
        """.trimIndent()
        db?.execSQL(scoreSql)
    }

    // 혹시 db 버전이 바뀌었다면 해당 함수가 실행된다.
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE tb_student")
        onCreate(db)
    }
}