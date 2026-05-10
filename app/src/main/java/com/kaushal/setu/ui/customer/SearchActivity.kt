package com.kaushal.setu.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaushal.setu.R
import com.kaushal.setu.databinding.ActivitySearchBinding
import com.kaushal.setu.ui.common.WorkerCardAdapter
import com.kaushal.setu.utils.hide
import com.kaushal.setu.utils.show
import com.kaushal.setu.viewmodel.WorkerViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var b: ActivitySearchBinding
    private val vm: WorkerViewModel by viewModels()
    private lateinit var adapter: WorkerCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = WorkerCardAdapter { worker ->
            startActivity(Intent(this, WorkerDetailActivity::class.java).putExtra("UID", worker.uid))
        }
        b.rvResults.layoutManager = LinearLayoutManager(this)
        b.rvResults.adapter = adapter

        val cats = listOf(getString(R.string.cat_all)) + resources.getStringArray(R.array.skill_categories).toList()
        b.spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cats)

        vm.workers.observe(this) { list ->
            adapter.submitList(list)
            b.tvCount.text = getString(R.string.workers_found, list.size)
            if (list.isEmpty()) b.tvEmpty.show() else b.tvEmpty.hide()
        }

        vm.ui.observe(this) { state ->
            b.progressBar.visibility = if (state is WorkerViewModel.UiState.Loading) View.VISIBLE else View.GONE
        }

        b.btnBack.setOnClickListener { finish() }
        b.btnSearch.setOnClickListener {
            val cat = if (b.spinnerCategory.selectedItemPosition == 0) ""
                      else b.spinnerCategory.selectedItem.toString()
            val loc = b.etLocation.text.toString().trim()
            vm.searchWorkers(cat, loc)
        }
    }
}