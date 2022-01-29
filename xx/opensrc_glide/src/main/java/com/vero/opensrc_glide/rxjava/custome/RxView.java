package com.vero.opensrc_glide.rxjava.custome;

import android.view.View;

import io.reactivex.rxjava3.core.Observable;

public class RxView {

    public static final String TAG = RxView.class.getSimpleName();


    public static Observable<Object> clicks(View view){
        return new ObservableViewClick(view);
    }

}
