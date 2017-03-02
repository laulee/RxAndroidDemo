package com.laulee.rx.ui.rxcyclelife;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.laulee.rx.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by laulee on 17/3/2.
 */

public class RxCycleLifeActivity extends RxAppCompatActivity {

    @BindView(R.id.tv_rxcycle_life_time)
    TextView mTvText;
    private String TAG = getClass( ).getSimpleName( );

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rxcycle_life );
        ButterKnife.bind( this );

        Observable.interval( 1, TimeUnit.SECONDS ).observeOn( AndroidSchedulers.mainThread( ) )
                .compose( bindToLifecycle( ) ).subscribe( this::showTime );
    }

    private void showTime( Long time ) {
        mTvText.setText( String.valueOf( "时间计数: " + time ) );
        Log.d( TAG, "时间计数器: " + time );
    }

}
