package com.laulee.rx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.laulee.rx.R;

/**
 * Created by laulee on 17/3/13.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    // 跳转简单的页面
    public void gotoSimpleModule( View view ) {
        startActivity( new Intent( this, SimpleActivity.class ) );
    }

    // 跳转复杂的页面
    public void gotoMoreModule( View view ) {
        startActivity( new Intent( this, MoreActivity.class ) );
    }

    // 跳转Lambda的页面
    public void gotoLambdaModule( View view ) {
        startActivity( new Intent( this, LambdaActivity.class ) );
    }

    // 跳转网络的页面
    public void gotoRetrofitModule( View view ) {
        startActivity( new Intent( this, GitHubUserActivity.class ) );
    }

    // 跳转线程安全的页面
    public void gotoCycleModule( View view ) {
        startActivity( new Intent( this, RxCycleLifeActivity.class ) );
    }
}