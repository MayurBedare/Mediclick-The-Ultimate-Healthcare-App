package com.example.mediclick.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediclick.R

class WeightPickerAdapter(private val context: Context, var dataList: List<String>, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<WeightPickerAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.weight_picker_item_layout, parent, false)
        return TextViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun swapData(newData: List<String>) {
        dataList = newData
        notifyDataSetChanged()
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pickerTxt: TextView = itemView.findViewById(R.id.weight_picker_item)

        init {
            pickerTxt.setOnClickListener {
                recyclerView.smoothScrollToPosition(adapterPosition)
            }
        }

        fun bind(weight: String) {
            pickerTxt.text = weight
        }
    }
}
