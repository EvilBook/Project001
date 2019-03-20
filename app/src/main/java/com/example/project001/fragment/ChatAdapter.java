package com.example.project001.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.project001.R;
import com.example.project001.database.Chat;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    //variables
    String email;
    View view;

    List<Chat> chats = new ArrayList<Chat>();
    Context context;

    ChatViewHolder holder = new ChatViewHolder();

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void add(Chat chat) {
        this.chats.add(chat);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater tripInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Chat chets = chats.get(position);

        convertView = tripInflater.inflate(R.layout.activity_chat_header, null);
        holder.chatName = convertView.findViewById(R.id.chatName);
        convertView.setTag(holder);

        holder.chatName.setText(chets.getId());
        return convertView;
    }
}

class ChatViewHolder {
    TextView chatName;
}
