package com.example.sasha.myapplication.utils.rest.api;

import android.os.AsyncTask;

/**
 * Created by sasha on 6/14/15.
 */
public abstract class MyAsyncTask<P, Pr, Res> extends AsyncTask<P, Pr, Res> {
    AsyncTaskEvent<Res> asyncTaskEvent;

    public void setAsyncTaskEvent(AsyncTaskEvent taskEvent) {
        this.asyncTaskEvent = taskEvent;
    }

    @Override
    protected void onPreExecute() {
        if (asyncTaskEvent != null) asyncTaskEvent.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Res res) {
        if (asyncTaskEvent != null) asyncTaskEvent.onPostResult(res);
        super.onPostExecute(res);
    }
}
