package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushal.setu.R
import com.kaushal.setu.data.model.ServiceCard
import com.kaushal.setu.databinding.ItemServiceCardBinding

class ServiceCardAdapter(
    private val workerView: Boolean = false,
    private val onDelete: ((String) -> Unit)? = null,
    private val onEdit: ((ServiceCard) -> Unit)? = null
) : ListAdapter<ServiceCard, ServiceCardAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemServiceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemServiceCardBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(s: ServiceCard) {
            b.tvName.text        = s.serviceName
            b.tvPrice.text       = "₹${s.price}"
            b.tvDescription.text = s.description
            b.tvAvailability.text = if (s.isAvailable) "Available" else "Not Available"
            b.tvAvailability.setTextColor(
                b.root.context.getColor(
                    if (s.isAvailable) R.color.colorAvailable else R.color.colorBusy
                )
            )

            if (workerView) {
                b.btnEdit.visibility   = View.VISIBLE
                b.btnDelete.visibility = View.VISIBLE
                b.btnEdit.setOnClickListener   { onEdit?.invoke(s) }
                b.btnDelete.setOnClickListener { onDelete?.invoke(s.id) }
            } else {
                b.btnEdit.visibility   = View.GONE
                b.btnDelete.visibility = View.GONE
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ServiceCard>() {
            override fun areItemsTheSame(a: ServiceCard, b: ServiceCard) = a.id == b.id
            override fun areContentsTheSame(a: ServiceCard, b: ServiceCard) = a == b
        }
    }
}