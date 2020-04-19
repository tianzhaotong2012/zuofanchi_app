package com.zfc.app.zuofanchi.adapter;


import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zfc.app.zuofanchi.DiseaseBankEntity;
import com.zfc.app.zuofanchi.R;
import com.zfc.app.zuofanchi.data.DataServer;

/**
 * Created by tian on 2019/12/6.
 */

public class HomeListAdapter extends BaseQuickAdapter<DiseaseBankEntity,BaseViewHolder> {

    public HomeListAdapter() {
        super(R.layout.find_item, DataServer.getSampleData());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DiseaseBankEntity item) {
        //调用赋值
        helper.setText(R.id.tv_item, item.getTitle());
        helper.addOnClickListener(R.id.tv_item);
        helper.addOnClickListener(R.id.item_img);
        Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.item_img));
    }

}
