package com.mrq.library.ui.dapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrq.library.base.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrq.library.databinding.CustomFilterBinding

@SuppressLint("NotifyDataSetChanged")
class DialogFilterAdapter(val context: Context) : BaseAdapter<String>() {

    private var selectedPosition: Int = -1

    fun setList(newList: ArrayList<String>) {
        data = newList
        notifyDataSetChanged()
    }

    fun getList(): List<String> {
        return data
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()
    }

    class SearchFilterViewHolder(val binding: CustomFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SearchFilterViewHolder(
            CustomFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, model: String, position: Int) {
        holder as SearchFilterViewHolder

        holder.binding.title.text = model
        if (selectedPosition == position) {
            holder.itemView.isSelected = true
            holder.binding.check.visibility = View.VISIBLE
        } else {
            holder.itemView.isSelected = false
            holder.binding.check.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }

    }

}

