package com.randy.training.base.adapter.recyclerview

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 *    author : yinzhiyu
 *    e-mail : yinzhiyu@zongheng.com
 *    date   : 2021/9/810:26
 *    desc   : recycleView通用adapter
 */
abstract class MyAdapter<T> constructor(
    var context: Context, //代替下方init方法 constructor(layout:Int)为主构造函数
    var layout: Int
) : BaseAdapter<T>() {
    //    init {
//        主构造函数执行后执行
//        this.layout = layout
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }


    private lateinit var onItemClickListener: OnItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return this.layout
//        return R.layout.server_owner_dash_board_item
    }
}