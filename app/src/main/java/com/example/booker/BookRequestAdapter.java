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

public class BookRequestAdapter extends RecyclerView.Adapter<BookRequestViewHolder>{
    List<Request> requests;
    DatabaseReference databaseReference;
    Map requestAcceptMap=new HashMap();

    public BookRequestAdapter(List<Request> requests, String userId) {

        this.requests = requests;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users/"+userId+"/Book_requests/");
        requestAcceptMap.put("status",true);

    }

    @NonNull
    @Override
    public BookRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        BookRequestBinding bookRequestBinding = DataBindingUtil.inflate(layoutInflater,R.layout.book_request,parent,false);


        return new BookRequestViewHolder(bookRequestBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRequestViewHolder holder, int position) {
        final Request request=requests.get(position);
        holder.requestBinding.requestTitle.setText(request.getRequesterName()+" wants your book named "+request.getBookTitle());
        Picasso.get().load(request.getCoverLink()).placeholder(R.drawable.book_place_holder).into(holder.requestBinding.requestbookIV);
        holder.requestBinding.acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(request.getRequesterId()).updateChildren(requestAcceptMap);
                notifyDataSetChanged();

            }
        });
        holder.requestBinding.rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(request.getRequesterId()).removeValue();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
class BookRequestViewHolder extends RecyclerView.ViewHolder{
    BookRequestBinding requestBinding;

    public BookRequestViewHolder(@NonNull BookRequestBinding binding) {

        super(binding.getRoot());
        requestBinding=binding;
    }
}
