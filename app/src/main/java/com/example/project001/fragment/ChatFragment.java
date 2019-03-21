package com.example.project001.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project001.R;
import com.example.project001.database.Chat;
import com.example.project001.database.Trip;
import com.example.project001.message.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

public class ChatFragment extends Fragment {

    //variables
    private ChatAdapter chatAdapter;
    private ListView chatView;
    String email;
    String name;
    Chat chat;
    String roomID;
    Button deleteChat;

    ArrayList<Chat> chats = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            name = getArguments().getString("name");
            //Log.e("chatFragment", email);
        } else {
            Log.e("doesn't", "work");
        }

        chatAdapter = new ChatAdapter(getContext());
        chatAdapter.email = email;
        chatAdapter.view = getView();
        chatView = getView().findViewById(R.id.chatView);
        chatView.setAdapter(chatAdapter);


        startChat();

        //reload content









        chatView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteChat();

                LinearLayout linearLayout = view.findViewById(R.id.chatClick);
                linearLayout.setBackgroundColor(Color.parseColor("#f93943"));

                view.setEnabled(false);
                view.setOnClickListener(null);

                return true;
            }
        });


        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                roomID = ((TextView)(((LinearLayout)(((LinearLayout)(((RelativeLayout)(((RelativeLayout)(view )).getChildAt(0))).getChildAt(0))).getChildAt(0))).getChildAt(0))).getText().toString();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("room", roomID);
                startActivity(intent);
            }
        });

    }


    public void startChat() {

        chats.clear();
        db.collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.e("bloody", email);
                                //Log.e("anything", document.getString("driverName"));

                                if(document.getId().contains(email)) {

                                    Log.e("inside if", email);
                                    chat = new Chat(document.get("driverName").toString(),
                                            document.get("id").toString(),
                                            document.get("passengerName").toString()
                                    );

                                    chats.add(chat);
                                    roomID = chat.getId();
                                    Log.e("bloody hell ", chat.getDriver());
                                }
                            }

                            for (Chat T : chats) {
                                chatAdapter.add(T);
                                // scroll the ListView to the last added element
                                chatView.setSelection(chatView.getCount() - 1);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void deleteChat() {

        db.collection("chat").document(chat.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //startChat();
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }


}
