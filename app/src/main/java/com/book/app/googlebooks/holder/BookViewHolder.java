package com.book.app.googlebooks.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.book.app.googlebooks.R;
import com.book.app.googlebooks.activity.InformationBookActivity;
import com.book.app.googlebooks.data.BookAPIData;
import com.google.api.services.books.model.Volume;

import java.util.List;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Volume volume;
    private int spanDataCount;

    public BookViewHolder(ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book, viewGroup, false));
        itemView.setOnClickListener(this);
    }

    public void setVolume(Volume volume) {
        this.volume = volume;

        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView author = (TextView) itemView.findViewById(R.id.author);
        TextView notAvailable = (TextView) itemView.findViewById(R.id.notAvailable);

         String mTitle = volume.getVolumeInfo().getTitle();
         List mAuthor = volume.getVolumeInfo().getAuthors();

        if(mTitle == null && mAuthor == null) {
            notAvailable.setVisibility(View.VISIBLE);
            notAvailable.setText(itemView.getContext().getResources().getString(R.string.not_available));
        }
        else {
            notAvailable.setVisibility(View.GONE);
            if(mTitle!=null)
            title.setText(mTitle);
            if(mAuthor!=null)
            author.setText("By : " + ((String) mAuthor.get(0)));
        }

        }

    public void setSpanDataCount(int spanDataCount) {
        this.spanDataCount = spanDataCount;
    }

    @Override
    public void onClick(View v) {

        Bundle metadata = new Bundle();
        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
        Volume.SaleInfo saleInfo = volume.getSaleInfo();

        if (volumeInfo != null) {
            if (volumeInfo.getTitle() != null) {
                metadata.putString(BookAPIData.TITLE, volumeInfo.getTitle());
            }
            if (volumeInfo.getSubtitle() != null) {
                metadata.putString(BookAPIData.SUBTITLE, volumeInfo.getSubtitle());
            }
            if (volumeInfo.getDescription() != null) {
                metadata.putString(BookAPIData.DESCRIPTION, volumeInfo.getDescription());
            }
            if (volumeInfo.getPublisher() != null) {
                metadata.putString(BookAPIData.PUBLISHER, volumeInfo.getPublisher());
            }
            if (volumeInfo.getAuthors() != null) {
                metadata.putStringArray(BookAPIData.AUTHORS, volumeInfo.getAuthors().toArray(new String[volumeInfo.getAuthors().size()]));
            }
            if (volumeInfo.getPublishedDate() != null) {
                metadata.putString(BookAPIData.PUBLISHED_DATE, volumeInfo.getPublishedDate());
            }
            if (saleInfo != null) {
                Volume.SaleInfo.RetailPrice retailPrice = saleInfo.getRetailPrice();
                Volume.SaleInfo.ListPrice listPrice = saleInfo.getListPrice();
                if (retailPrice != null) {
                    metadata.putDouble(BookAPIData.RETAIL_PRICE, retailPrice.getAmount());
                    metadata.putString(BookAPIData.RETAIL_PRICE_CURRENCY_CODE, retailPrice.getCurrencyCode());
                }
                if (listPrice != null) {
                    metadata.putDouble(BookAPIData.LIST_PRICE, listPrice.getAmount());
                    metadata.putString(BookAPIData.LIST_PRICE_CURRENCY_CODE, listPrice.getCurrencyCode());
                }
            }
        }

        Context context = itemView.getContext();
        context.startActivity(new Intent(context, InformationBookActivity.class).putExtra("metadata", metadata));
    }
}
