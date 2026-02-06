package com.example.mission1.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mission1.DetailActivity
import com.example.mission1.R
import com.example.mission1.databinding.ItemMainBinding
import com.example.mission1.model.Student
import com.example.mission1.util.showCustomDialog
import com.example.mission1.util.showToast

class MainViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MainAdapter(val context: Activity, val datas: MutableList<Student>) :
    RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // 각 항목을 구성하기 위해 호출한다.
    override fun onBindViewHolder(
        holder: MainViewHolder,
        position: Int
    ) {
        val student = datas[position]

        holder.binding.itemNameView.text = student.name
        holder.binding.itemImageView.setOnClickListener {
            showCustomDialog(context, R.drawable.ic_student_large)
        }
        holder.binding.itemContactView.setOnClickListener {
            if (!student.phone.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${student.phone}"))
                context.startActivity(intent)
            } else {
                showToast(context, context.getString(R.string.main_list_phone_error))
            }
        }
        holder.binding.itemNameView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("id", student.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}