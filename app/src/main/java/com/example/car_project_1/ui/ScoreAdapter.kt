package com.example.car_project_1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.car_project_1.data.ScoreRecord
import com.example.car_project_1.databinding.ItemScoreBinding

private var selectedIndex = RecyclerView.NO_POSITION


class ScoreAdapter(
    private val scores: List<ScoreRecord>,
    private val onClick: (ScoreRecord) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    private var selectedIndex = 0

    inner class ScoreViewHolder(val binding: ItemScoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {

        val adapterPos = holder.bindingAdapterPosition
        if (adapterPos == RecyclerView.NO_POSITION) return

        val score = scores[adapterPos]
        val rank = adapterPos + 1

        holder.binding.lblIndex.text = rank.toString()
        holder.binding.lblName.text = score.name
        holder.binding.lblScore.text =
            String.format("%.2f", score.km)


        holder.binding.card.setCardBackgroundColor(
            if (adapterPos == selectedIndex)
                0xFFFFE082.toInt()
            else
                0xFFFFFFFF.toInt()
        )


        val indexColor = when (rank) {
            1 -> "#FBC02D"
            2 -> "#9E9E9E"
            3 -> "#A1887F"
            else -> "#333333"
        }
        holder.binding.lblIndex.setTextColor(indexColor.toColorInt())


        holder.binding.root.setOnClickListener {
            selectedIndex = adapterPos
            notifyDataSetChanged()
            onClick(score)
        }


    }


    override fun getItemCount() = scores.size
}