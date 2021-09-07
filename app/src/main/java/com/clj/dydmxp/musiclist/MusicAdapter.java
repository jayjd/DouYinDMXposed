package com.clj.dydmxp.musiclist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clj.dydmxp.R;
import com.clj.dydmxp.message.MessageSuccessUserContent;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends BaseAdapter {

    List<MessageSuccessUserContent> list = new ArrayList<>();
    public LayoutInflater inflater;
    public Context context;

    public MusicAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        ViewHoder viewHoder;
        if (convertView == null) {
            viewHoder = new ViewHoder();
            convertView = inflater.inflate(R.layout.activity_list_content, null);

            viewHoder.userNameText = convertView.findViewById(R.id.nickName);
            viewHoder.countText = convertView.findViewById(R.id.content);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        System.out.println(list.get(position).toString());
        viewHoder.userNameText.setText("[点歌成功]" + list.get(position).getRealNickName());
        viewHoder.userNameText.setTextColor(Color.RED);
        viewHoder.userNameText.getPaint().setFakeBoldText(true);
        viewHoder.userNameText.setTextSize(15f);
        viewHoder.countText.setText(list.get(position).getContent());
        viewHoder.countText.setTextColor(Color.BLACK);
        return convertView;
    }


    public void add(MessageSuccessUserContent messageSuccessUserContent) {
        list.add(messageSuccessUserContent);
        notifyDataSetChanged();
    }

    public static class ViewHoder {
        public TextView userNameText;
        public TextView countText;
    }
}
