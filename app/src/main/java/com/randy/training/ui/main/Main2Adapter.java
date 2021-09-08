package com.randy.training.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.randy.training.R;
import com.randy.training.base.adapter.recyclerview.BaseViewHolder;
import com.randy.training.base.adapter.recyclerview.MyAdapter;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/718:06
 * desc   :
 */
public class Main2Adapter extends MyAdapter<String> {
    private Context context;

    public Main2Adapter(@NonNull Context context, int layout) {
        super(context, layout);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        Button button = holder.itemView.findViewById(R.id.acb_function_item);
        String functionName = (String) getItem(position);
        button.setText(functionName);
    }

}
