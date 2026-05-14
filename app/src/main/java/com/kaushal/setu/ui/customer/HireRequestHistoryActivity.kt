package com.kaushal.setu.ui.customer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.databinding.ActivityHireRequestHistoryBinding
import com.kaushal.setu.ui.common.BaseActivity
import com.kaushal.setu.ui.common.CustomerRequestAdapter
import com.kaushal.setu.utils.hide
import com.kaushal.setu.utils.show
import com.kaushal.setu.viewmodel.WorkerViewModel

class HireRequestHistoryActivity : BaseActivity() {
    private lateinit var b: ActivityHireRequestHistoryBinding
    private val vm: WorkerViewModel by viewModels()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private lateinit var adapter: CustomerRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHireRequestHistoryBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = CustomerRequestAdapter()
        b.rvRequests.layoutManager = LinearLayoutManager(this)
        b.rvRequests.adapter = adapter

        vm.customerRequests.observe(this) { list ->
            adapter.submitList(list)
            b.progressBar.hide()
            if (list.isEmpty()) {
                b.tvEmpty.show()
                b.rvRequests.hide()
            } else {
                b.tvEmpty.hide()
                b.rvRequests.show()
            }
        }

        b.btnBack.setOnClickListener { finish() }

        b.swipeRefresh.setOnRefreshListener {
            vm.loadCustomerRequests(uid)
            b.swipeRefresh.isRefreshing = false
        }

        b.progressBar.show()
        vm.loadCustomerRequests(uid)
    }
}
