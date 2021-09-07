package com.randy.training.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/717:56
 * desc   :
 */
public abstract class KBaseAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    private int mItemLayout;
    private LayoutInflater mLayoutInflater;

    public KBaseAdapter(Context context, int itemLayout) {
        this.mContext = context;
        this.mItemLayout = itemLayout;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(mItemLayout, null);
            }
            itemBehavior(position, convertView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public abstract void itemBehavior(int position, View convertView);

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        if (list != null && list.size() > 0) {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void update(List<T> list) {
        if (list == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<>();
        }
        this.mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder {

        @SuppressWarnings("unchecked")
        public static <V extends View> V get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (V) childView;
        }
    }
}

