package com.vero.opensrc_glide.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vero.opensrc_glide.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PollingActivity extends AppCompatActivity {

    private static final String TAG = PollingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
    }

    /**
     * interval操作符
     * 创建一个按固定时间间隔发射整数序列的Observable
     */
    private Disposable mDisposable;

    @SuppressLint("CheckResult")
    public void polling(View view) {
        if (mDisposable == null) {
            final long startTime = System.currentTimeMillis();
            // 由于interval默认在新线程，所以我们应该切回主线程
            mDisposable = Observable.interval(0, 2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            Log.e(TAG, "interval :" + aLong + " at " + (System.currentTimeMillis() - startTime));
                        }
                    });
        } else {
            //取消任务
            mDisposable.dispose();
            mDisposable = null;
        }



    }
}