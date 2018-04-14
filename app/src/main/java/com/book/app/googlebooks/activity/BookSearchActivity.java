package com.book.app.googlebooks.activity;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.book.app.googlebooks.R;
import com.book.app.googlebooks.adapter.BookAdapter;
import com.book.app.googlebooks.databinding.SearchActivityBinding;
import com.book.app.googlebooks.fragment.BookSearchFragment;
import com.book.app.googlebooks.holder.AsyncTaskSearch;
import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;


public class BookSearchActivity extends AppCompatActivity implements AsyncTaskSearch.BookSearchListener {

    private List<Volume> volumeDataList;
    private SearchActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_activity);

        BookSearchFragment searchFragment = (BookSearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment");
        if (searchFragment != null) {
            volumeDataList = searchFragment.getVolumeList();
            binding.searchView.setQuery(searchFragment.getLatestQuery(), false);
        } else {
            volumeDataList = new ArrayList<>();
            searchFragment = new BookSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(searchFragment, "searchFragment")
                    .commit();
        }

        RecyclerView recyclerView = binding.booksGrid;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 1);
        BookAdapter adapter = new BookAdapter(volumeDataList, gridLayoutManager.getSpanCount());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BookSearchFragment searchFragment = (BookSearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment");
                if (searchFragment != null) {
                    searchFragment.searchBooks(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onSearching() {
        volumeDataList.clear();
        binding.booksGrid.getAdapter().notifyDataSetChanged();
        binding.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResult(List<Volume> volumes) {
        binding.loadingView.setVisibility(View.GONE);
        volumeDataList.addAll(volumes);
        binding.booksGrid.getAdapter().notifyDataSetChanged();
    }

}
