package com.example.booker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.BookRequestBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReturnRequestAdapter extends RecyclerView.Adapter<ReturnRequestAdapter.ReturnRequestHolder> {
    List<Request>requestList;
    public static RequestClickListener requestClickListener;


    public ReturnRequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ReturnRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        BookRequestBinding binding= DataBindingUtil.inflate(layoutInflater,R.layout.book_request,parent,false);
        return new ReturnRequestHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnRequestHolder holder, int position) {
        Request request=requestList.get(position);
        holder.requestBinding.requestTitle.setText(request.getRequesterName()+" wants to return your Book "+request.getBookTitle());
        Picasso.get().load(request.getCoverLink()).placeholder(R.drawable.book_place_holder).into(holder.requestBinding.requestbookIV);

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ReturnRequestHolder extends RecyclerView.ViewHolder {
        BookRequestBinding requestBinding;
        public ReturnRequestHolder(@NonNull BookRequestBinding  binding) {
            super(binding.getRoot());
            requestBinding=binding;
            binding.acceptBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestClickListener.onRequestClick(getAdapterPosition(),v);

                }
            });
            binding.rejectBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestClickListener.onRequestClick(getAdapterPosition(),v);

                }
            });

        }
    }
    void setRequestClickListener(RequestClickListener listener){
        requestClickListener=listener;
    }
}
