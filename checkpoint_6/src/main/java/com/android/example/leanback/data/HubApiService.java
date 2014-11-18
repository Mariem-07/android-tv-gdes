package com.android.example.leanback.data;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by maui on 18.11.14.
 */
public interface HubApiService {

   @GET("/gdes/products")
   public void getGdeCategories(Callback<ArrayList<GdeProduct>> callback);

    @GET("/gdes/product/{productCode}")
    public void getGdeByProduct(String productCode, Callback<HubApiResponse<Gde>> callback);
}
