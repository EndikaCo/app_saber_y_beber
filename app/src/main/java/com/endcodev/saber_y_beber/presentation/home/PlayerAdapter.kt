package com.endcodev.saber_y_beber.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.domain.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.PlayerHolderBinding

class PlayerAdapter(
    private var mList: List<PlayersModel>,
    private val onDeleteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PlayerHolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            PlayerHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return (ViewHolder(binding))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = mList[position]
        holder.binding.playerName.text = currentItem.name

        holder.binding.playerDelete.setOnClickListener {
            onDeleteClickListener.invoke(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mList.size)
        }

        when (currentItem.genre) {
            0 -> holder.binding.playerGenre.setImageResource(R.drawable.player_boy)
            1 -> holder.binding.playerGenre.setImageResource(R.drawable.player_girl)
            2 -> holder.binding.playerGenre.setImageResource(R.drawable.player_horse)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
