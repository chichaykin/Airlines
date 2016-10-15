package com.mich.airlines.view.detail;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mich.airlines.App;
import com.mich.airlines.R;
import com.mich.airlines.data.Airline;
import com.mich.airlines.di.components.DaggerUiComponent;
import com.mich.airlines.di.modules.UiModule;
import com.mich.airlines.presenters.DetailAirlinePresenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailAirlineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailAirlineFragment extends Fragment implements View.OnClickListener, DetailsView {
    private static final String ARG_AIRLINE = "airline";
    private static final int PERMISSIONS_REQUEST = 2;

    private Airline airline;

    @Bind(R.id.name)
    TextView nameTextView;

    @Bind(R.id.phone)
    TextView phoneTextView;

    @Bind(R.id.phone_header)
    View phoneHeaderView;

    @Bind(R.id.website)
    TextView websiteTextView;

    @Bind(R.id.website_header)
    View websiteHeader;

    @Inject
    DetailAirlinePresenter mPresenter;
    private PicassoAsyncLoader loader;
    private Drawable favorite;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param airline Airline.
     * @return A new instance of fragment DetailAirlineFragment.
     */
    public static DetailAirlineFragment newInstance(Airline airline) {
        DetailAirlineFragment fragment = new DetailAirlineFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_AIRLINE, airline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            airline = getArguments().getParcelable(ARG_AIRLINE);
        }

        DaggerUiComponent.builder()
                .appComponent(App.get(getContext()).getComponent())
                .uiModule(new UiModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_airline, container, false);
        ButterKnife.bind(this, rootView);

        nameTextView.setText(airline.getName());
        nameTextView.setOnClickListener(this);
        loader = new PicassoAsyncLoader();

        Picasso.with(getContext()).load(airline.getLogoURL()).into(loader);
        String phone = airline.getPhone();
        if (TextUtils.isEmpty(phone) ) {
            phoneTextView.setVisibility(View.GONE);
            phoneHeaderView.setVisibility(View.GONE);
        } else {
            phoneTextView.setText(airline.getPhone());
            phoneTextView.setOnClickListener(this);
        }

        String site = airline.getSite();
        if (TextUtils.isEmpty(site)) {
            websiteTextView.setVisibility(View.GONE);
            websiteHeader.setVisibility(View.GONE);
        } else {
            websiteTextView.setText(site);
            websiteTextView.setOnClickListener(this);
        }

        mPresenter.init();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (nameTextView.getId() == id) {
            mPresenter.onFavoriteClick(airline);
        } else if (phoneTextView.getId() == id) {
            mPresenter.onPhoneClick(airline);
        } else if (websiteTextView.getId() == id) {
            mPresenter.onWebsiteClick(airline);
        }
    }

    @Override
    public void requestPermission(String permission) {
        if (shouldShowRequestPermissionRationale(permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Toast.makeText(getContext(), R.string.request_permission_explanation, Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{permission}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void showMessage(int string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdateFavoriteImage(boolean isFavoriteAfterUpdate) {
        airline.setFavorite(isFavoriteAfterUpdate);
        updateImages();
    }

    void updateImages() {
        favorite = ContextCompat.getDrawable(getContext(),
                airline.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        favorite.setBounds(0, 0, 150, 150);
        nameTextView.setCompoundDrawables(loader.logo, null, favorite, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                mPresenter.onPermissionGranted(airline, permissions, grantResults);
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    final class PicassoAsyncLoader implements Target {
        Drawable logo;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            logo = new BitmapDrawable(getContext().getResources(), bitmap);
            logo.setBounds(0,0,150,150);
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
