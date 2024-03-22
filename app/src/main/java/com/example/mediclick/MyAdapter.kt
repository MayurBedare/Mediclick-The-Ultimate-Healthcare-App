package com.example.mediclick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    var videosList: ArrayList<Videos>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MyAdapter.ListViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_videos_list, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentVideo = videosList[position]
        with(holder.itemView) {
            val imageRecyclerView = findViewById<ImageView>(R.id.imageRecyclerView)
            val textRecyclerView1 = findViewById<TextView>(R.id.textRecyclerView1)
            val textRecyclerView3 = findViewById<TextView>(R.id.textRecyclerView3)

            imageRecyclerView.setImageResource(currentVideo.image)
            textRecyclerView1.text = currentVideo.title
            textRecyclerView3.text = "Duration: ${currentVideo.duration}"
        }
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}
