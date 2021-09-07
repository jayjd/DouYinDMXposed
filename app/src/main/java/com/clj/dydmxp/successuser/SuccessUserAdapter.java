package com.clj.dydmxp.successuser;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuccessUserAdapter extends BaseAdapter {
    List<MessageSuccessUserContent> list = new ArrayList<>();
    public LayoutInflater inflater;
    public Context context;

    public SuccessUserAdapter(Context context) {
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
        viewHoder.userNameText.setText(list.get(position).getRealNickName());
        viewHoder.userNameText.setTextColor(Color.RED);
        viewHoder.countText.setText(list.get(position).getCount() + "");
        viewHoder.countText.setTextColor(Color.BLACK);
        return convertView;
    }

    public void changedCountText(MessageSuccessUserContent messageSuccessUserContent) {
        boolean isCz = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRealNickName().equals(messageSuccessUserContent.getRealNickName())) {
                int i1 = list.get(i).getCount() + 1;
                list.get(i).setCount(i1);
                isCz = true;
                break;
            }
        }
        if (!isCz) {
            list.add(messageSuccessUserContent);
        }
        Collections.sort(list, new Comparator<MessageSuccessUserContent>() {
            @Override
            public int compare(MessageSuccessUserContent o1, MessageSuccessUserContent o2) {
                return o2.getCount() - (o1.getCount()); //倒序
            }
        });
        notifyDataSetChanged();
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
