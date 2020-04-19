package com.zfc.app.zuofanchi.fragment;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.zfc.app.zuofanchi.CommonwebviewActivity;
import com.zfc.app.zuofanchi.DetailActivity;
import com.zfc.app.zuofanchi.DiseaseBankEntity;
import com.zfc.app.zuofanchi.GetList;
import com.zfc.app.zuofanchi.R;
import com.zfc.app.zuofanchi.SearchResltActivity;
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
 * Created by tian on 2019/12/15.
 */

public class HomeFragment extends Fragment{
    private List<DiseaseBankEntity> list = new ArrayList<>();
    private BaseQuickAdapter mQuickAdapter;
    private HomeListAdapter homeListAdapter;

    private int curPageOffset = 1;

    private View mRootView;//缓存Fragment view


    //获取组件
    RecyclerView rvBank;

    @BindView(R.id.homeSearchET)
    EditText homeSearchET;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //mRootView = inflater.inflate(R.layout.home_fragment, container, false);


        if(mRootView==null){
            mRootView = View.inflate(getActivity(), R.layout.home_fragment, null);
            ButterKnife.bind(this, mRootView);
        }else{
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }

        rvBank = (RecyclerView) mRootView.findViewById(R.id.rv_bank);

        //设置布局的方式
        rvBank.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        homeListAdapter = new HomeListAdapter();
        homeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                                  @Override
                                                  public void onLoadMoreRequested() {
                                                      //Toast.makeText(MainActivity.this, "" + "more info---", Toast.LENGTH_SHORT).show();
                                                      loadMoreData();
                                                  }
                                              },rvBank
        );
        rvBank.setAdapter(homeListAdapter);
        rvBank.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("postId",homeListAdapter.getItem(position).getId());
                startActivity(intent);
                //overridePendingTransition(R.anim.right_in,R.anim.left_out);
                //Toast.makeText(MainActivity.this, "" + Integer.toString(position) + homeListAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        homeSearchET.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                        if (null != keyEvent && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()) {
                            switch (keyEvent.getAction()) {
                                case KeyEvent.ACTION_UP:
                                    //Toast.makeText(MainActivity.this, "" + "search....." + homeSearchET.getText(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), SearchResltActivity.class);
                                    intent.putExtra("query","" + homeSearchET.getText());
                                    startActivity(intent);
                                    return true;
                                default:
                                    return true;
                            }
                        }
                        return false;
                    }
                }
        );

        initData();

        return mRootView;


    }

    private void initData(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zuofanchi.cn/")
                .callFactory(okHttpClient)
                .build();
        GetList req = retrofit.create(GetList.class);
        Call<ResponseBody> call = req.GetList("load_category_list_v1_json","","" + curPageOffset);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(MainActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
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
                        list.add(bank);

                    }
                    homeListAdapter.setNewData(list);
                    homeListAdapter.notifyDataSetChanged();

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
        GetList req = retrofit.create(GetList.class);
        Call<ResponseBody> call = req.GetList("load_category_list_v1_json","","" + curPageOffset);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(MainActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    String resBodyStr = response.body().string();
                    System.out.println(resBodyStr);
                    Gson gson = new Gson();
                    ResStatusModel resStatusModel = gson.fromJson(resBodyStr, ResStatusModel.class);
                    if(resStatusModel.getErrNo().equals("204")){
                        homeListAdapter.loadMoreEnd(true);
                        //Toast.makeText(MainActivity.this, "" + getString(R.string.no_more_des), Toast.LENGTH_SHORT).show();
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

                        }
                        homeListAdapter.loadMoreComplete();
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


}
