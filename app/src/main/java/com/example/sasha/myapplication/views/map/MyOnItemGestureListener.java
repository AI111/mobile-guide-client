package com.example.sasha.myapplication.views.map;

/**
 * Created by sasha on 3/12/15.
 */
public interface MyOnItemGestureListener<T, C> {

    public boolean onItemSingleTapUp(final int index, final T item, C point);

    public boolean onItemLongPress(final int index, final T item, C point);

}
