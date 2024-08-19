package com.example.filmthusiast.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmthusiast.R;
import com.example.filmthusiast.models.Chat;
import com.example.filmthusiast.utilities.ChatCallback;
import com.example.filmthusiast.utilities.MovieCallback;

import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.MovieViewHolder> {

    private List<Chat> chats;
    private Context context;
    private ChatCallback chatCallback;

    public ChatItemAdapter(Context context, List<Chat> chats, ChatCallback chatCallback) {
        this.context = context;
        this.chats = chats;
        this.chatCallback = chatCallback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.user.setText(chat.getUsername());
        holder.timestamp.setText(chat.getNow());
        holder.messageText.setText(chat.getText());

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Create a new AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this item?");

                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the delete callback
                        chatCallback.onDeleteButtonClicked(chat);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if the user cancels
                        dialog.dismiss();
                    }
                });

                // Show the dialog
                builder.show();

                return true; // Return true to indicate the long click event is handled
            }
        });


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    public void updateMovies(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView user;
        private TextView messageText;
        private TextView timestamp;
        private ConstraintLayout container;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            messageText = itemView.findViewById(R.id.messageText);
            timestamp = itemView.findViewById(R.id.timestamp);
            container = itemView.findViewById(R.id.container);
        }
    }


}
