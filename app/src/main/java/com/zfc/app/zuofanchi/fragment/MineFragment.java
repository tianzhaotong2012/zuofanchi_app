package com.zfc.app.zuofanchi.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zfc.app.zuofanchi.CollectListActivity;
import com.zfc.app.zuofanchi.CommonwebviewActivity;
import com.zfc.app.zuofanchi.R;
import com.zfc.app.zuofanchi.ViewedListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tian on 2019/12/15.
 */

public class MineFragment extends Fragment {
    private View mRootView;//缓存Fragment view

    @BindView(R.id.mine_user_collect)
    LinearLayout lToUserCollect;

    @BindView(R.id.mine_user_view)
    LinearLayout lToUserView;

    @BindView(R.id.mine_user_privacy)
    RelativeLayout rlToUserPrivacy;

    @BindView(R.id.mine_user_protocol)
    RelativeLayout rlToUserProtocol;

    @BindView(R.id.mine_to_update_app)
    RelativeLayout rlToUpdateApp;

    @BindView(R.id.mine_to_about_us)
    RelativeLayout rlToAboutUs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mRootView==null){
            mRootView = View.inflate(getActivity(), R.layout.fragment_mine, null);
            ButterKnife.bind(this, mRootView);
        }else{
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }
        lToUserCollect = (LinearLayout) mRootView.findViewById(R.id.mine_user_collect);
        lToUserCollect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), CollectListActivity.class);
                        startActivity(intent);
                    }
                }
        );
        lToUserView = (LinearLayout) mRootView.findViewById(R.id.mine_user_view);
        lToUserView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ViewedListActivity.class);
                        startActivity(intent);
                    }
                }
        );
        rlToUserPrivacy = (RelativeLayout) mRootView.findViewById(R.id.mine_user_privacy);
        rlToUserPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommonwebviewActivity.class);
                intent.putExtra("url", "http://www.zuofanchi.cn/wp-content/themes/zuofanchi/app/user-privacy.html");
                startActivity(intent);
            }
        });

        rlToUserProtocol = (RelativeLayout) mRootView.findViewById(R.id.mine_user_protocol);
        rlToUserProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommonwebviewActivity.class);
                intent.putExtra("url", "http://www.zuofanchi.cn/wp-content/themes/zuofanchi/app/user-protocol.html");
                startActivity(intent);
            }
        });

        rlToUpdateApp = (RelativeLayout) mRootView.findViewById(R.id.mine_to_update_app);
        rlToUpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        });

        rlToAboutUs = (RelativeLayout) mRootView.findViewById(R.id.mine_to_about_us);
        rlToAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommonwebviewActivity.class);
                intent.putExtra("url", "http://www.zuofanchi.cn/wp-content/themes/zuofanchi/app/about-us.html");
                startActivity(intent);
            }
        });
        return mRootView;
    }
}
