package com.zfc.app.zuofanchi;

import android.app.Activity;
import android.content.Intent;
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
import com.zfc.app.zuofanchi.NetInterface.Search;
import com.zfc.app.zuofanchi.adapter.HomeListAdapter;
import com.zfc.app.zuofanchi.model.PostItem;
import com.zfc.app.zuofanchi.model.ResModel;
import com.zfc.app.zuofanchi.model.ResStatusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Created by tian on 2019/12/9.
 */

public class SearchResltActivity extends Activity {
    private List<DiseaseBankEntity> list = new ArrayList<>();
    private HomeListAdapter homeListAdapter;
    private String query;

    private int curPageOffset = 1;

    //获取组件
    @BindView(R.id.search_reslut_list)
    RecyclerView resultRv;

    //获取组件
    @BindView(R.id.search_reslut_title)
    TextView searchTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();

        query = (String) intent.getSerializableExtra("query");

        searchTitle.setText("搜索：" + query);

        initData();

        //设置布局的方式
        resultRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        homeListAdapter = new HomeListAdapter();
        homeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                                  @Override
                                                  public void onLoadMoreRequested() {
                                                      //Toast.makeText(SearchResltActivity.this, "" + "more info---", Toast.LENGTH_SHORT).show();
                                                      loadMoreData();
                                                  }
                                              },resultRv
        );
        resultRv.setAdapter(homeListAdapter);
        resultRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("postId",homeListAdapter.getItem(position).getId());
                startActivity(intent);
                //overridePendingTransition(R.anim.right_in,R.anim.left_out);
                //Toast.makeText(SearchResltActivity.this, "" + Integer.toString(position) + homeListAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zuofanchi.cn/")
                .callFactory(okHttpClient)
                .build();
        Search req = retrofit.create(Search.class);
        Call<ResponseBody> call = req.Search("load_search_results_v1_json",query,"" + curPageOffset);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(SearchResltActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    String resBodyStr = response.body().string();
                    System.out.println(resBodyStr);
                    Gson gson = new Gson();
                    ResModel resModel = gson.fromJson(resBodyStr, ResModel.class);
                    System.out.println(resModel.getErrNo());

                    for (int i = 0; i < resModel.getData().getPostList().size(); i++) {
                        DiseaseBankEntity bank = new DiseaseBankEntity();
                        bank.setId(String.valueOf(i));
                        PostItem postItem = resModel.getData().getPostList().get(i);
                        bank.setTitle(postItem.getPostTitle());
                        bank.setImgUrl(postItem.getCover());
                        bank.setShareUrl(postItem.getShareUrl());
                        bank.setId(postItem.getPostId());

                        homeListAdapter.addData(bank);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void loadMoreData(){
        curPageOffset++;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zuofanchi.cn/")
                .callFactory(okHttpClient)
                .build();
        Search req = retrofit.create(Search.class);
        Call<ResponseBody> call = req.Search("load_search_results_v1_json",query,"" + curPageOffset);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(SearchResltActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    String resBodyStr = response.body().string();
                    System.out.println(resBodyStr);
                    Gson gson = new Gson();
                    ResStatusModel resStatusModel = gson.fromJson(resBodyStr, ResStatusModel.class);
                    if(resStatusModel.getErrNo().equals("204")){
                        homeListAdapter.loadMoreEnd(true);
                        //Toast.makeText(SearchResltActivity.this, "" + getString(R.string.no_more_des), Toast.LENGTH_SHORT).show();

                    }
                    if(resStatusModel.getErrNo().equals("0")){
                        ResModel resModel = gson.fromJson(resBodyStr, ResModel.class);
                        System.out.println(resModel.getErrNo());
                        
                        for (int i = 0; i < resModel.getData().getPostList().size(); i++) {
                            DiseaseBankEntity bank = new DiseaseBankEntity();
                            bank.setId(String.valueOf(i));
                            PostItem postItem = resModel.getData().getPostList().get(i);
                            bank.setTitle(postItem.getPostTitle());
                            bank.setImgUrl(postItem.getCover());
                            bank.setShareUrl(postItem.getShareUrl());
                            bank.setId(postItem.getPostId());

                            homeListAdapter.addData(bank);
                            homeListAdapter.loadMoreComplete();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.search_reslut_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_reslut_back:
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
