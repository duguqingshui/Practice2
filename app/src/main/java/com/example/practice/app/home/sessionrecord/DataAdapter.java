package com.example.practice.app.home.sessionrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.practice.R;
import com.example.practice.doman.Messages;
import com.example.practice.view.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends BaseAdapter
{

    private List<Messages> mDatas=new ArrayList<Messages>();
    private LayoutInflater mInflater;
    private SwipeListView mSwipeListView ;

    public DataAdapter(Context context, List<Messages> datas , SwipeListView swipeListView)
    {
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        mSwipeListView = swipeListView;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView = mInflater.inflate(R.layout.swipelistview_item, null);
        TextView content = (TextView) convertView.findViewById(R.id.id_text);
        TextView receivername = (TextView) convertView.findViewById(R.id.receivername);

        Button del = (Button) convertView.findViewById(R.id.id_remove);
        content.setText(mDatas.get(position).getContent());
        receivername.setText("safd");
        del.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDatas.remove(position);
                notifyDataSetChanged();
                /**
                 * 关闭SwipeListView
                 * 不关闭的话，刚删除位置的item存在问题
                 * 在监听事件中onListChange中关闭，会出现问题
                 */
                mSwipeListView.closeOpenedItems();
            }
        });

        return convertView;
    }

}