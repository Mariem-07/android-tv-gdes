package com.android.example.leanback.data;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by maui on 18.11.14.
 */
public interface GoogleAvatarService {

    @GET("/plus/v1/people/{plusId}?fields=cover%2FcoverPhoto%2Furl%2Cimage&key=AIzaSyBkHUlp6Y4i2xPXmX1_HypE9YpAREpSqUg")
    public void getUserImageStuff(@Path("plusId") String plusId, Callback<PlusPhotoInfo> callback);
}
