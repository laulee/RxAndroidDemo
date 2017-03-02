package com.laulee.rx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laulee.rx.R;
import com.laulee.rx.bean.GitHubUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laulee on 17/3/2.
 */

public class GitHubUserAdapter
        extends BaseRecyclerAdapter<GitHubUser, GitHubUserAdapter.ViewHolder> {
    public GitHubUserAdapter( List<GitHubUser> datas ) {
        super( datas );
    }

    @Override
    protected ViewHolder createViewHolder( View view ) {
        return new ViewHolder( view );
    }

    @Override
    protected int layoutResId() {
        return R.layout.item_github_user;
    }

    @Override
    protected void onBindViewHolder( ViewHolder holder, GitHubUser entity ) {
        holder.bindTo( entity );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_user_icon)
        ImageView mIvUserPicture;
        @BindView(R.id.tv_item_user_name)
        TextView mTvUserName;
        @BindView(R.id.tv_item_user_login)
        TextView mTvUserLogin;

        public ViewHolder( View itemView ) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }

        // 绑定数据
        public void bindTo( GitHubUser user ) {
            mTvUserName.setText( user.getName( ) );
            mTvUserLogin.setText( user.getLogin( ) );

            Glide.with( mIvUserPicture.getContext( ) ).load( user.getAvatar_url( ) )
                    .into( mIvUserPicture );
        }
    }
}
