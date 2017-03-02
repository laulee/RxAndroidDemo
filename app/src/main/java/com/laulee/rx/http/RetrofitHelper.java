package com.laulee.rx.http;

import com.laulee.rx.BuildConfig;
import com.laulee.rx.app.Constants;
import com.laulee.rx.http.service.GitHubService;
import com.laulee.rx.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by laulee on 17/3/1.
 */

public class RetrofitHelper {

    private static GitHubService gitHubService = null;

    public RetrofitHelper() {
        //initOkHttpClent( );
        gitHubService = createService( GitHubService.class, Constants.BASE_URL,
                                       new OkHttpClient.Builder( ).build( ) );
    }

    /**
     * 静态内部类方式创建单例
     *
     * @return
     */
    public static final RetrofitHelper getInstance() {
        return RetrofitInnerClass.instance;
    }

    private static OkHttpClient initOkHttpClent() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder( );
        if( BuildConfig.DEBUG ) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor( );
            loggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BASIC );
            builder.addInterceptor( loggingInterceptor );
        }
        File cacheFile = new File( Constants.PATH_CACHE );
        Cache cache = new Cache( cacheFile, 1024 * 1024 * 50 );
        Interceptor cacheInterceptor = new Interceptor( ) {
            @Override
            public Response intercept( Chain chain ) throws IOException {
                Request request = chain.request( );
                if( !SystemUtil.isNetworkConnected( ) ) {
                    request = request.newBuilder( ).cacheControl( CacheControl.FORCE_CACHE )
                            .build( );
                }
                Response response = chain.proceed( request );
                if( SystemUtil.isNetworkConnected( ) ) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder( ).header( "Cache-Control", "public, max-age=" + maxAge )
                            .removeHeader( "Pragma" ).build( );
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder( ).header( "Cache-Control",
                                                   "public, only-if-cached, max-stale=" + maxStale )
                            .removeHeader( "Pragma" ).build( );
                }
                return response;
            }
        };
        //        Interceptor apikey = new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request request = chain.request();
        //                request = request.newBuilder()
        //                        .addHeader("apikey",Constants.KEY_API)
        //                        .build();
        //                return chain.proceed(request);
        //            }
        //        };
        //        builder.addInterceptor(apikey);
        //设置缓存
        builder.addNetworkInterceptor( cacheInterceptor );
        builder.addInterceptor( cacheInterceptor );
        builder.cache( cache );
        //设置超时
        builder.connectTimeout( 10, TimeUnit.SECONDS );
        builder.readTimeout( 20, TimeUnit.SECONDS );
        builder.writeTimeout( 20, TimeUnit.SECONDS );
        //错误重连
        builder.retryOnConnectionFailure( true );
        return builder.build( );
    }

    public static GitHubService getGitHubService() {
        return getInstance( ).gitHubService;
    }

    public <T> T createService( Class<T> serviceClass, String baseUrl, OkHttpClient okHttpClient ) {
        Retrofit retrofit = new Retrofit.Builder( ).baseUrl( baseUrl ).client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create( ) )
                .addCallAdapterFactory( RxJavaCallAdapterFactory.create( ) ).build( );
        return retrofit.create( serviceClass );
    }

    private static class RetrofitInnerClass {
        public static final RetrofitHelper instance = new RetrofitHelper( );
    }

    /**
     * 比较纠结是否使用建造着模式
     *
     * @param <T>
     */
    //    public static class Builder<T> {
    //
    //        private String baseUrl;
    //        private Class<T> serviceClass;
    //        private OkHttpClient okHttpClient;
    //
    //        public Builder() {
    //        }
    //
    //        public Builder<T> baseUrl( String baseUrl ) {
    //            this.baseUrl = baseUrl;
    //            return this;
    //        }
    //
    //        public Builder<T> client( OkHttpClient okHttpClient ) {
    //            this.okHttpClient = okHttpClient;
    //            return this;
    //        }
    //
    //        public Builder<T> create( Class<T> serviceClass ) {
    //            this.serviceClass = serviceClass;
    //            return this;
    //        }
    //
    //        public T build() {
    //            if( serviceClass == null ) {
    //                throw new NullPointerException( "serviceClass is a null object" );
    //            }
    //            if( baseUrl == null )
    //                baseUrl = Constants.BASE_URL;
    //            if( okHttpClient == null )
    //                okHttpClient = initOkHttpClent( );
    //            Retrofit retrofit = new Retrofit.Builder( ).baseUrl( baseUrl ).client(
    // okHttpClient )
    //                    .addConverterFactory( GsonConverterFactory.create( ) )
    //                    .addCallAdapterFactory( RxJavaCallAdapterFactory.create( ) ).build( );
    //            return retrofit.create( serviceClass );
    //        }
    //
    //    }

}
