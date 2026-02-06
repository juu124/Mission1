package com.example.mission1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mission1.adapter.MainAdapter
import com.example.mission1.callback.DialogCallback
import com.example.mission1.callback.PermissionCallback
import com.example.mission1.databinding.ActivityMainBinding
import com.example.mission1.db.DBHelper
import com.example.mission1.model.Student
import com.example.mission1.util.checkAllPermission
import com.example.mission1.util.showMessageDialog

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var addLauncher: ActivityResultLauncher<Intent>
    val datas = mutableListOf<Student>()
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // activity를 되돌아 왔을 때 결과 데이터를 추출한다.
            it.data?.let { intent ->
                val id = intent.getIntExtra("id", 0)
                val name = intent.getStringExtra("name")
                val email = intent.getStringExtra("email")
                val phone = intent.getStringExtra("phone")
                val photo = intent.getStringExtra("photo")
                val memo = intent.getStringExtra("memo")

                // 결과 데이터로 항목 하나추가한다.
                val student = Student(id, name, email, phone, photo, memo)
                // adapter 데이터에 추가한 후
                datas.add(student)
                // 반영 명령
                adapter.notifyDataSetChanged()
            }
        }

        // 리스트 만드는 함수
        // 아래 권한에서 리스트 만들도록 수정됨
        // makeRecyclerView()

        // 앱을 위한 모든 퍼미션 체크
        checkAllPermission(this as ComponentActivity, object  : PermissionCallback {
            override fun onPermissionResult(isAllGranted: Boolean) {
                if (isAllGranted) {
                    // 권한이 허락되면 그때 리스트를 그리겠다.
                    makeRecyclerView()
                } else {
                    showDialog()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_main_add) {
            val intent = Intent(this, AddStudentActivity::class.java)
            addLauncher.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    // db select 역할
    private fun getListData() {
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_student ORDER BY name", null)

        // db 에서 신규 데이터를 추출한다.
        // 혹시 이전에 List에 데이터가 추가되었다면 모두 제거하고 다시 구축한다.
        datas.clear()

        cursor.run {
            while (moveToNext()) {
                datas.add(
                    Student(
                        id = getInt(0),
                        name = getString(1),
                        email = getString(2),
                        phone = getString(3),
                        photo = getString(4),
                        memo = getString(5)
                    )
                )
            }
        }
    }

    private fun makeRecyclerView() {
        getListData()

        adapter = MainAdapter(this, datas)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun showDialog() {
        showMessageDialog(
            this,
            getString(R.string.permission_denied),
            "확인",
            null,
            object : DialogCallback {
                override fun onPositiveCallBack() {
                    finish()
                }

                override fun onNegativeCallBack() {

                }
            })
    }
}