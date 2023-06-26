package com.endcodev.saber_y_beber.presenter.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.ItemListRankingBinding

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

        when (position) {
            0 -> holder.binding.tvRankTrophy.setImageResource(R.drawable.cup_golden)
            1 -> holder.binding.tvRankTrophy.setImageResource(R.drawable.cup_silver)
            else -> holder.binding.tvRankTrophy.setImageResource(R.drawable.cup_copper)
        }

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
}