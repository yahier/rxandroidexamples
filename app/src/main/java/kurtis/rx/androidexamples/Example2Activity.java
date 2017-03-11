package kurtis.rx.androidexamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Example2Activity extends AppCompatActivity {

    private Subscription mTvShowSubscription;
    private RecyclerView mTvShowListView;
    private ProgressBar mProgressBar;
    private SimpleStringAdapter mSimpleStringAdapter;
    private RestClient mRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(this);
        configureLayout();
        createObservable();
        test1();
    }


    void test1() {
        fetchObservable()
                // 指定 subscribe() 发生在 IO 线程
                .subscribeOn(Schedulers.io())
                // 指定 Subscriber 的回调发生在主线程
                .observeOn(AndroidSchedulers.mainThread())
                //will listen the results on the main thread
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    //有更多接口可以复写喔

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<String> list) {
                        Log.e("test1", list.get(0));
                    }
                });
    }

    public Observable<List<String>> fetchObservable() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                // Fetch information from database
                subscriber.onNext(getUserList());

                subscriber.onCompleted();
            }
        });
    }

    List<String> getUserList() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        List<String> list = new ArrayList<String>();
        list.add("hello ya");
        return list;
    }


    //test2
    private void createObservable() {
        Observable<List<String>> tvShowObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return mRestClient.getFavoriteTvShows();
            }
        });


        Log.e("Example2Activity", "createObservable开始");
        mTvShowSubscription = tvShowObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<String> tvShows) {
                                displayTvShows(tvShows);
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTvShowSubscription != null && !mTvShowSubscription.isUnsubscribed()) {
            mTvShowSubscription.unsubscribe();
        }
    }

    private void displayTvShows(List<String> tvShows) {
        mSimpleStringAdapter.setStrings(tvShows);
        mProgressBar.setVisibility(View.GONE);
        mTvShowListView.setVisibility(View.VISIBLE);
    }

    private void configureLayout() {
        setContentView(R.layout.activity_example_2);
        mProgressBar = (ProgressBar) findViewById(R.id.loader);
        mTvShowListView = (RecyclerView) findViewById(R.id.tv_show_list);
        mTvShowListView.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mTvShowListView.setAdapter(mSimpleStringAdapter);
    }
}
