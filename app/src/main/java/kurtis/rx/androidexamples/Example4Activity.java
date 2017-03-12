package kurtis.rx.androidexamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * point Subject
 * Subject这个对象既是Observable又是Observer，我会把Subject想象成一个管道：从一端把数据注入，结果就会从另一端输出。
 * 因为它是一个Observer，它可以订阅一个或多个Observable；
 * 又因为它是一个Observable，它可以转发它收到(Observe)的数据，也可以发射新的数据。
 * <p>
 * Subject的种类
 * <p>
 * 一共有四种类型的Subject,分别为AsyncSubject、BehaviorSubject、PublishSubject和ReplaySubject。
 */
public class Example4Activity extends AppCompatActivity {

    private TextView mCounterDisplay;
    private Button mIncrementButton;
    private PublishSubject<Integer> mCounterEmitter;

    private int mCounter = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createCounterEmitter();
    }

    private void createCounterEmitter() {
        mCounterEmitter = PublishSubject.create();
        mCounterEmitter.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                mCounterDisplay.setText(String.valueOf(integer));
            }
        });
    }

    private void configureLayout() {
        setContentView(R.layout.activity_example_4);
        configureCounterDisplay();
        configureIncrementButton();
    }

    private void configureCounterDisplay() {
        mCounterDisplay = (TextView) findViewById(R.id.counter_display);
        mCounterDisplay.setText(String.valueOf(mCounter));
    }

    private void configureIncrementButton() {
        mIncrementButton = (Button) findViewById(R.id.increment_button);
        mIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIncrementButtonClick();
            }
        });
    }

    private void onIncrementButtonClick() {
        mCounter++;
        mCounterEmitter.onNext(mCounter);
    }
}
