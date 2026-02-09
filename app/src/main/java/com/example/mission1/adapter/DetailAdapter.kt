package com.example.mission1.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mission1.databinding.ItemDetailBinding

class DetailViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)

// 점수, 날짜로 클래스를 만들기에는 너무 작은 걸 묶는 것 같은 느낌이 들어서
// map형식으로 해서 전달한다.
class DetailAdapter(val context: Activity, val datas: MutableList<Map<String, String>>) :
    RecyclerView.Adapter<DetailViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder {
        return DetailViewHolder(
            ItemDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: DetailViewHolder,
        position: Int
    ) {
        // 항목 데이터를 각 항목에 출력
        holder.binding.detailItemScore.text = datas[position]["score"]
        holder.binding.detailItemDate.text = datas[position]["date"]
    }

    override fun getItemCount() = datas.size
}