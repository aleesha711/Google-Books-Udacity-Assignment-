package com.book.app.googlebooks.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.book.app.googlebooks.R;
import com.book.app.googlebooks.data.BookAPIData;
import com.book.app.googlebooks.databinding.InformationBookActivityBinding;


public class InformationBookActivity extends AppCompatActivity {

    Bundle metadata;
    InformationBookActivityBinding bookInfoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bookInfoBinding = DataBindingUtil.setContentView(this, R.layout.information_book_activity);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        metadata = getIntent().getBundleExtra("metadata");
        updateViews();
    }

    private void updateViews() {

        String anonyomous = getString(R.string.anonyomous);
        bookInfoBinding.contentBookInfo.publishedDate.append(metadata.containsKey(BookAPIData.PUBLISHED_DATE) ? metadata.getString(BookAPIData.PUBLISHED_DATE) : anonyomous);
        bookInfoBinding.contentBookInfo.publisher.append(metadata.containsKey(BookAPIData.PUBLISHER) ? metadata.getString(BookAPIData.PUBLISHER) : anonyomous);
        if (metadata.containsKey(BookAPIData.AUTHORS)) {
            String author = "\n";
            String[] authors = metadata.getStringArray(BookAPIData.AUTHORS);
            for (int i = 0; i < authors.length; i++) {
                String singleAuthor = authors[i];
                if (TextUtils.isEmpty(singleAuthor)) {
                    continue;
                }
                author += singleAuthor.concat(i == authors.length - 1 ? "" : "\n");
                bookInfoBinding.contentBookInfo.author.append(author);
            }
        }
        if (metadata.containsKey(BookAPIData.SUBTITLE)) {
            bookInfoBinding.contentBookInfo.subtitle.setText(getString(R.string.title) + metadata.getString(BookAPIData.SUBTITLE));
        } else {
           bookInfoBinding.contentBookInfo.subtitle.setVisibility(View.GONE);
        }
        if (metadata.containsKey(BookAPIData.DESCRIPTION)) {
            bookInfoBinding.contentBookInfo.description.setText(metadata.getString(BookAPIData.DESCRIPTION));
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), BookSearchActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
