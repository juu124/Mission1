package com.example.mission1

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mission1.databinding.ActivityScoreAddBinding
import com.example.mission1.db.DBHelper
import com.example.mission1.util.showToast

class ScoreAddActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityScoreAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScoreAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.run {
            key0.setOnClickListener(this@ScoreAddActivity)
            key1.setOnClickListener(this@ScoreAddActivity)
            key2.setOnClickListener(this@ScoreAddActivity)
            key3.setOnClickListener(this@ScoreAddActivity)
            key4.setOnClickListener(this@ScoreAddActivity)
            key5.setOnClickListener(this@ScoreAddActivity)
            key6.setOnClickListener(this@ScoreAddActivity)
            key7.setOnClickListener(this@ScoreAddActivity)
            key8.setOnClickListener(this@ScoreAddActivity)
            key9.setOnClickListener(this@ScoreAddActivity)
            keyBack.setOnClickListener(this@ScoreAddActivity)
            keyAdd.setOnClickListener(this@ScoreAddActivity)
        }
    }

    // 매개변수가 현재 이벤트가 발생한 객체이다.
    override fun onClick(v: View?) {
        when (v) {
            binding.keyAdd -> {
                // db insert하기 위한 이벤트
                // insert하기위한 데이터를 준비해야한다.
                // 어느 학생인지 id로 구별
                val id = intent.getIntExtra("id", 0)
                val date = System.currentTimeMillis()
                val score = binding.keyEdit.text.toString()

                val db = DBHelper(this).writableDatabase
                db.execSQL(
                    "INSERT INTO tb_score (student_id, date, score) VALUES (?, ?, ?)",
                    arrayOf(id.toString(), date.toString(), score)
                )
                db.close()

                // add가 끝났으니
                // 결과를 포함해서 이전 화면으로 전환한다.
                intent.apply {
                    putExtra("score", score)
                    putExtra("date", date)
                }
                setResult(RESULT_OK, intent)
                finish()
            }

            binding.keyBack -> {
                // 현재 화면에 출력되는 score를 획득하고
                val score = binding.keyEdit.text.toString()
                if (score.length == 1) {
                    binding.keyEdit.text = "0"
                } else {
                    val newScore = score.substring(0, score.length - 1)
                    binding.keyEdit.text = newScore
                }
            }

            else -> {
                // 버튼의 문자열을 획득해서 숫자로 사용한다.
                val btn = v as Button
                val txt = btn.text.toString()
                val score = binding.keyEdit.text.toString()
                if (score == "0") {
                    binding.keyEdit.text = txt
                } else {
                    val newScore = score + txt
                    val intScore = newScore.toInt()
                    if (intScore > 100) {
                        showToast(this, getString(R.string.score_over))
                    } else {
                        binding.keyEdit.text = newScore
                    }
                }
            }
        }
    }
}