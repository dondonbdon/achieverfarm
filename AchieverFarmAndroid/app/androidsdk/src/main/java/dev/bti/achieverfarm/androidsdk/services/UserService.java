package dev.bti.achieverfarm.androidsdk.services;

import dev.bti.achieverfarm.androidsdk.models.User;
import dev.bti.achieverfarm.androidsdk.models.req.UserReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService extends Service {

    @GET("user/{userId}")
    Call<Response<User>> getUser(@Path("userId") String userId);

    @PUT("/user/{userId}")
    Call<Response<Void>> updateUser(@Path("userId") String userId, @Body UserReq userReq);
}

