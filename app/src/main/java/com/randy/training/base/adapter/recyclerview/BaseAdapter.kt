package com.randy.training.base.adapter.recyclerview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *    author : yinzhiyu
 *    e-mail : yinzhiyu@zongheng.com
 *    date   : 2021/9/810:25
 *    desc   : recycleView统配adapter
 */
abstract class BaseAdapter<T>(var data: List<T> = listOf()): RecyclerView.Adapter<BaseViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }
    open fun getItem(position: Int): Any? {
        return data?.get(position)
    }

    fun refreshData(newData: List<T>) {
        this.data = newData
        this.notifyDataSetChanged()
    }
    //  增加点击和长按事件
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
//        fun onItemLongClick(view: View,position: Int): Boolean
    }

    operator fun <V : View?> get(view: View, id: Int): V? {
        var viewHolder = view.tag as SparseArray<View?>
        if (viewHolder == null) {
            viewHolder = SparseArray()
            view.tag = viewHolder
        }
        var childView = viewHolder[id]
        if (childView == null) {
            childView = view.findViewById(id)
            viewHolder.put(id, childView)
        }
        return childView as V?
    }
}