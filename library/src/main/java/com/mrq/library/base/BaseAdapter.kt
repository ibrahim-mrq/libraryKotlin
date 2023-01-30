package com.mrq.library.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrq.library.helpers.Utils

//https://stackoverflow.com/a/38350061/10044208

@SuppressLint("NotifyDataSetChanged")
abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Utils {

    var data = ArrayList<T>()

    abstract fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindData(holder: RecyclerView.ViewHolder, model: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(holder, data[position], position)
    }

    override fun getItemCount() = data.size

}