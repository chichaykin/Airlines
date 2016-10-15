package com.mich.airlines.view.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.mich.airlines.R;
import com.mich.airlines.data.Airline;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

@SuppressWarnings("unused")
@LayoutId(R.layout.holder_airline_item)
public class AirlineViewHolder extends ItemViewHolder<Airline> {

    @ViewId(R.id.name)
    TextView textView;
    private Drawable favorite;
    private PicassoAsyncLoader loader;
    private Airline airline;

    public AirlineViewHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Airline item, PositionInfo positionInfo) {
        airline = item;
        final AirlineHolderCallback callback = getListener(AirlineHolderCallback.class);

        textView.setText(item.getName());

        Context context = getContext();

        loader = new PicassoAsyncLoader();
        Picasso.with(context)
                .load(item.getLogoURL())
                .tag(context)
                .into(loader);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onClicked(airline);
                }
            }
        });

    }

    private void updateImages() {
        favorite = airline.isFavorite() ?
                ContextCompat.getDrawable(getContext(), android.R.drawable.btn_star_big_on)
                : null;
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(loader.logo, null, favorite, null);
    }

    public interface AirlineHolderCallback {

        void onClicked(Airline airline);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    final class PicassoAsyncLoader implements Target {
        Drawable logo;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            logo = new BitmapDrawable(getContext().getResources(), bitmap);
            updateImages();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            updateImages();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }


}
