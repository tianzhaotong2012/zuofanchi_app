package com.zfc.app.zuofanchi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfc.app.zuofanchi.NetInterface.Detail;
import com.zfc.app.zuofanchi.model.DetailModel;
import com.zfc.app.zuofanchi.model.PostItem;
import com.zfc.app.zuofanchi.model.PostListModel;
import com.zfc.app.zuofanchi.model.ResDetailModel;
import com.zfc.app.zuofanchi.model.ResModel;
import com.zfc.app.zuofanchi.model.ResStatusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by tian on 2019/12/14.
 */

public class DetailActivity extends Activity {
    //获取组件
    @BindView(R.id.detail_layout_collect)
    Button collectBt;
    @BindView(R.id.detail_layout_pic)
    ImageView iv_pic;
    @BindView(R.id.detail_layout_title)
    TextView tv_title;
    @BindView(R.id.detail_layout_desc)
    TextView tv_desc;
    @BindView(R.id.detail_layout_steps)
    com.tencent.smtt.sdk.WebView wv_steps;

    private String postId;
    PostItem thisPost;
    private boolean isCollected;

    private List<PostItem> viewedPosts;
    private List<PostItem> collectedPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        postId = (String) intent.getSerializableExtra("postId");

        viewedPosts = getViewedPosts();
        collectedPosts = getCollectedPosts();
        for(PostItem collectedPost : collectedPosts){
            if(collectedPost.getPostId().equals(postId)){
                isCollected = true;
                collectBt.setText("取消收藏");
                break;
            }else{
                isCollected = false;
                collectBt.setText("收藏");
            }
        }

        init();
    }

    private void init(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zuofanchi.cn/")
                .callFactory(okHttpClient)
                .build();
        Detail req = retrofit.create(Detail.class);
        Call<ResponseBody> call = req.Detail("load_post_detail_v1_json", postId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String resBodyStr = null;
                try {
                    resBodyStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(resBodyStr);
                Gson gson = new Gson();
                ResStatusModel resStatusModel = gson.fromJson(resBodyStr, ResStatusModel.class);
                if(resStatusModel.getErrNo().equals("0")) {
                    ResDetailModel resDeatilModel = gson.fromJson(resBodyStr, ResDetailModel.class);
                    System.out.println(resDeatilModel.toString());
                    tv_title.setText(resDeatilModel.getData().getPostTitle() + "的做法");
                    tv_desc.setText(resDeatilModel.getData().getDesc());
                    if(resDeatilModel.getData().getDesc().isEmpty()){
                        tv_desc.setVisibility(View.GONE);
                    }
                    Glide.with(getBaseContext()).load(resDeatilModel.getData().getCover()).into((iv_pic));
                    wv_steps.setBackgroundColor(0); // 设置背景色
                    wv_steps.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
                    wv_steps.loadDataWithBaseURL(null,  resDeatilModel.getData().getSteps(), "text/html", "UTF-8", null);

                    thisPost = new PostItem();
                    thisPost.setPostId(resDeatilModel.getData().getPostId());
                    thisPost.setPostTitle(resDeatilModel.getData().getPostTitle());
                    thisPost.setCover(resDeatilModel.getData().getCover());
                    addViewedPosts(thisPost);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.detail_layout_back, R.id.detail_layout_collect, R.id.detail_layout_share})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_layout_back:
                finish();
                return;
                //overridePendingTransition(R.anim.right_out, R.anim.left_in);
            case R.id.detail_layout_collect:
                Button collectBt = (Button) view;
                if(isCollected == false){
                    collectBt.setText("收藏成功");
                    collectBt.setEnabled(false);
                    addCollectedPosts(thisPost);
                }else {
                    collectBt.setText("已取消");
                    collectBt.setEnabled(false);
                    removeCollectedPosts(thisPost);
                }

                //overridePendingTransition(R.anim.right_out, R.anim.left_in);
                return;
            case R.id.detail_layout_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.zuofanchi.cn/archives/" +  postId);
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return;
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

    void addViewedPosts(PostItem postItem){
        viewedPosts.add(postItem);
        viewedPosts = unPostList(viewedPosts);
        Gson gson = new Gson();
        String viewedposts = gson.toJson(viewedPosts);
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        setting.edit().putString("viewedposts", viewedposts).commit();
    }

    private List<PostItem> getCollectedPosts(){
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        String collectedposts = setting.getString("collectedposts", "");
        if(collectedposts.equals("")){
            return new ArrayList<>();
        }else{
            Gson gson = new Gson();
            List<PostItem> postList = gson.fromJson(collectedposts,new TypeToken<List<PostItem>>() {}.getType());
            if(postList.size() > 100){
                postList.remove(0);
            }
            return postList;
        }
    }

    void addCollectedPosts(PostItem postItem){
        collectedPosts.add(postItem);
        collectedPosts = unPostList(collectedPosts);
        Gson gson = new Gson();
        String collectedposts = gson.toJson(collectedPosts);
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        setting.edit().putString("collectedposts", collectedposts).commit();
    }

    void removeCollectedPosts(PostItem postItem){
        for(int i = 0; i< collectedPosts.size();i++){
            if(collectedPosts.get(i).getPostId().equals(postItem.getPostId())){
                collectedPosts.remove(i);
            }
        }
        Gson gson = new Gson();
        String collectedposts = gson.toJson(collectedPosts);
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        setting.edit().putString("collectedposts", collectedposts).commit();
    }

    //根据postId对list去重
    List<PostItem> unPostList(List<PostItem> postList){
        List<String> postIds = new ArrayList<>();
        List<PostItem> unPostList = new ArrayList<>();
        for(PostItem postItem : postList){
            if(!postIds.contains(postItem.getPostId())){
                postIds.add(postItem.getPostId());
                unPostList.add(postItem);
            }
        }
        return unPostList;
    }
}
