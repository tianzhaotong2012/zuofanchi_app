package com.zfc.app.zuofanchi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tian on 2019/12/9.
 */

public class CommonwebviewActivity extends Activity {

    private String url;

    private com.tencent.smtt.sdk.WebView mWebView;

    private TextView webviewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview);

        ButterKnife.bind(this);

        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBar);

        Intent intent = this.getIntent();

        url = (String) intent.getSerializableExtra("url");

        webviewTitle = (TextView) findViewById(R.id.common_webview_title);

        mWebView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.common_webview_content);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setBackgroundColor(0); // 设置背景色
        mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                String title=webView.getTitle();
                if(title==null){
                    title="";
                }

                webviewTitle.setText(title);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setAlpha(0.0f);
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }

    @OnClick({R.id.common_webview_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_webview_back:
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
