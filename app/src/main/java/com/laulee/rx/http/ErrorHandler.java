package com.laulee.rx.http;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * Created by laulee on 17/3/2.
 */

public abstract class ErrorHandler<T> implements Action1<Throwable> {

    private final Class<T> errorClass;

    public ErrorHandler( Class<T> errorClass ) {
        this.errorClass = errorClass;
    }

    @Override
    public void call( Throwable throwable ) {
        if( throwable instanceof HttpException ) {
            HttpException error = (HttpException) throwable;
            try {
                onError( new Gson( ).fromJson( error.response( ).errorBody( ).string( ),
                                               errorClass ) );
            } catch( IOException e ) {
                onException( e );
                e.printStackTrace( );
            }
        } else {
            onException( throwable );
            throwable.printStackTrace( );
        }
    }

    public abstract void onError( T s );

    public abstract void onException( Throwable e );
}
