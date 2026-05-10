package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushal.setu.data.model.Review
import com.kaushal.setu.databinding.ItemReviewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: Review) {
            b.tvName.text = r.customerName
            b.tvComment.text = r.comment.ifEmpty { "No comment" }
            b.ratingBar.rating = r.rating
            b.tvRating.text = "%.1f".format(r.rating)
            b.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(r.createdAt))
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(a: Review, b: Review) = a.id == b.id
            override fun areContentsTheSame(a: Review, b: Review) = a == b
        }
    }
}