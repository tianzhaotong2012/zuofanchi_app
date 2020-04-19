package com.zfc.app.zuofanchi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.zfc.app.zuofanchi.adapter.HomeListAdapter;
import com.zfc.app.zuofanchi.fragment.CategoryFragment;
import com.zfc.app.zuofanchi.fragment.HomeFragment;
import com.zfc.app.zuofanchi.fragment.MineFragment;
import com.zfc.app.zuofanchi.model.PostItem;
import com.zfc.app.zuofanchi.model.PostListModel;
import com.zfc.app.zuofanchi.model.ResModel;
import com.zfc.app.zuofanchi.model.ResStatusModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{
//    private List<DiseaseBankEntity> list = new ArrayList<>();
//    private BaseQuickAdapter mQuickAdapter;
//    private HomeListAdapter homeListAdapter;
//
//    private int curPageOffset = 1;
//
//
//    //获取组件
//    @BindView(R.id.rv_bank)
//    RecyclerView rvBank;
//
//    @BindView(R.id.homeSearchET)
//    EditText homeSearchET;


    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class[] fragmentArray = {HomeFragment.class,CategoryFragment.class ,MineFragment.class,};

    //定义数组来存放按钮图片
    private int[] mImageViewArray = {R.drawable.tab_home_btn,
            R.drawable.tab_category_btn,
            R.drawable.tab_mine_btn};

    //定义数组来存放文字颜色
    private int[] mTextColorArray = {R.drawable.tab_text_color,
            R.drawable.tab_text_color,
            R.drawable.tab_text_color};

    //Tab选项卡的文字
    private String[] mTextViewArray = {"首页", "分类" , "我的"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ButterKnife.bind(this);

        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            // mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        mTabHost.setOnTabChangedListener(this);


//        //设置布局的方式
//        rvBank.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//
//        homeListAdapter = new HomeListAdapter();
//        homeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                //Toast.makeText(MainActivity.this, "" + "more info---", Toast.LENGTH_SHORT).show();
//                loadMoreData();
//            }
//          },rvBank
//        );
//        rvBank.setAdapter(homeListAdapter);
//        rvBank.addOnItemTouchListener(new OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
//                intent.putExtra("postId",homeListAdapter.getItem(position).getId());
//                startActivity(intent);
//                //overridePendingTransition(R.anim.right_in,R.anim.left_out);
//                //Toast.makeText(MainActivity.this, "" + Integer.toString(position) + homeListAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        homeSearchET.setOnEditorActionListener(
//                new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
//                        if (null != keyEvent && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()) {
//                            switch (keyEvent.getAction()) {
//                                case KeyEvent.ACTION_UP:
//                                    //Toast.makeText(MainActivity.this, "" + "search....." + homeSearchET.getText(), Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getBaseContext(), SearchResltActivity.class);
//                                    intent.putExtra("query","" + homeSearchET.getText());
//                                    startActivity(intent);
//                                    return true;
//                                default:
//                                    return true;
//                            }
//                        }
//                        return false;
//                    }
//            }
//        );
//
//        initData();
//
        //定义一个setting记录APP是几次启动
        SharedPreferences setting = getSharedPreferences("com.zfc.app.zuofanchi", 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {// 第一次则跳转到欢迎页面
            setting.edit().putBoolean("FIRST", false).commit();
            useAgree();
        }


    }

//    private void initData(){
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.zuofanchi.cn/")
//                .callFactory(okHttpClient)
//                .build();
//        GetList req = retrofit.create(GetList.class);
//        Call<ResponseBody> call = req.GetList("load_category_list_v1_json","","" + curPageOffset);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                //Toast.makeText(MainActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
//                try {
//                    String resBodyStr = response.body().string();
//                    System.out.println(resBodyStr);
//                    Gson gson = new Gson();
//                    ResModel resModel = gson.fromJson(resBodyStr, ResModel.class);
//                    System.out.println(resModel.getErrNo());
//
//                    for (int i = 0; i < resModel.getData().getPostList().size(); i++) {
//                        DiseaseBankEntity bank = new DiseaseBankEntity();
//                        bank.setId(String.valueOf(i));
//                        PostItem postItem = resModel.getData().getPostList().get(i);
//                        bank.setTitle(postItem.getPostTitle());
//                        bank.setImgUrl(postItem.getCover());
//                        bank.setShareUrl(postItem.getShareUrl());
//                        bank.setId(postItem.getPostId());
//                        list.add(bank);
//
//                    }
//                    homeListAdapter.setNewData(list);
//                    homeListAdapter.notifyDataSetChanged();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void loadMoreData(){
//        curPageOffset++;
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.zuofanchi.cn/")
//                .callFactory(okHttpClient)
//                .build();
//        GetList req = retrofit.create(GetList.class);
//        Call<ResponseBody> call = req.GetList("load_category_list_v1_json","","" + curPageOffset);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                //Toast.makeText(MainActivity.this, "" + response.message()+"   "+response.toString(), Toast.LENGTH_SHORT).show();
//                try {
//                    String resBodyStr = response.body().string();
//                    System.out.println(resBodyStr);
//                    Gson gson = new Gson();
//                    ResStatusModel resStatusModel = gson.fromJson(resBodyStr, ResStatusModel.class);
//                    if(resStatusModel.getErrNo().equals("204")){
//                        homeListAdapter.loadMoreEnd(true);
//                        //Toast.makeText(MainActivity.this, "" + getString(R.string.no_more_des), Toast.LENGTH_SHORT).show();
//                    }
//                    if(resStatusModel.getErrNo().equals("0")){
//                    ResModel resModel = gson.fromJson(resBodyStr, ResModel.class);
//                    System.out.println(resModel.getErrNo());
//
//
//
//
//                        for (int i = 0; i < resModel.getData().getPostList().size(); i++) {
//                            DiseaseBankEntity bank = new DiseaseBankEntity();
//                            bank.setId(String.valueOf(i));
//                            PostItem postItem = resModel.getData().getPostList().get(i);
//                            bank.setTitle(postItem.getPostTitle());
//                            bank.setImgUrl(postItem.getCover());
//                            bank.setShareUrl(postItem.getShareUrl());
//                            bank.setId(postItem.getPostId());
//
//                            homeListAdapter.addData(bank);
//
//                        }
//                        homeListAdapter.loadMoreComplete();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    @OnClick({R.id.home_header_quest})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.home_header_quest:
//                useAgree();
//        }
//    }
//
    private void useAgree(){
        new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_click)    //设置弹窗展示的xml布局
//                .setDialogView(view)  //设置弹窗布局,直接传入View
                .setWidth(600)  //设置弹窗宽度(px)
                .setHeight(800)  //设置弹窗高度(px)
                .setScreenWidthAspect(this, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 0.3f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("DialogTest")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelableOutside(true)     //弹窗在界面外是否可以点击取消
                //.setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                .setOnDismissListener(new DialogInterface.OnDismissListener() { //弹窗隐藏时回调方法
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //Toast.makeText(MainActivity.this, "弹窗消失回调", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnBindViewListener(new OnBindViewListener() {   //通过BindViewHolder拿到控件对象,进行修改
                    @Override
                    public void bindView(BindViewHolder bindViewHolder) {
                        bindViewHolder.setText(R.id.tv_title, "服务协议和隐私声明");
                        //bindViewHolder.setText(R.id.tv_content, "请你务必仔细阅读服务协议和隐私声明的所有条款");
                        final SpannableStringBuilder style = new SpannableStringBuilder();

                        //设置文字
                        style.append("欢迎使用做饭吃APP，请仔细阅读服务协议和隐私政策。使用做饭吃的你需要了解社区指导原则，自觉规范自身行为。如你同意请点击同意按钮开始使用我们的服务");

                        //设置部分文字点击事件
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                //Toast.makeText(MainActivity.this, "查看服务协议", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), CommonwebviewActivity.class);
                                intent.putExtra("url", "http://www.zuofanchi.cn/wp-content/themes/zuofanchi/app/user-protocol.html");
                                startActivity(intent);
                            }
                        };
                        style.setSpan(clickableSpan, 16, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        ClickableSpan clickableSpan1 = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                //Toast.makeText(MainActivity.this, "查看隐私政策", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), CommonwebviewActivity.class);
                                intent.putExtra("url", "http://www.zuofanchi.cn/wp-content/themes/zuofanchi/app/user-privacy.html");
                                startActivity(intent);
                            }
                        };
                        style.setSpan(clickableSpan1, 21, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        TextView t = bindViewHolder.getView(R.id.tv_content);
                        t.setText(style);
                        //设置部分文字颜色
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                        style.setSpan(foregroundColorSpan, 16, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style.setSpan(foregroundColorSpan, 21, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        //配置给TextView
                        t.setMovementMethod(LinkMovementMethod.getInstance());
                        t.setText(style);
                    }
                })
                .addOnClickListener(R.id.btn_left, R.id.btn_right, R.id.tv_title)   //添加进行点击控件的id
                .setOnViewClickListener(new OnViewClickListener() {     //View控件点击事件回调
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        switch (view.getId()) {
                            case R.id.btn_left:
                                //Toast.makeText(MainActivity.this, "left clicked", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case R.id.btn_right:
                                //Toast.makeText(MainActivity.this, "right clicked", Toast.LENGTH_SHORT).show();
                                tDialog.dismiss();
                                break;
                            case R.id.tv_title:
                                //Toast.makeText(MainActivity.this, "title clicked", Toast.LENGTH_SHORT).show();
                                //viewHolder.setText(R.id.tv_title, "Title点击了");
                                break;
                        }
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                })
                .create()   //创建TDialog
                .show();    //展示
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tabhost_item_view, null);

        ImageView iv_tab_icon = (ImageView) view.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mImageViewArray[index]);

        TextView tv_tab_text = (TextView) view.findViewById(R.id.tv_tab_text);
        tv_tab_text.setText(mTextViewArray[index]);
        tv_tab_text.setTextColor(getResources().getColorStateList(mTextColorArray[index]));

        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        supportInvalidateOptionsMenu();
    }

}
