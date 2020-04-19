package com.zfc.app.zuofanchi.NetInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tian on 2019/12/14.
 */

public interface Detail {
    @FormUrlEncoded
    @POST("wp-admin/admin-ajax.php")
    Call<ResponseBody> Detail(@Field("action") String action, @Field("postId") String postId);

}
