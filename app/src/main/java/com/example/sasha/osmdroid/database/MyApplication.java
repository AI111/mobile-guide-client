package com.example.sasha.osmdroid.database;

import android.app.Application;

/**
 * Created by sasha on 1/26/15.
 */
     public class MyApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            HelperFactory.setHelper(getApplicationContext());
        }
        @Override
        public void onTerminate() {
            HelperFactory.releaseHelper();
            super.onTerminate();
        }
    }
