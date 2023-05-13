package com.sophra.overlaynote_mobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        String rootSD = Environment.getExternalStorageDirectory().toString();
        File file = new File(rootSD + "/Download/OverlayNote/" + holder.dialog_title.getText() + ".rtf");


        InputStream is = null;
        try {
            is = new FileInputStream(file.getPath());
            IRtfSource source = new RtfStreamSource(is);
            IRtfParser parser = new StandardRtfParser();
            MyRtfListener listener = new MyRtfListener();
            parser.parse(source, listener);

            //Log.d("log_overlay", "각자 불러온 텍스트 : " + listener.getText());
            String listenText = listener.getText();

            int idx = listenText.indexOf("/");
            String content = listenText.substring(idx+1, idx + 40);
            Log.d("log_overlay", "content : " + content);
            holder.dialog_text.setText(content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            dialog_text = itemView.findViewById(R.id.dialog_text);
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
