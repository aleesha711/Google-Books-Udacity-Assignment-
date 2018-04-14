package com.book.app.googlebooks.holder;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.book.app.googlebooks.BuildConfig;
import com.book.app.googlebooks.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.common.primitives.Ints;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AsyncTaskSearch extends AsyncTask<String, Void, List<Volume>> {

    private BookSearchListener booksearchListenerBook;
    private Context mContext;
    AsyncTaskSearch asyncTaskSearch;

    public AsyncTaskSearch(Context context) {
        mContext = context;
    }

    public void setBooksearchListenerBook(BookSearchListener booksearchListenerBook) {
        this.booksearchListenerBook = booksearchListenerBook;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncTaskSearch = this;
        booksearchListenerBook.onSearching();

    }

    @Override
    protected List<Volume> doInBackground(String... params) {

        if (NetworkCheck.isInternetAvailable(mContext)) {
            String query = params[0];
            if (Ints.tryParse(query) != null && (query.length() == 13 || query.length() == 10)) {
                query = query.concat("+isbn:" + query);
            }

            Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .build();

            try {
                Books.Volumes.List list = books.volumes().list(query).setProjection("LITE");
                return list.execute().getItems();
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected void onPostExecute(List<Volume> volumes) {
        super.onPostExecute(volumes);
        if(volumes!=null) {
            booksearchListenerBook.onResult(volumes == null ? Collections.<Volume>emptyList() : volumes);
            if (!NetworkCheck.isInternetAvailable(mContext)) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            } else if (volumes.contains("IOException: java.net.ConnectException:")) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.connection_exc), Toast.LENGTH_LONG).show();

            } else if (volumes.contains("IOException: java.net.SocketTimeoutException:")) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.socket), Toast.LENGTH_LONG).show();

            } else if (volumes.contains("IOException: java.net.SocketException:")) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.socket_exc), Toast.LENGTH_LONG).show();

            }
        }
    }

    public interface BookSearchListener {
        void onSearching();
        void onResult(List<Volume> volumes);
    }

}
