package com.example.sasha.myapplication.utils.rest.api;

import android.content.Context;
import android.util.Log;

import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.views.MainActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sasha on 6/14/15.
 */
public class APIUtils {

    public static MyAsyncTask<String, Void, ArrayList<Guide>> getGuides(String token, final Context context) {
        return new MyAsyncTask<String, Void, ArrayList<Guide>>() {
            String url;

            @Override
            protected void onPreExecute() {
                url = context.getString(R.string.get_guides);
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Guide> doInBackground(String... params) {
                ArrayList<Guide> cityGuides = null;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    cityGuides = new ArrayList<Guide>(Arrays.asList(restTemplate.getForObject(url, Guide[].class)));
                    Log.d(MainActivity.LOG_TAG, cityGuides.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return cityGuides;
            }
        };
    }
}
