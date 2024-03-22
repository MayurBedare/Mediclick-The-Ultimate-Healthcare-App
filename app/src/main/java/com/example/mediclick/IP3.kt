package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class IP3 : IP2(), MyAdapter.ItemClickListener {
    private lateinit var viewModel: LearnViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ip3)

        viewModel = ViewModelProvider(this).get(LearnViewModel::class.java)

        val rv = findViewById<RecyclerView>(R.id.recyclerView1)
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(viewModel.getData(), this)
        rv.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, VideosView::class.java).apply {
            putExtra("videos", viewModel.getData()[position])
        }
        startActivity(intent)
    }
}
