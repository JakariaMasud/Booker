package com.example.booker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.HistoryItemBinding;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    List<History>historyList;

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        HistoryItemBinding binding= DataBindingUtil.inflate(inflater,R.layout.history_item,parent,false);

        return new HistoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.historyItemBinding.historyTV.setText(historyList.get(position).getHistoryMsg());

    }



    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        HistoryItemBinding historyItemBinding;

        public HistoryViewHolder(@NonNull HistoryItemBinding binding) {
            super(binding.getRoot());
            historyItemBinding=binding;
        }
    }
}
