package com.laulee.rx.http;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * Created by laulee on 17/3/2.
 */

public abstract class ErrorHandler<T> implements Action1<Throwable> {

    @Override
    public void call( Throwable throwable ) {
        if( throwable instanceof HttpException ) {
            HttpException error = (HttpException) throwable;
            try {
                onError( error.response( ).errorBody( ).string( ) );
            } catch( IOException e ) {
                e.printStackTrace( );
            }
        } else {
            throwable.printStackTrace( );
        }
    }

    public abstract void onError( T s );
}
