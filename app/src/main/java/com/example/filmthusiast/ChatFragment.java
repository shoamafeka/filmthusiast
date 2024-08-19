package com.example.filmthusiast;

import android.graphics.Rect;
import android.os.Bundle;

import com.example.filmthusiast.adapters.ChatItemAdapter;
import com.example.filmthusiast.models.Chat;
import com.example.filmthusiast.utilities.DataManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatFragment extends Fragment {


    private ChatItemAdapter chatItemAdapter;
    private RecyclerView recyclerChat;
    private EditText inputText;
    private ImageButton SendBtn;
    private LinearLayout inputLayout;
    private ProgressBar progressbar;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerChat = (RecyclerView) view.findViewById(R.id.chatRecyclerView);
        inputText = (EditText) view.findViewById(R.id.messageInput);
        SendBtn = (ImageButton) view.findViewById(R.id.sendButton);
        inputLayout =(LinearLayout) view.findViewById(R.id.inputLayout);
        progressbar =(ProgressBar) view.findViewById(R.id.progressBar);
        progressbar.setVisibility(View.VISIBLE);

        recyclerChat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Detect if the keyboard is shown
                Rect r = new Rect();
                recyclerChat.getWindowVisibleDisplayFrame(r);
                int screenHeight = recyclerChat.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is to detect a keyboard, may vary per device
                    // Keyboard is shown, scroll to top
                    try {
                        recyclerChat.smoothScrollToPosition(chatItemAdapter.getItemCount() - 1);
                    }catch (Exception e){

                    }
                }
            }
        });


        loadChat();
        return view;

    }

    private void loadChat() {
        new DataManager(dataManager->{
            chatItemAdapter = new ChatItemAdapter(getContext(), dataManager.getChats(), chat -> {
                dataManager.deleteChat(chat, getContext());
                dataManager.saveChat();
                chatItemAdapter.notifyDataSetChanged();
            });
            recyclerChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerChat.setAdapter(chatItemAdapter);
            try {
                recyclerChat.smoothScrollToPosition(chatItemAdapter.getItemCount() - 1);
            }catch (Exception e){

            }
            progressbar.setVisibility(View.GONE);
            SendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chat chat = new Chat();
                    chat.setText(String.valueOf(inputText.getText()));
                    chat.setUser_id(user.getUid());
                    String name = user.getUid() ;
                    if(user.getDisplayName() != null ){
                        if(!user.getDisplayName().isEmpty()) {
                           name = user.getDisplayName();
                        }
                    }
                    chat.setUsername(name);
                    chat.setNow();

                    dataManager.addChat(chat);
                    dataManager.saveChat();

                    inputText.setText("");
                    chatItemAdapter.notifyDataSetChanged();
                    try {
                        recyclerChat.smoothScrollToPosition(chatItemAdapter.getItemCount() - 1);
                    }catch (Exception e){

                    }

                }
            });
        });

    }
}