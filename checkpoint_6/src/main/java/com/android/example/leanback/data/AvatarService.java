package com.android.example.leanback.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class AvatarService extends IntentService {

    public static final String IDS = "ids";
    public static final String MY_SUPER_ACTION = "my_super_action";

    public static void start(Context context, List<Gde> gdes) {
        ArrayList<String> plusIds = Lists.newArrayList();
        for (Gde gde : gdes) {
            plusIds.add(gde.getId());
        }

        Intent intent = new Intent(context, AvatarService.class);
        intent.putStringArrayListExtra(IDS, plusIds);
        context.startService(intent);
    }

    public AvatarService() {
        super(AvatarService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.googleapis.com/plus/v1/")
                .setConverter(new GsonConverter(gson))
                .build();

        GoogleAvatarService googleAvatarService = restAdapter.create(GoogleAvatarService.class);
        ArrayList<String> ids = intent.getStringArrayListExtra(IDS);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int count = 0;
        for (String id : ids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String preferenceKey = buildPreferenceKeyForId(id);
            if (prefs.contains(preferenceKey)) {
                
            }
            PlusPhotoInfo userImageStuff = googleAvatarService.getUserImageStuff(id);

            String url = userImageStuff.getImage().getUrl();
            Log.d("super_tag", url);
            prefs.edit()
                    .putString(preferenceKey, url.replace("sz=50", "sz=256"))
                    .commit();
            if ((count++) % 10 == 0) {
                sendBroadcast(new Intent(MY_SUPER_ACTION));
            }
        }
    }

    public static String buildPreferenceKeyForId(String id) {
        return "avatar_" + id;
    }
}
