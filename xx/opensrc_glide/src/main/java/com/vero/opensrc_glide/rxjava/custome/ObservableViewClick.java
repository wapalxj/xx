package com.vero.opensrc_glide.rxjava.custome;

import android.os.Looper;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ObservableViewClick extends Observable<Object> {

    public final View mView;
    private static Object EVENT2;

    public ObservableViewClick(View view) {
        mView = view;
        EVENT2 = new Object();
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super Object> observer) {
        ViewClickObserver parent = new ViewClickObserver(observer);
        observer.onSubscribe(parent);

        mView.setOnClickListener(parent);
    }

    public final class ViewClickObserver implements Disposable, View.OnClickListener {

        private final Observer<Object> mObserver;
        private final AtomicBoolean isDisposed = new AtomicBoolean();

        public ViewClickObserver(Observer<Object> observer) {
            mObserver = observer;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                mObserver.onNext(EVENT2);
            }
        }

        @Override
        public void dispose() {
            if (isDisposed.compareAndSet(false, true)) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mView.setOnClickListener(null);
                } else {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mView.setOnClickListener(null);
//                        }
//                    });
                    AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                        @Override
                        public void run() {
                            mView.setOnClickListener(null);
                        }
                    });
                }

            }
        }

        @Override
        public boolean isDisposed() {
            return isDisposed.get();
        }
    }
}
