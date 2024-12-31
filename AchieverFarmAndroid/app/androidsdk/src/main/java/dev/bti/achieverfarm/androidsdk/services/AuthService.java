package dev.bti.achieverfarm.androidsdk.services;

import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.models.req.LoginReq;
import dev.bti.achieverfarm.androidsdk.models.req.UserReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService extends Service {

    @POST("auth/register")
    Call<Response<Pair<String, String>>> createUser(@Body UserReq userReq);

    @POST("auth/login")
    Call<Response<Pair<String, String>>> loginUser(@Body LoginReq loginReq);

    @POST("auth/invalidate")
    Call<Response<Void>> invalidate();
}

