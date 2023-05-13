package com.sophra.overlaynote_mobile;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;
    private ArrayList<String> colors;

    public CustomAdapter (ArrayList<String> dataSet, ArrayList<String> colordata)
    {
        localDataSet = dataSet;
        colors = colordata;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo, parent, false);
        CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        String text = localDataSet.get(position);
        holder.dialog_title.setText(text);

        String colorcode = colors.get(position);
        if(colorcode == "White")
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor(colorcode));
        }

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dialog_title;
        private TextView dialog_text;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dialog_title = itemView.findViewById(R.id.dialog_title);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("log_overlay", "클릭한 메모 : " + dialog_title.getText());
                    Intent intent = new Intent(itemView.getContext(), EditActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public TextView getTextView() {
            return dialog_title;
        }
    }
}
