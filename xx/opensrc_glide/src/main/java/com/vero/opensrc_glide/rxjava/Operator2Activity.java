package com.vero.opensrc_glide.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vero.opensrc_glide.R;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Operator2Activity extends AppCompatActivity {
    private static final String TAG = Operator2Activity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
    }


    /**
     * distinct操作符
     * 去重
     */
    @SuppressLint("CheckResult")
    public void distinct(View view) {
        Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "distinct : " + integer);
                    }
                });
    }

    /**
     * filter操作符
     * 过滤
     */
    @SuppressLint("CheckResult")
    public void filter(View view) {
        Observable.just(1, 20, 65, -5, 7, 19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        //过滤掉小于10的值
                        return integer >= 10;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "filter : " + integer);
                    }
                });
    }

    /**
     * buffer操作符
     */
    @SuppressLint("CheckResult")
    public void buffer(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        Log.e(TAG, "buffer size : " + integers.size() + "\n");
                        Log.e(TAG, "buffer value : ");
                        for (Integer i : integers) {
                            Log.e(TAG, i + "");
                        }
                        Log.e(TAG, "\n");
                    }
                });
    }

    /**
     * timer操作符
     * <p>
     * 在一个给定的延迟后发射一个特殊的值
     */
    @SuppressLint("CheckResult")
    public void timer(View view) {
        final long startTime = System.currentTimeMillis();
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.e(TAG, "timer :" + aLong + " at " + (System.currentTimeMillis() - startTime));
                    }
                });
    }


    /**
     * interval操作符
     * 创建一个按固定时间间隔发射整数序列的Observable
     */
    private Disposable mDisposable;

    @SuppressLint("CheckResult")
    public void interval(View view) {
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

    /**
     * doOnNext操作符
     * <p>
     * <p>
     * 让订阅者在接收到数据之前干点有意思的事情
     */
    @SuppressLint("CheckResult")
    public void doOnNext(View view) {
        Observable.just(100)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        Log.e(TAG, "doOnNext --doOnSubscribe=="+Thread.currentThread());
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "doOnNext 保存 " + integer + "成功=="+Thread.currentThread());
                    }
                })
                .compose(TestRxjavaMainActivity.rxud())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "doOnNext :" + integer+"==="+Thread.currentThread());
                    }
                });
    }

    /**
     * skip操作符
     * <p>
     * 跳过 count 个数目开始接收
     */
    @SuppressLint("CheckResult")
    public void skip(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "receive : " + integer);
                    }
                });
    }

    /**
     * skip操作符
     * 接受一个 long 型参数 count ，代表至多接收 count 个数据
     */
    public void take(View view) {
        Flowable.fromArray(1, 2, 3, 4, 5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "accept: take : " + integer);
                    }
                });
    }

    /**
     * single操作符
     * <p>
     * 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()。
     */
    public void single(View view) {
        Single.just(new Random().nextInt())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        Log.e(TAG, "single : onSuccess : " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "single : onError : " + e.getMessage());
                    }
                });
    }

    /**
     * debounce操作符
     * <p>
     * 防抖:
     * 当一个事件发送出来之后，
     * 在约定时间内没有再次发送这个事件，则发射这个事件，
     * 如果再次触发了，则重新计算时间
     */
    @SuppressLint("CheckResult")
    public void debounce(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                // send events with simulated time wait
                emitter.onNext(1); // skip
                Thread.sleep(400);
                emitter.onNext(2); // deliver
                Thread.sleep(505);
                emitter.onNext(3); // skip
                Thread.sleep(100);
                emitter.onNext(4); // deliver
                Thread.sleep(605);
                emitter.onNext(5); // deliver
                Thread.sleep(510);
                emitter.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "debounce :" + integer + "\n");
                    }
                });
    }


    /**
     * defer
     * 简单地时候就是每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable。
     *
     * @param view
     */
    public void defer(View view) {
        Observable.defer(new Supplier<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> get() throws Throwable {
                return Observable.just(1, 2, 3);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e(TAG, "defer : " + integer + "\n");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "defer : onError : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "defer : onComplete\n");
            }
        });
    }

    /**
     * last
     * 仅发射可观察到的最后一个值
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void last(View view) {
        Observable.just(1, 2, 3)
                .last(4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "last : " + integer + "\n");
                    }
                });
    }

    /**
     * merge
     * 把多个 Observable 结合起来
     *
     * @param view
     */
    @SuppressLint("CheckResult")
    public void merge(View view) {
        Observable.merge(Observable.just(1, 2), Observable.just(3, 4, 5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "accept: merge :" + integer + "\n");
                    }
                });
    }

    /**
     * reduce
     *  每次用一个方法处理所有值，可以有一个 seed 作为初始值
     * @param view
     */
    @SuppressLint("CheckResult")
    public void reduce(View view) {
        Observable.just(1, 2, 3)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "accept: reduce : " + integer + "\n");
                    }
                });
    }

    /**
     * scan
     *  	和 reduce 一致，
            区别是 reduce 是个只追求结果的坏人，而  scan 会始终如一地把每一个步骤都输出
     * @param view
     */
    @SuppressLint("CheckResult")
    public void scan(View view) {
        Observable.just(1, 2, 3)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                Log.e(TAG, "accept: scan " + integer + "\n");
            }
        });
    }

    /**
     * window
     *  按照实际划分窗口，将数据发送给不同的 Observable
     * @param view
     */
    @SuppressLint("CheckResult")
    public void window(View view) {
        Log.e(TAG, "window\n");
        Observable.interval(1, TimeUnit.SECONDS) // 间隔一秒发一次
                .take(15) // 最多接收15个
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(@NonNull Observable<Long> longObservable) throws Exception {
                        Log.e(TAG, "Sub Divide begin...\n");
                        longObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long aLong) throws Exception {
                                        Log.e(TAG, "Next:" + aLong + "\n");
                                    }
                                });
                    }
                });
    }
}
