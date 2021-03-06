package com.example.booker;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.SingleBookBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookLendAdapter extends RecyclerView.Adapter<BookLendAdapter.BookHolder> {
    List<Book> bookList;
    private static BookClickListener bookClickListener;

    public BookLendAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        SingleBookBinding bookBinding= DataBindingUtil.inflate(layoutInflater,R.layout.single_book,parent,false);
        return new BookHolder(bookBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, final int position) {
        final Book book=bookList.get(position);
        Picasso.get().load(book.getCoverLink()).placeholder(R.drawable.book_place_holder).into(holder.singleBookBinding.bookIV);
        holder.singleBookBinding.bookTitleTV.setText(book.getTitle());
        holder.singleBookBinding.bookEditionTV.setText("Edition: "+book.getEdition());
        holder.singleBookBinding.bookAuthorTV.setText("Author "+book.getAuthor());


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private SingleBookBinding singleBookBinding;

        public BookHolder(@NonNull final SingleBookBinding bookBinding) {
            super(bookBinding.getRoot());
            this.singleBookBinding=bookBinding;
             bookBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     bookClickListener.onBookClick(getAdapterPosition(),v);
                 }
             });
             bookBinding.getRoot().setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),310,0,"Chat With Borrower");


        }
    }

    public void setOnBookClickListener(BookClickListener listener){
        bookClickListener =listener;
    }

}
