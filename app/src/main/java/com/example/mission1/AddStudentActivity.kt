package com.example.mission1

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mission1.databinding.ActivityAddStudentBinding
import com.example.mission1.db.DBHelper
import com.example.mission1.util.showToast

class AddStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_save) {
            save()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        // 유저 입력 데이터를 추출해야한다.
        val name = binding.addName.text.toString()
        val email = binding.addEmail.text.toString()
        val phone = binding.addPhone.text.toString()
        val memo = binding.addMemo.text.toString()

        // 유효성을 검증 :  name not null check로 하는 실습 1개만 진행
        if (name.isEmpty()) {
            showToast(this, getString(R.string.add_name_null))
        } else {
            // db 저장
            val db = DBHelper(this).writableDatabase
            val values = ContentValues().apply {
                put("name", name)
                put("email", email)
                put("phone", phone)
                put("memo", memo)
            }
            // insert 시키면서 새로운 row의 id(primary key) 획득한다
            // MainAcivity에 신규 저장된 데이터의 id도 넘겨야 하기 때문에
            // Main -> Detail로 넘어갈 때 id가 필요하다.
            val newRowId = db.insert("tb_student", null, values)
            db.close()

            // 결과데이터 포함.. 되돌리기..
            // intent에 결과 데이터 포함..
            intent.apply {
                putExtra("id", newRowId)
                putExtra("name", name)
                putExtra("email", email)
                putExtra("phone", phone)
                putExtra("memo", memo)
            }
            // 결과 상태 ok.. cancel
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}