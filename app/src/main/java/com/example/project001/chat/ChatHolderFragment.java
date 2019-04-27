package com.example.project001.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.project001.R;

public class ChatHolderFragment extends Fragment {


    //Tabs
    LinearLayout linearLayout;
    TabHost frameLayout;
    LinearLayout triliniarLayout;

    //Add trips
    String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_holder, container, false);
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        //Tabs
        frameLayout = getView().findViewById(R.id.tabHost);
        TabHost host = getView().findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.chats);
        spec.setIndicator("Chat");
        host.addTab(spec);

        //set text color tab 1
        TextView tv = host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(getResources().getColor(R.color.white));

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.users);
        spec.setIndicator("User");
        host.addTab(spec);

        //set text color tab 2
        TextView tv2 = host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextColor(getResources().getColor(R.color.white));

        linearLayout = getView().findViewById(R.id.chats);
        triliniarLayout = getView().findViewById(R.id.users);

        chatTab();
        userTab();
    }


    public void chatTab() {

        Bundle bun = new Bundle();
        bun.putString("email", email);

        ChatteFragment chatte = new ChatteFragment();
        chatte.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(linearLayout.getId(), chatte)
                .commit();
    }


    public void userTab() {

        Bundle bun = new Bundle();
        bun.putString("email", email);

        UserFragment user = new UserFragment();
        user.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(triliniarLayout.getId(), user)
                .commit();
    }



}
