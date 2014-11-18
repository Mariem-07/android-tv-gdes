/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.leanback.fastlane;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;

import com.android.example.leanback.R;
import com.android.example.leanback.data.AvatarService;
import com.android.example.leanback.data.Gde;
import com.android.example.leanback.data.GdeObjectAdapter;
import com.android.example.leanback.data.HubApiResponse;
import com.android.example.leanback.data.HubApiService;
import com.android.example.leanback.data.Video;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class LeanbackBrowseFragment extends BrowseFragment implements Callback<HubApiResponse<Gde>> {

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundHelper bgHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://hub.gdgx.io/api/v1/")
                .setConverter(new GsonConverter(gson))
                .build();

        HubApiService hubApiService = restAdapter.create(HubApiService.class);
        hubApiService.getAllGdes(this);
    }

    public void init(Multimap<String, Gde> gdeProducts) {

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        setBrandColor(getResources().getColor(R.color.primary));
        setBadgeDrawable(getResources().getDrawable(R.drawable.filmi));

        int position = 0;
        Set<Map.Entry<String, Collection<Gde>>> entries = gdeProducts.asMap().entrySet();
        for (Map.Entry<String, Collection<Gde>> entry : entries) {

            HeaderItem headerItem = new HeaderItem(position++, entry.getKey(), null);
            mRowsAdapter.add(new ListRow(headerItem, new GdeObjectAdapter(new CardPresenter(), entry.getValue())));
        }

        setOnItemViewClickedListener(getDefaultItemViewClickedListener());
        setOnItemViewSelectedListener(getDefaultItemSelectedListener());

        bgHelper = new BackgroundHelper(getActivity());
        bgHelper.prepareBackgroundManager();

    }

    protected OnItemViewClickedListener getDefaultItemViewClickedListener() {
        return new OnItemViewClickedListener() {

            @Override
            public void onItemClicked(Presenter.ViewHolder viewHolder, Object o, RowPresenter.ViewHolder viewHolder2, Row row) {

                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                intent.putExtra(Video.INTENT_EXTRA_VIDEO, (Serializable) o);
                startActivity(intent);

            }
        };
    }

    protected OnItemViewSelectedListener getDefaultItemSelectedListener() {

        return new OnItemViewSelectedListener() {
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                       RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof Video) {
                    bgHelper.setBackgroundUrl(((Video) item).getThumbUrl());
                    bgHelper.startBackgroundTimer();
                }

            }
        };
    }

    private Multimap<String, Gde> convertToVeryHelpfulMap(HubApiResponse<Gde> gdeHubApiResponse) {
        LinkedHashMultimap<String, Gde> multimap = LinkedHashMultimap.create();
        for (Gde gde : gdeHubApiResponse.getItems()) {
            for (String productCategory : gde.getProducts()) {
                multimap.put(productCategory, gde);
            }
        }
        return multimap;
    }

    @Override
    public void success(HubApiResponse<Gde> gdeHubApiResponse, Response response) {
        AvatarService.start(getActivity(), gdeHubApiResponse.getItems());
        init(convertToVeryHelpfulMap(gdeHubApiResponse));
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getResponse().getReason() + ": " + error.getResponse().getStatus(), Toast.LENGTH_LONG).show();
    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mRowsAdapter.notifyArrayItemRangeChanged(0, mRowsAdapter.size());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(AvatarService.MY_SUPER_ACTION);
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(myReceiver);
        super.onPause();
    }
}
