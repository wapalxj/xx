package com.vero.opensrc_glide.rxjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vero.opensrc_glide.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.observable.ObservableJust;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestRxjavaMainActivity extends AppCompatActivity {

    private static final String TAG = TestRxjavaMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_main);
        testHook();
    }

    public static <UD> ObservableTransformer<UD, UD> rxud() {
        return new ObservableTransformer<UD, UD>() {
            @Override
            public @NonNull ObservableSource<UD> apply(@NonNull Observable<UD> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public void rxjava(View view) {

        //创建被观察者
        //observable=====本质上是ObservableCreate
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //调用观察者的回调
                emitter.onNext("我是");
                emitter.onNext("RxJava");
                emitter.onNext("简单示例");
                emitter.onError(new Throwable("出错了"));
                emitter.onComplete();
            }
        });

        //方式2：通过just创建被观察者
        Observable<String> observableJust = Observable.just("vero", "vnix");

        //方式3：通过from创建被观察者
        Observable<String> observableFrom = Observable.fromArray("vvvv", "vnix");

        //创建观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onCompleted");
            }

            //onSubscribe()方法是最先调用的
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        //注册，将观察者和被观察者关联，将会触发OnSubscribe.call方法

        //本质上是ObservableCreate.subscribe()
        observable.subscribe(observer);


//        observableJust.subscribe(observer);
//        observableFrom.subscribe(observer);
    }

    //链式调用
    public void rxjavaChain(View view) {
        //创建被观察者
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //调用观察者的回调
                emitter.onNext("我是");
                emitter.onNext("RxJava");
                emitter.onNext("简单示例");
                emitter.onError(new Throwable("出错了"));
                emitter.onComplete();
            }

        })
                .compose(rxud())
                .subscribe(new Observer<String>() {
                    //注册观察者
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "subscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, s);
                    }
                });
    }


    /**
     * map操作符
     */

    @SuppressLint("CheckResult")
    public void map(View view) {
        //创建被观察者

        //创建ObservableCreate,---持有ObservableOnSubscribe
        //ObservableCreate.map----创建ObservableMap,并持有创建ObservableCreate和Function
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //调用观察者的回调
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }

        }).map(new Function<Integer, Integer>() {
            //将一个 Observable 通过某种函数关系，转换为另一种 Observable
            //将onNext中的值先进行处理
            @Override
            public Integer apply(Integer integer) throws Exception {
                //将onNext中的值 * 100
                return integer * 100;
            }
        }).map(new Function<Integer, Integer>() {
            //将一个 Observable 通过某种函数关系，转换为另一种 Observable
            //将onNext中的值先进行处理
            @Override
            public Integer apply(Integer integer) throws Exception {
                //将onNext中的值 * 100
                return integer * 100;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, integer + "");
            }
        });
    }


    /**
     * zip操作符
     */

    @SuppressLint("CheckResult")
    public void zip(View view) {
        Observable.zip(getStringObservable(), getIntegerObservable(),
                new BiFunction<String, Integer, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                        return s + integer;
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, "zip : accept : " + s + "\n");
            }
        });
    }

    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("A");
                    Log.e(TAG, "String emit : A \n");
                    e.onNext("B");
                    Log.e(TAG, "String emit : B \n");
                    e.onNext("C");
                    Log.e(TAG, "String emit : C \n");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(1);
                    Log.e(TAG, "Integer emit : 1 \n");
                    e.onNext(2);
                    Log.e(TAG, "Integer emit : 2 \n");
                    e.onNext(3);
                    Log.e(TAG, "Integer emit : 3 \n");
                    e.onNext(4);
                    Log.e(TAG, "Integer emit : 4 \n");
                    e.onNext(5);
                    Log.e(TAG, "Integer emit : 5 \n");
                }
            }
        });
    }


    /**
     * concat操作符
     */
    @SuppressLint("CheckResult")
    public void concat(View view) {
        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "concat : " + integer);
                    }
                });
    }

    /**
     * flatmap操作符
     */
    @SuppressLint("CheckResult")
    public void flatmap(View view) {

//        Observable.just("vero", "vnix", "vvvvvvvvv")
//                .flatMap(new Function<String, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(String s) throws Exception {
//                        Log.e(TAG, "flatMap : apply : " + s);
//                        int delayTime = (int) (1 + Math.random() * 10);
////                        return  Observable.just(s);
//                        //做1个延时
//                        return Observable.just(s).delay(delayTime, TimeUnit.MILLISECONDS);
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e(TAG, "flatMap : accept : " + s);
//                    }
//                });


        Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(i + "===I am value " + integer);
                        }
                        int delayTime = (int) (1 + Math.random() * 10);
                        //将每个事件重新组装成3个
                        return Observable.fromIterable(list);
//                        return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        //一共可以接收到9个
                        Log.e(TAG, "flatMap : accept : " + s);
                    }
                });
    }

    /**
     * concatMap操作符
     */
    @SuppressLint("CheckResult")
    public void concatMap(View view) {
        Observable.just("vero", "vnix", "vvvvvvvvv")
                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        int delayTime = (int) (1 + Math.random() * 10);
                        //做1个延时
                        return Observable.just(s).delay(delayTime, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "flatMap : accept : " + s);
                    }
                });

//        Observable.just(1, 2, 3)
//                .concatMap(new Function<Integer, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
//                        List<String> list = new ArrayList<>();
//                        for (int i = 0; i < 3; i++) {
//                            list.add("I am value " + integer);
//                        }
//                        int delayTime = (int) (1 + Math.random() * 10);
//                        return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        Log.e(TAG, "flatMap : accept : " + s );
//                    }
//                });
    }


    /**
     * throttleFirst操作符
     * <p>
     * 特定时间段内，只取第一个发射的数据
     * <p>
     * 每500ms，只取第一个发射的数据
     */
    @SuppressLint("CheckResult")
    public void throttleFirst(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                // send events with simulated time wait
                emitter.onNext("0"); // deliver
                Thread.sleep(100);
                emitter.onNext("100"); // skip
                Thread.sleep(205);
                emitter.onNext("205"); // skip
                Thread.sleep(300);
                emitter.onNext("300"); // deliver
                Thread.sleep(605);
                emitter.onNext("605"); // deliver
                emitter.onComplete();
            }
        }).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "throttleFirst : throttleFirst : " + s);
                    }
                });
    }

    /**
     * throttleLast操作符
     * <p>
     * 特定时间段内，只取最后一个发射的数据
     * <p>
     * 每500ms，只取最后一个发射的数据
     */
    @SuppressLint("CheckResult")
    public void throttleLast(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                // send events with simulated time wait
                emitter.onNext("0"); // skip
                Thread.sleep(100);
                emitter.onNext("100"); // skip
                Thread.sleep(205);
                emitter.onNext("205"); // deliver
                Thread.sleep(300);
                emitter.onNext("300"); // deliver
                Thread.sleep(605);
                emitter.onNext("605"); // skip
                emitter.onComplete();
            }
        }).throttleLast(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "throttleLast : throttleLast : " + s);
                    }
                });
    }


    /**
     * testHook
     */
    public void testHook() {
        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
            @Override
            public Observable apply(Observable observable) throws Throwable {

                Log.e(TAG, "testHook======= : testHook : " + observable);
                return observable;
            }
        });

    }

    /**
     * testDisposable
     */
    public void testDisposable() {
        //创建被观察者
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //调用观察者的回调
                emitter.onNext("我是");
                emitter.onNext("RxJava");
                emitter.onNext("简单示例");
                emitter.onError(new Throwable("出错了"));
                emitter.onComplete();
            }

        })
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                });

    }

    public void moreOperator(View view) {
        Intent intent = new Intent(this, Operator2Activity.class);
        startActivity(intent);

    }

    public void network(View view) {
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }

    public void polling(View view) {
        Intent intent = new Intent(this, PollingActivity.class);
        startActivity(intent);
    }

    public void flowable(View view) {
//        Intent intent = new Intent(this, PollingActivity.class);
//        startActivity(intent);
    }
}

