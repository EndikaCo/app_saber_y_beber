package com.endcodev.saber_y_beber.presenter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.saber_y_beber.data.model.ProfileModel
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

        holder.binding.profileDate.text = currentItem.date
        holder.binding.profileUser.text = currentItem.user
        holder.binding.profileQuest.text = currentItem.quest
    }

    override fun getItemCount(): Int {
        return  activityList.size
    }
}