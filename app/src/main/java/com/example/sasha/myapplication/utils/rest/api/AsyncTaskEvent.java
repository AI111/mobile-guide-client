package com.example.sasha.myapplication.utils.rest.api;

/**
 * Created by sasha on 6/14/15.
 */
public interface AsyncTaskEvent<T> {
    public void onPreExecute();

    public T onPostResult(T result);
}
