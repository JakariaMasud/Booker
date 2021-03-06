package com.example.booker;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.BookRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRequestAdapter extends RecyclerView.Adapter<BookRequestAdapter.BookRequestViewHolder> {
    List<Request> requests;
    public static RequestClickListener requestClickListener;

    public BookRequestAdapter(List<Request> requests) {

        this.requests = requests;

    }

    @NonNull
    @Override
    public BookRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        BookRequestBinding binding=DataBindingUtil.inflate(layoutInflater,R.layout.book_request,parent,false);
        return new BookRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRequestViewHolder holder, int position) {
        Request request=requests.get(position);
        holder.requestBinding.requestTitle.setText(request.getRequesterName()+" wants one of your book named "+request.getBookTitle());
        Picasso.get().load(request.getCoverLink()).placeholder(R.drawable.book_place_holder).into(holder.requestBinding.requestbookIV);


    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public  class BookRequestViewHolder extends RecyclerView.ViewHolder {
        BookRequestBinding requestBinding;
        public BookRequestViewHolder(@NonNull BookRequestBinding binding) {
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
    public void setRequestClickListener(RequestClickListener listener){
        requestClickListener=listener;
    }
}


