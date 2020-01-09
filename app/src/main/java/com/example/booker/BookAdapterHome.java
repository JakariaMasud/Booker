package com.example.booker;

import android.util.Log;
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
    private List<Book>originalData = null;
    private List<Book>filteredData = null;
    private Filter mFilter = new BookFilter();
    private static BookClickListener bookClickListener;

    public BookAdapterHome(List<Book> booklist) {

        this.filteredData = booklist ;
        this.originalData = booklist ;

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
        final Book book=filteredData.get(position);
        Picasso.get().load(book.getCoverLink()).placeholder(R.drawable.book_place_holder).into(holder.singleBookBinding.bookIV);
        holder.singleBookBinding.bookTitleTV.setText(book.getTitle());
        holder.singleBookBinding.bookEditionTV.setText("Edition: "+book.getEdition());
        holder.singleBookBinding.bookAuthorTV.setText("Author "+book.getAuthor());


    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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
private class BookFilter extends Filter{
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        String filterString = constraint.toString().toLowerCase();

        FilterResults results = new FilterResults();

        final List<Book> list = originalData;

        int count = list.size();
        final ArrayList<Book> nlist = new ArrayList<Book>(count);

        Book filterableObj ;

        for (int i = 0; i < count; i++) {
            filterableObj = list.get(i);
            if (filterableObj.getTitle().toLowerCase().contains(filterString)
                    || filterableObj.getAuthor().toLowerCase().contains(filterString)
            || filterableObj.getOwnerId().toLowerCase().contains(filterString)) {
                nlist.add(filterableObj);
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredData = (ArrayList<Book>) results.values;
        notifyDataSetChanged();
    }

}
}
    


