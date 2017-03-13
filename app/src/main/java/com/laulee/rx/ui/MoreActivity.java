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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * just: 获取输入数据, 直接分发, 更加简洁, 省略其他回调.
 * from: 获取输入数组, 转变单个元素分发.
 * map: 映射, 对输入数据进行转换, 如大写.
 * flatMap: 增大, 本意就是增肥, 把输入数组映射多个值, 依次分发.
 * reduce: 简化, 正好相反, 把多个数组的值, 组合成一个数据.
 * Created by laulee on 17/3/13.
 */

public class MoreActivity extends AppCompatActivity {
    final String[] mManyWords = { "Hello", "I", "am", "your", "friend", "Laulee" };
    final List<String> mManyWordList = Arrays.asList( mManyWords );
    @BindView(R.id.tv_activity_simple)
    TextView textView;
    // Action类似订阅者, 设置TextView
    private Action1<String> mTextViewAction = new Action1<String>( ) {
        @Override
        public void call( String s ) {
            textView.setText( s );
        }
    };

    // Action设置Toast
    private Action1<String> mToastAction = new Action1<String>( ) {
        @Override
        public void call( String s ) {
            Toast.makeText( MoreActivity.this, s, Toast.LENGTH_SHORT ).show( );
        }
    };

    // 设置映射函数
    private Func1<List<String>, Observable<String>> mOneLetterFunc
            = new Func1<List<String>, Observable<String>>( ) {
        @Override
        public Observable<String> call( List<String> strings ) {
            return Observable.from( strings ); // 映射字符串
        }
    };

    // 设置大写字母
    private Func1<String, String> mUpperLetterFunc = new Func1<String, String>( ) {
        @Override
        public String call( String s ) {
            return s.toUpperCase( ); // 大小字母
        }
    };

    // 连接字符串
    private Func2<String, String, String> mMergeStringFunc = new Func2<String, String, String>( ) {
        @Override
        public String call( String s, String s2 ) {
            return String.format( "%s %s", s, s2 ); // 空格连接字符串
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_simple );
        ButterKnife.bind( this );

        // 添加字符串, 省略Action的其他方法, 只使用一个onNext.
        Observable<String> obShow = Observable.just( getText( ) );

        // 先映射, 再设置TextView
        obShow.observeOn( AndroidSchedulers.mainThread( ) ).map( mUpperLetterFunc )
                .subscribe( mTextViewAction );

        // 单独显示数组中的每个元素
        Observable<String> obMap = Observable.from( mManyWords );

        // 映射之后分发
        obMap.observeOn( AndroidSchedulers.mainThread( ) ).map( mUpperLetterFunc )
                .subscribe( mToastAction );

        // 优化过的代码, 直接获取数组, 再分发, 再合并, 再显示toast, Toast顺次执行.
        Observable.just( mManyWordList )
                .observeOn( AndroidSchedulers.mainThread( ) )
                .flatMap( mOneLetterFunc )
                .reduce( mMergeStringFunc )
                .subscribe( mToastAction );
    }

    private String getText() {
        return "Hello World";
    }

}
