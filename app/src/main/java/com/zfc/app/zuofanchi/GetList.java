package com.zfc.app.zuofanchi;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by tian on 2019/12/3.
 */

public interface GetList {
    @FormUrlEncoded
    @POST("wp-admin/admin-ajax.php")
    Call<ResponseBody> GetList(@Field("action") String action, @Field("categoryId") String categoryId, @Field("pageOffset") String pageOffset);
}
