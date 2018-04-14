package com.book.app.googlebooks.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.book.app.googlebooks.holder.AsyncTaskSearch;
import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

public class BookSearchFragment extends Fragment implements AsyncTaskSearch.BookSearchListener {

    private boolean searching;
    Context mContext;
    private AsyncTaskSearch asyncTaskSearch;
    private AsyncTaskSearch.BookSearchListener bookSearchListener;

    private String latestQuery;
    private List<Volume> volumeList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bookSearchListener = (AsyncTaskSearch.BookSearchListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void searchBooks(String query) {
        if (query.equalsIgnoreCase(latestQuery)) {
            return;
        }
        if (asyncTaskSearch != null) {
            asyncTaskSearch.cancel(true);
        }
        latestQuery = query;
        asyncTaskSearch = new AsyncTaskSearch(getActivity());
        asyncTaskSearch.setBooksearchListenerBook(this);

      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {


            @Override
            public void run() {
                if ( asyncTaskSearch.getStatus() == AsyncTask.Status.RUNNING || asyncTaskSearch.getStatus() == AsyncTask.Status.PENDING)
                    asyncTaskSearch.cancel(true);


            }
        }, 600000 );*/

        asyncTaskSearch.execute(query);



    }

    @Override
    public void onSearching() {
        searching = true;
        volumeList.clear();
        bookSearchListener.onSearching();
    }


    @Override
    public void onResult(List<Volume> volumes) {
        searching = false;
        volumeList = volumes;
        bookSearchListener.onResult(volumeList);
    }

    public String getLatestQuery() {
        return latestQuery;
    }

    public List<Volume> getVolumeList() {
        return volumeList;
    }

}
