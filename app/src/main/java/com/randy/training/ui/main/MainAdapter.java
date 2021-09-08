package com.randy.training.ui.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.randy.training.R;
import com.randy.training.base.adapter.BaseListviewAdapter;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/718:06
 * desc   :
 */
public class MainAdapter extends BaseListviewAdapter<String> {
    public MainAdapter(Context context, int itemLayout) {
        super(context, itemLayout);
    }

    @Override
    public void itemBehavior(int position, View convertView) {
        TextView bookCategory = ViewListviewHolder.get(convertView, R.id.acb_function_item);
        String functionName = (String) getItem(position);
        bookCategory.setText(functionName);
    }
}
