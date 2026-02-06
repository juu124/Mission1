package com.example.mission1.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mission1.databinding.ItemMainBinding
import com.example.mission1.model.Student

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
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}