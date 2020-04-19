package com.zfc.app.zuofanchi.NetInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tian on 2019/12/9.
 */

public interface Search {
    @FormUrlEncoded
    @POST("wp-admin/admin-ajax.php")
    Call<ResponseBody> Search(@Field("action") String action, @Field("query") String query, @Field("pageOffset") String pageOffset);

}
