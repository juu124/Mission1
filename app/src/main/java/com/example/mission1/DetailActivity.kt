package com.example.mission1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mission1.adapter.DetailAdapter
import com.example.mission1.databinding.ActivityDetailBinding
import com.example.mission1.db.DBHelper
import com.example.mission1.model.Student
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var student: Student

    // 점수 항목 구성 데이터
    lateinit var scoreList: MutableList<Map<String, String>>
    lateinit var adapter: DetailAdapter

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

        setInitStudentData(id)

        setInitScoreData(id)

        val addLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            result.data?.let {
                Log.d("jay", "111111111")
                // 데이터 추출
                val score = it.getStringExtra("score") ?: ""
                val date = it.getLongExtra("date", 0)

                // 항목 추가한다.
                // 항목 데이터는 map으로 준비한다.
                val map = mutableMapOf<String, String>()
                map["score"] = score
                // 시간 시스템 현재 시간
                // System.currentItmeMillis() -> Long 타입 : timestamp 값이다.
                // 유저가 보기 편한 형태로 만든다.
                val d = Date(date)  // 현재시간
                val sdFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

                map["date"] = sdFormat.format(d)
                scoreList.add(map)
                adapter.notifyDataSetChanged()

                binding.detailScore.score = score.toInt()
            }
        }

        binding.detailAddScoreBtn.setOnClickListener {
            val intent = Intent(this, ScoreAddActivity::class.java)
            intent.putExtra("id", id)
            addLauncher.launch(intent)
        }
    }

    // intent에서 넘어온 값을 DB에 매칭해서 추출한다.
    private fun setInitStudentData(id: Int) {
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
        val scoreCursor = db.rawQuery(
            "SELECT score FROM tb_score WHERE student_id = ? ORDER BY date DESC LIMIT 1",
            arrayOf(id.toString())
        )

        var score = 0
        if (scoreCursor.moveToFirst()) {
            score = scoreCursor.getInt(0)
        }

        binding.detailScore.score = score
    }


    private fun setInitScoreData(id: Int) {
        // db 에서 데이터 추출(select)
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery(
            "SELECT score, date FROM tb_score WHERE student_id = ? ORDER BY date",
            arrayOf(id.toString())
        )
        scoreList = mutableListOf()
        while (cursor.moveToNext()) {
            val map = mutableMapOf<String, String>()
            // index : select column 순서를 말하기 때문에
            // 여기에서 0은 score이다.
            map["score"] = cursor.getString(0)

            val d = Date(cursor.getString(1).toLong())
            val sd = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            map["date"] = sd.format(d)

            scoreList.add(map)
        }

        binding.detailRecyclerView.apply {
            // this - RecyclerView
            // RecyclerView에 adapter라는 변수가 있다.
            // 어댑터라는 변수가 있어서 중복이된다.
            // 우선적으로 RecyclerView의 객체의 변수가 우선적으로 둔 것 같다.
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = DetailAdapter(this@DetailActivity, scoreList).also {
                this@DetailActivity.adapter = it
            }
            addItemDecoration(
                DividerItemDecoration(
                    this@DetailActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}