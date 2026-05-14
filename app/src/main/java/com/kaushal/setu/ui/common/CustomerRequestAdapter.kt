package com.kaushal.setu.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushal.setu.R
import com.kaushal.setu.data.model.HireRequest
import com.kaushal.setu.databinding.ItemCustomerRequestBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerRequestAdapter :
    ListAdapter<HireRequest, CustomerRequestAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCustomerRequestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemCustomerRequestBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(r: HireRequest) {
            b.tvWorkerName.text  = r.workerName
            b.tvService.text     = r.serviceName.ifEmpty { "General Service" }
            b.tvMessage.text     = r.message.ifEmpty { "No message sent" }
            b.tvDate.text        = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault()
            ).format(Date(r.createdAt))

            when (r.status) {
                "pending" -> {
                    b.tvStatus.text = "Pending"
                    b.tvStatus.setBackgroundResource(R.drawable.bg_pending)
                    b.tvStatus.setTextColor(
                        b.root.context.getColor(R.color.colorPending)
                    )
                    b.tvStatusMessage.text = "Your request is waiting for the worker to respond."
                }
                "accepted" -> {
                    b.tvStatus.text = "Accepted ✓"
                    b.tvStatus.setBackgroundResource(R.drawable.bg_available)
                    b.tvStatus.setTextColor(
                        b.root.context.getColor(R.color.colorAvailable)
                    )
                    b.tvStatusMessage.text = "Worker accepted your request. Contact them to confirm details."
                }
                "rejected" -> {
                    b.tvStatus.text = "Rejected ✗"
                    b.tvStatus.setBackgroundResource(R.drawable.bg_busy)
                    b.tvStatus.setTextColor(
                        b.root.context.getColor(R.color.colorBusy)
                    )
                    b.tvStatusMessage.text = "Worker is unavailable. Try another worker."
                }
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HireRequest>() {
            override fun areItemsTheSame(a: HireRequest, b: HireRequest) = a.id == b.id
            override fun areContentsTheSame(a: HireRequest, b: HireRequest) = a == b
        }
    }
}