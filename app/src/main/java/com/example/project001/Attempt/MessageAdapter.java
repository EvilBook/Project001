package com.example.project001.Attempt;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.project001.R;
import com.example.project001.database.User;
import com.example.project001.message.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChats;
    private String imageURL;
    public TextView show_message;

    FirebaseUser fuser;


    public MessageAdapter(Context mContext, List<Chat> mChats, String imageURL) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false );
            return new MessageAdapter.ViewHolder(view);

        } else {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false );
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat = mChats.get(i);

        viewHolder.show_message.setText(chat.getMessage());

        if(imageURL.equals("default")) {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(mContext).load(imageURL).into(viewHolder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = (TextView) itemView.findViewById(R.id.show_message);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChats.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
