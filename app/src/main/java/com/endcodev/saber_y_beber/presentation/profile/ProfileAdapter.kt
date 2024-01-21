package com.endcodev.saber_y_beber.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.domain.model.ProfileModel
import com.endcodev.saber_y_beber.databinding.ProfileHolderBinding

class ProfileAdapter(
    private var activityList: List<ProfileModel>,
) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ProfileHolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ProfileHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return (ViewHolder(binding))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = activityList[position]

        holder.binding.profileQuest.text = currentItem.quest

        when (currentItem.result) {
            true -> holder.binding.profileImage.setImageResource(R.drawable.check_v)
            false -> holder.binding.profileImage.setImageResource(R.drawable.check_x)
        }
    }

    override fun getItemCount(): Int {
        return  activityList.size
    }
}