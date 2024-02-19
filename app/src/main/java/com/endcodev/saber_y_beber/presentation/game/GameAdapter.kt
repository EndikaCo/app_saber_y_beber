package com.endcodev.saber_y_beber.presentation.game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.saber_y_beber.databinding.ItemListRankingBinding
import com.endcodev.saber_y_beber.domain.model.PlayersModel

class GameAdapter(
    private var playerList: List<PlayersModel>,
    private val onClickListener: (Boolean) -> Unit
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListRankingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemListRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return (ViewHolder(binding))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = playerList[position]

        holder.binding.tvRankName.text = currentItem.name
        holder.binding.tvPoints.text = currentItem.points.toString()

        holder.binding.tvRankName.setOnClickListener {
            onClickListener.invoke(true)
        }
    }

    override fun getItemCount(): Int {
        val size = playerList.size
        return if (size > 3)
            3
        else
            size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortListByPoints() {
        playerList = playerList.sortedByDescending { it.points }
        notifyDataSetChanged()
    }
}