package com.android.example.leanback.data;

import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

public class GdeObjectAdapter extends ObjectAdapter {

    private final List<Gde> mGdes;

    public GdeObjectAdapter(Presenter presenter, Collection<Gde> gdes) {
        super(presenter);
        mGdes = Lists.newArrayList(gdes);
    }

    @Override
    public int size() {
        return mGdes.size();
    }

    @Override
    public Object get(int i) {
        return mGdes.get(i);
    }
}
