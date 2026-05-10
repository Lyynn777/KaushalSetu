package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushal.setu.R
import com.kaushal.setu.data.model.HireRequest
import com.kaushal.setu.databinding.ItemHireRequestBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HireRequestAdapter(
    private val onStatusChange: (String, String) -> Unit
) : ListAdapter<HireRequest, HireRequestAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHireRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemHireRequestBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: HireRequest) {
            b.tvName.text = r.customerName
            b.tvPhone.text = "📞 ${r.customerPhone}"
            b.tvService.text = r.serviceName.ifEmpty { "General" }
            b.tvMessage.text = r.message.ifEmpty { "No message" }
            b.tvDate.text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(r.createdAt))

            val ctx = b.root.context
            when (r.status) {
                "pending"  -> {
                    b.tvStatus.text = ctx.getString(R.string.status_pending)
                    b.tvStatus.setBackgroundResource(R.drawable.bg_pending)
                    b.btnAccept.isEnabled = true; b.btnReject.isEnabled = true
                }
                "accepted" -> {
                    b.tvStatus.text = ctx.getString(R.string.status_accepted)
                    b.tvStatus.setBackgroundResource(R.drawable.bg_available)
                    b.btnAccept.isEnabled = false; b.btnReject.isEnabled = false
                }
                "rejected" -> {
                    b.tvStatus.text = ctx.getString(R.string.status_rejected)
                    b.tvStatus.setBackgroundResource(R.drawable.bg_busy)
                    b.btnAccept.isEnabled = false; b.btnReject.isEnabled = false
                }
            }
            b.btnAccept.setOnClickListener { onStatusChange(r.id, "accepted") }
            b.btnReject.setOnClickListener { onStatusChange(r.id, "rejected") }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HireRequest>() {
            override fun areItemsTheSame(a: HireRequest, b: HireRequest) = a.id == b.id
            override fun areContentsTheSame(a: HireRequest, b: HireRequest) = a == b
        }
    }
}