package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaushal.setu.R
import com.kaushal.setu.data.model.WorkerProfile
import com.kaushal.setu.databinding.ItemWorkerCardBinding

class WorkerCardAdapter(
    private val onClick: (WorkerProfile) -> Unit
) : ListAdapter<WorkerProfile, WorkerCardAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemWorkerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemWorkerCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(w: WorkerProfile) {
            b.tvName.text = w.name
            b.tvCategory.text = w.skillCategory
            b.tvLocation.text = w.location
            b.tvExp.text = b.root.context.getString(R.string.years_exp, w.yearsOfExperience)
            b.tvRating.text = "%.1f ★".format(w.averageRating)
            b.chipStatus.text = if (w.isAvailable)
                b.root.context.getString(R.string.status_available)
            else
                b.root.context.getString(R.string.status_busy)
            b.chipStatus.setChipBackgroundColorResource(
                if (w.isAvailable) R.color.colorAvailable else R.color.colorBusy
            )
            if (w.profileImageUrl.isNotEmpty())
                Glide.with(b.root).load(w.profileImageUrl).circleCrop().into(b.ivAvatar)
            else
                b.ivAvatar.setImageResource(R.drawable.ic_person_placeholder)
            b.root.setOnClickListener { onClick(w) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<WorkerProfile>() {
            override fun areItemsTheSame(a: WorkerProfile, b: WorkerProfile) = a.uid == b.uid
            override fun areContentsTheSame(a: WorkerProfile, b: WorkerProfile) = a == b
        }
    }
}