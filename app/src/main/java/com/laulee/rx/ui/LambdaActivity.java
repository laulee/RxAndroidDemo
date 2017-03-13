package com.laulee.rx.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.laulee.rx.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by laulee on 17/3/13.
 */

public class LambdaActivity extends AppCompatActivity {
    final String[] mManyWords = { "Hello", "World" };
    final List<String> mManyWordList = Arrays.asList( mManyWords );
    @BindView(R.id.tv_activity_simple)
    TextView mTvText;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_simple );
        ButterKnife.bind( this );

        // 添加字符串, 省略Action的其他方法, 只使用一个onNext.
        Observable<String> obShow = Observable.just( getText( ) );

        // 先映射, 再设置TextView
        obShow.observeOn( AndroidSchedulers.mainThread( ) ).map( String::toUpperCase )
                .subscribe( mTvText::setText );

        // 单独显示数组中的每个元素
        Observable<String> obMap = Observable.from( mManyWords );

        // 映射之后分发
        obMap.observeOn( AndroidSchedulers.mainThread( ) ).map( String::toUpperCase )
                .subscribe( this::showToast );

        // 优化过的代码, 直接获取数组, 再分发, 再合并, 再显示toast, Toast顺次执行.
        Observable.just( mManyWordList ).observeOn( AndroidSchedulers.mainThread( ) )
                .flatMap( Observable::from ).reduce( this::mergeString )
                .subscribe( this::showToast );
    }

    // 创建字符串
    private String getText() {
        return "Hello,World!";
    }

    // 显示Toast
    private void showToast( String s ) {
        Toast.makeText( LambdaActivity.this, s, Toast.LENGTH_SHORT ).show( );
    }

    // 合并字符串
    private String mergeString( String s1, String s2 ) {
        return String.format( "%s %s", s1, s2 );
    }
}