package com.android.example.leanback.data;

import java.util.ArrayList;

/**
 * Created by maui on 18.11.14.
 */
public class HubApiResponse<T> {

    private final int count, pages, page;
    private final ArrayList<T> items;

    public HubApiResponse(int count, int pages, int page, ArrayList<T> items) {
        this.count = count;
        this.pages = pages;
        this.page = page;
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public int getPage() {
        return page;
    }

    public ArrayList<T> getItems() {
        return items;
    }
}
