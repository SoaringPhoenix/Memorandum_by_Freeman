package com.example.memorandum;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by jason on 2018/3/19.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Data> mDataList;
    private List<Data> dataList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View dataView;
        TextView dataDate;
        TextView dataContent;

        public ViewHolder(View view) {
            super(view);
            dataView = view;
            dataDate = (TextView) view.findViewById(R.id.data_date);
            dataContent = (TextView) view.findViewById(R.id.data_content);
        }
    }
    public DataAdapter(List<Data> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        final Intent intent = new Intent();
        holder.dataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Data data = mDataList.get(position);
                String content = data.getContent();
//                Toast.makeText(v.getContext(), "You clicked view" + data.getContent(), Toast.LENGTH_SHORT).show();
                intent.setClass(parent.getContext(), MemorandumActivity.class);
                intent.putExtra("content", content);
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        holder.dataDate.setText(data.getDate());
        holder.dataContent.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
