package com.example.mission1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mission1.databinding.ActivityDetailBinding
import com.example.mission1.db.DBHelper
import com.example.mission1.model.Student

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var student: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        // 나를 실행시킨 intent에서 id값 획득하기
        val id = intent.getIntExtra("id", 0)

        setInitsStudentData(id)
    }

    // intent에서 넘어온 값을 DB에 매칭해서 추출한다.
    private fun setInitsStudentData(id: Int) {
        // db select
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_student WHERE _id = ?", arrayOf(id.toString()))

        var photoFilePath: String? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(1)
            val email = cursor.getString(2)
            val phone = cursor.getString(3)

            // 화면 출력
            binding.detailName.text = name
            binding.detailEmail.text = email
            binding.detailPhone.text = phone

            photoFilePath = cursor.getString(4)

            student = Student(id, name, email, phone, photoFilePath, cursor.getString(5))
        }
    }
}