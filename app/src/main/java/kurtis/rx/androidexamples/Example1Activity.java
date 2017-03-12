package kurtis.rx.androidexamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Example1Activity extends AppCompatActivity {

    RecyclerView mColorListView;
    SimpleStringAdapter mSimpleStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private Action1<String> mToastAction = new Action1<String>() {
        @Override
        public void call(String s) {
            Toast.makeText(Example1Activity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private Action1<List<String>> mTextViewAction = new Action1<List<String>>() {
        @Override
        public void call(List<String> list) {
            for (String s : list) {
                Toast.makeText(Example1Activity.this, s, Toast.LENGTH_SHORT).show();
            }

        }
    };


    //打开注释的1,2两个地方，就是异步调用
    private void createObservable() {
        Observable<List<String>> listObservable = Observable.just(getColorList());
        // 1.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())
        listObservable.subscribe(mTextViewAction);
        listObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<String>>() {

            @Override
            public void onCompleted() {
                Log.e("Example1Activity", "createObservable onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> colors) {
//                try {
//                    Log.d("Example1Activity onNext", "开始了");
//                    Thread.sleep(5000);
//                } catch (Exception e) {
//                    Log.d("Example1Activity onNext", e.getLocalizedMessage());
//                }
                mSimpleStringAdapter.setStrings(colors);
                Log.e("Example1Activity onNext", "size: " + colors.size());
            }
        });

//        listObservable.subscribe(new Action1<List<String>>() {
//            @Override
//            public void call(List<String> strings) {
//                Log.e("createObservable", "Action1:" + strings.get(0));
//                mSimpleStringAdapter.setStrings(strings);
//            }
//        });
//
//        listObservable.doOnCompleted(new Action0() {
//            @Override
//            public void call() {
//
//            }
//        });
//
        listObservable.subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    private void configureLayout() {
        setContentView(R.layout.activity_example_1);
        mColorListView = (RecyclerView) findViewById(R.id.color_list);
        mColorListView.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mColorListView.setAdapter(mSimpleStringAdapter);
    }

    private static List<String> getColorList() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("blue");
        colors.add("green");
        colors.add("red");
        colors.add("chartreuse");
        colors.add("Van Dyke Brown");
        return colors;
    }
}
