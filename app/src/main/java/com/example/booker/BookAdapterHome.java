package com.example.booker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booker.databinding.SingleBookBinding;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class BookAdapterHome extends RecyclerView.Adapter<BookAdapterHome.BookHolder>  implements Filterable {
    List<Book> bookList;
    List<Book> bookListFull;
    private static BookClickListener bookClickListener;

    public BookAdapterHome(List<Book> bookList) {

        this.bookList = bookList;
        bookListFull=new ArrayList<>(bookList);
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

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    public class BookHolder extends RecyclerView.ViewHolder{
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

        }
    }

    public void setOnBookClickListener(BookClickListener listener){
        bookClickListener =listener;
    }
    Filter bookFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredBook=new ArrayList<>();
            if(constraint==null ||constraint.length()==0){
                filteredBook.addAll(bookListFull);
            }
            else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(Book item:bookListFull){
                  if(item.getTitle().toLowerCase().contentEquals(filterPattern)){
                      filteredBook.add(item);
                  }
                }
            }

            FilterResults results=new FilterResults();
            results.values=filteredBook;


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
            bookList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
    

}
