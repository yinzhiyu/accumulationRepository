package com.randy.training.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.ViewGroupUtils;

import com.randy.training.R;
import com.randy.training.base.adapter.BaseListviewAdapter;
import com.randy.training.base.adapter.recyclerview.BaseViewHolder;
import com.randy.training.base.adapter.recyclerview.MyAdapter;
import com.randy.training.widget.MultiTouchDelegate;

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

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        Button button = holder.itemView.findViewById(R.id.acb_function_item);
        LinearLayout ll = holder.itemView.findViewById( R.id.ll);

        ll.post(new Runnable() {
            @Override
            public void run() {
                Rect rect1 = new Rect();
                ViewGroupUtils.getDescendantRect(ll, button, rect1);
                rect1.inset(-300, -300);
                MultiTouchDelegate multiTouchDelegate = new MultiTouchDelegate(rect1,ll);
                multiTouchDelegate.addDelegateView(button, rect1);
            }
        });



        String functionName = (String) getItem(position);
        button.setText(functionName);
    }

}
