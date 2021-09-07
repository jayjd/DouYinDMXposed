package com.clj.dydmxp.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clj.dydmxp.R;

import java.util.ArrayList;
import java.util.List;

public class MessageContentAdapter extends BaseAdapter {

    public List<MessageSuccessUserContent> list = new ArrayList<>();
    public LayoutInflater inflater;
    public Context context;

    public MessageContentAdapter() {
    }

    public MessageContentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(MessageSuccessUserContent messageSuccessUserContent) {
        list.add(messageSuccessUserContent);
//        Collections.reverse(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_list_content, null);
            viewHolder.nickNameTv = convertView.findViewById(R.id.nickName);
            viewHolder.nickNameTv.setTextColor(Color.RED);
            viewHolder.nickNameTv.getPaint().setFakeBoldText(true);
            viewHolder.contentTv = convertView.findViewById(R.id.content);
            viewHolder.contentTv.setTextColor(Color.BLACK);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nickNameTv.setText(list.get(position).getRealNickName() + ":");
        viewHolder.contentTv.setText(list.get(position).getContent());
        return convertView;
    }

    public static class ViewHolder {
        TextView nickNameTv;
        TextView contentTv;
    }
}
