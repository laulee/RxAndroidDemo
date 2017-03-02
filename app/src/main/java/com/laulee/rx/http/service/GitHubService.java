package com.laulee.rx.http.service;

import com.laulee.rx.bean.GitHubUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by laulee on 17/3/1.
 */

public interface GitHubService {

    // 获取个人信息
    @GET("/users/{user}")
    Observable<GitHubUser> getUserData( @Path("user") String user );
}
