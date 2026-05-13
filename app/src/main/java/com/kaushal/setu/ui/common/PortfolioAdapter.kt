package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaushal.setu.databinding.ItemPortfolioImageBinding

class PortfolioAdapter : ListAdapter<String, PortfolioAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPortfolioImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemPortfolioImageBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(url: String) {
            Glide.with(b.root)
                .load(url)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(b.ivPhoto)
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(a: String, b: String) = a == b
            override fun areContentsTheSame(a: String, b: String) = a == b
        }
    }
}