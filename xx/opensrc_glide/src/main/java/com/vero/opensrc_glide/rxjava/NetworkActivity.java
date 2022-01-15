package com.vero.opensrc_glide.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vero.opensrc_glide.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity {
    TextView mTv;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mTv=findViewById(R.id.tv);
    }

    @SuppressLint("CheckResult")
    public void doThreadWithScheduler(View view) {
        Observable.just("vero", "vnix", "vvvv")
                .subscribeOn(Schedulers.io())//指定发射线程
                .observeOn(AndroidSchedulers.mainThread())//指定接收线程
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, s + "---subscribe 线程:" + Thread.currentThread().getName());
                        mTv.setText(s);
                    }
                });
    }

    public void doWeatherCompute(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("start");
                SystemClock.sleep(2000);

                emitter.onNext("sleep 2000 ");
                SystemClock.sleep(3000);

                emitter.onNext("sleep 3000 ");
                SystemClock.sleep(5000);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, s);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }


    /**
     * okhttp 同步请求
     * @param view
     */
    @SuppressLint("CheckResult")
    public void rxjavaSync(View view) {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                Log.e(TAG, "11111线程:" + Thread.currentThread().getName() + "\n");
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                Call call = okHttpClient.newCall(new Request.Builder()
                        .url("http://www.baidu.com")
                        .build());
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).subscribeOn(Schedulers.io())//IO线性发射
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        Log.e(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        String string = response.body().string();
                        mTv.setText(string);
                    }
                });
    }

    /**
     * okhttp 异步步请求
     * @param view
     */
    @SuppressLint("CheckResult")
    public void rxjavaAsync(View view) {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(final ObservableEmitter<Response> emitter) throws Exception {
                Log.e(TAG, "11111线程:" + Thread.currentThread().getName() + "\n");

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                Call call = okHttpClient.newCall(new Request.Builder()
                        .url("http://www.baidu.com")
                        .build());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e(TAG, "222222线程:" + Thread.currentThread().getName() + "\n");
                        emitter.onNext(response);
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread())//IO线性发射
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        Log.e(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        String string = response.body().string();
                        mTv.setText(string);
                    }
                });
    }
}
