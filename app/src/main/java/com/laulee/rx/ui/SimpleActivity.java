package com.laulee.rx.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.laulee.rx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by laulee on 17/3/13.
 */

public class SimpleActivity extends AppCompatActivity {
    @BindView(R.id.tv_activity_simple)
    TextView textView;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_simple );
        ButterKnife.bind( this );

        // 观察事件发生
        Observable.OnSubscribe mObservableAction = new Observable.OnSubscribe<String>( ) {
            @Override
            public void call( Subscriber<? super String> subscriber ) {
                subscriber.onNext( getText( ) ); // 发送事件
                subscriber.onCompleted( ); // 完成事件
            }
        };

        // 订阅者, 接收字符串, 修改控件
        Subscriber<String> mTextSubscriber = new Subscriber<String>( ) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError( Throwable e ) {

            }

            @Override
            public void onNext( String s ) {
                textView.setText( s ); // 设置文字
            }
        };

        // 订阅者, 接收字符串, 提示信息
        Subscriber<String> mToastSubscriber = new Subscriber<String>( ) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError( Throwable e ) {

            }

            @Override
            public void onNext( String s ) {
                Toast.makeText( SimpleActivity.this, s, Toast.LENGTH_SHORT ).show( );
            }
        };

        // 注册观察活动
        @SuppressWarnings("unchecked") Observable<String> observable = Observable
                .create( mObservableAction );

        // 分发订阅信息
        observable.observeOn( AndroidSchedulers.mainThread( ) );
        observable.subscribe( mTextSubscriber );
        observable.subscribe( mToastSubscriber );
    }

    private String getText() {
        return "Hello World";
    }

}
