package com.zfc.app.zuofanchi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.zfc.app.zuofanchi.Bean.CategoryBean;
import com.zfc.app.zuofanchi.CategoryListActivity;
import com.zfc.app.zuofanchi.DetailActivity;
import com.zfc.app.zuofanchi.R;

import java.util.List;

/**
 * Created by tian on 2019/12/15.
 */

public class HomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean.DataBean.DataListBean> foodDatas;

    public HomeItemAdapter(Context context, List<CategoryBean.DataBean.DataListBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryBean.DataBean.DataListBean subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_category, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(subcategory.getTitle());
        Uri uri = Uri.parse(subcategory.getImgURL());
        //viewHold.iv_icon.setImageURI(uri);
        Glide.with(context).load(uri).transform(new CircleCrop()).into(viewHold.iv_icon);
        viewHold.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "点击" + subcategory.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CategoryListActivity.class);
                intent.putExtra("catName",subcategory.getTitle());
                intent.putExtra("catId",subcategory.getId());
                context.startActivity(intent);
            }
        });
        return convertView;


    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }

}