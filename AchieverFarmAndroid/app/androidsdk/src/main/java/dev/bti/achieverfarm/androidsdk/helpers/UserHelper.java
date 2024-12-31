package dev.bti.achieverfarm.androidsdk.helpers;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.models.User;
import dev.bti.achieverfarm.androidsdk.models.req.UserReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.UserService;

public class UserHelper {

    private final UserService service;

    public UserHelper(String token) {
        this.service = ProviderApiClient.GetInstance(UserService.class, token);
    }

    public CallHandler<Response<User>> getUser(String userId) {
        return new CallHandler<>(service.getUser(userId));
    }

    public CallHandler<Response<Void>> updateUser(String userId, UserReq userReq) {
        return new CallHandler<>(service.updateUser(userId, userReq));
    }
}