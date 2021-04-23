package com.example.timetablemanager;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<ListItem> mListItem;
    private String mParam1, mParam2;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onSetAlarmClick(int position, ImageView mAlarmBtn);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public TextView mTimeView;
        public ImageView mDeleteImg;
        public ImageView mEditBtn;
        public ImageView mAlarmBtn;

        public ListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.ListName);
            mTimeView = itemView.findViewById(R.id.ListTime);
            mDeleteImg = itemView.findViewById(R.id.deleteBtn);
            mEditBtn = itemView.findViewById(R.id.editBtn);
            mAlarmBtn = itemView.findViewById(R.id.alarm);

            mAlarmBtn.setTag(R.drawable.notif_off);

            mDeleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            mEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mAlarmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSetAlarmClick(position, mAlarmBtn);
                        }
                    }
                }
            });
        }
    }

    public ListAdapter(ArrayList<ListItem> listItem, String param1, String param2) {
        mListItem = listItem;
        mParam1 = param1;
        mParam2 = param2;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        ListViewHolder lvh = new ListViewHolder(v, mListener);

        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        int myIntValue = sharedPreferences.getInt("icon " + lvh.getAdapterPosition() + mParam1 + mParam2, -1);
        if(myIntValue != -1) {
            lvh.mAlarmBtn.setImageResource(myIntValue);
        } else {
            lvh.mAlarmBtn.setImageResource(R.drawable.notif_off);
        }

        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem currentList = mListItem.get(position);

        holder.mNameView.setText(currentList.getName());
        holder.mTimeView.setText(currentList.getTime());

        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        int myIntValue = sharedPreferences.getInt("icon " + position + mParam1 + mParam2, -1);
        if(myIntValue != -1) {
            holder.mAlarmBtn.setImageResource(myIntValue);
        } else {
            holder.mAlarmBtn.setImageResource(R.drawable.notif_off);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }
}
