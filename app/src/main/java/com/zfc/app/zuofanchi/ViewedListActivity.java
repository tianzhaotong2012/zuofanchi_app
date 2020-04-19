package com.zfc.app.zuofanchi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfc.app.zuofanchi.adapter.HomeListAdapter;
import com.zfc.app.zuofanchi.model.PostItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tian on 2020/1/8.
 */

public class ViewedListActivity extends Activity{
    private List<DiseaseBankEntity> list = new ArrayList<>();
    private HomeListAdapter homeListAdapter;

    //获取组件
    @BindView(R.id.category_list)
    RecyclerView resultRv;

    //获取组件
    @BindView(R.id.category_list_title)
    TextView searchTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category);

        ButterKnife.bind(this);

        searchTitle.setText("浏览过的菜谱");

        //设置布局的方式
        resultRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        homeListAdapter = new HomeListAdapter();
        resultRv.setAdapter(homeListAdapter);
        resultRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("postId", homeListAdapter.getItem(position).getId());
                startActivity(intent);
                //overridePendingTransition(R.anim.right_in,R.anim.left_out);
                //Toast.makeText(SearchResltActivity.this, "" + Integer.toString(position) + homeListAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        initData();
    }

    void initData(){
        List<PostItem> viewedList = getViewedPosts();

        if(viewedList.size() == 0){
            Toast.makeText(getBaseContext(), "还没有浏览任何内容", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < viewedList.size(); i++) {
            DiseaseBankEntity bank = new DiseaseBankEntity();
            bank.setId(String.valueOf(i));
            PostItem postItem = viewedList.get(i);
            bank.setTitle(postItem.getPostTitle());
            bank.setImgUrl(postItem.getCover());
            bank.setShareUrl(postItem.getShareUrl());
            bank.setId(postItem.getPostId());

            homeListAdapter.addData(bank);
        }
    }

    private List<PostItem> getViewedPosts(){
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        String viewedposts = setting.getString("viewedposts", "");
        if(viewedposts.equals("")){
            return new ArrayList<>();
        }else{
            Gson gson = new Gson();
            List<PostItem> postList = gson.fromJson(viewedposts,new TypeToken<List<PostItem>>() {}.getType());
            if(postList.size() > 100){
                postList.remove(0);
            }
            return postList;
        }
    }

    @OnClick({R.id.category_list_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_list_back:
                finish();
                //overridePendingTransition(R.anim.right_out, R.anim.left_in);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }
}
