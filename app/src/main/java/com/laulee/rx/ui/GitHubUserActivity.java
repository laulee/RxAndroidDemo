package com.laulee.rx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.laulee.rx.R;
import com.laulee.rx.adapter.BaseRecyclerAdapter;
import com.laulee.rx.adapter.GitHubUserAdapter;
import com.laulee.rx.bean.ErrorBean;
import com.laulee.rx.bean.GitHubUser;
import com.laulee.rx.http.ErrorHandler;
import com.laulee.rx.http.RetrofitHelper;
import com.laulee.rx.http.service.GitHubService;
import com.laulee.rx.ui.rxbinding.RxBindingActivity;
import com.laulee.rx.ui.rxcyclelife.RxCycleLifeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by laulee on 17/3/1.
 */

public class GitHubUserActivity extends AppCompatActivity {

    private static final String[] mFamousUsers = { "SpikeKing", "JakeWharton", "rock3r", "Takhion",
            "dextorer", "Mariuxtheone" };

    @BindView(R.id.rv_github_user)
    RecyclerView userContent;

    GitHubUserAdapter gitHubUserAdapter;

    CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_github_user );
        ButterKnife.bind( this );
        userContent.setLayoutManager( new LinearLayoutManager( this ) );

        gitHubUserAdapter = new GitHubUserAdapter( new ArrayList<GitHubUser>( ) );
        userContent.setAdapter( gitHubUserAdapter );
        compositeSubscription = new CompositeSubscription( );
        gitHubUserAdapter.setIOnItemClickListener(
                new BaseRecyclerAdapter.IOnItemClickListener<GitHubUser>( ) {
                    @Override
                    public void onItemClick( View view, GitHubUser entity, int position ) {
                        if( position % 2 == 0 ) {
                            startActivity( new Intent( GitHubUserActivity.this,
                                                       RxCycleLifeActivity.class ) );
                        } else {
                            startActivity( new Intent( GitHubUserActivity.this,
                                                       RxBindingActivity.class ) );
                        }
                    }
                } );
        getUserData( );
    }

    private void getUserData() {

        final GitHubService gitHubService = RetrofitHelper.getGitHubService( );
        Subscription subscription = gitHubService.getUserData( "adfasdfasdfas" )
                .subscribeOn( Schedulers.io( ) ).observeOn( AndroidSchedulers.mainThread( ) )
                .subscribe( gitHubUserAdapter::addItem, new ErrorHandler<ErrorBean>( ) {
                    @Override
                    public void onError( ErrorBean errorBean ) {
                        Toast.makeText( getApplicationContext( ), errorBean.getMessage( ),
                                        Toast.LENGTH_LONG ).show( );
                    }

                    @Override
                    public void onException( Throwable e ) {
                        Toast.makeText( getApplicationContext( ), e.getMessage( ),
                                        Toast.LENGTH_LONG ).show( );
                    }

                } );

        Subscription subscription1 = gitHubService.getUserData( "sean" )
                .subscribeOn( Schedulers.io( ) ).observeOn( AndroidSchedulers.mainThread( ) )
                .subscribe( gitHubUserAdapter::addItem, new Action1<Throwable>( ) {
                    @Override
                    public void call( Throwable throwable ) {
                        Toast.makeText( getApplicationContext( ), "获取数据失败", Toast.LENGTH_LONG )
                                .show( );
                    }
                } );

        compositeSubscription.add( subscription1 );

        Subscription lambda = Observable.from( mFamousUsers ).flatMap( gitHubService::getUserData )
                .subscribeOn( Schedulers.io( ) ).observeOn( AndroidSchedulers.mainThread( ) )
                .subscribe( gitHubUserAdapter::addItem );

        compositeSubscription.add( lambda );
    }

}
