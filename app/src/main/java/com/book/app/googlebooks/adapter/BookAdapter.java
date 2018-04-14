package com.book.app.googlebooks.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.book.app.googlebooks.holder.BookViewHolder;
import com.google.api.services.books.model.Volume;

import java.util.List;
public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private final int spanDataCount;
    private List<Volume> volumes;

    public BookAdapter(List<Volume> volumes, int spanDataCount) {
        this.volumes = volumes;
        this.spanDataCount = spanDataCount;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.setSpanDataCount(spanDataCount);
        if(volumes!=null)
        holder.setVolume(volumes.get(position));
    }

    @Override
    public long getItemId(int position) {
        return volumes.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return volumes.size();
    }
}
