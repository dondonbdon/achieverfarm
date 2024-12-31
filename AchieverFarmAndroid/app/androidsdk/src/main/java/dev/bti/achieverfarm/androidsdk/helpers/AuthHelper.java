package dev.bti.achieverfarm.androidsdk.helpers;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.client.ProviderApiClient;
import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.models.req.LoginReq;
import dev.bti.achieverfarm.androidsdk.models.req.UserReq;
import dev.bti.achieverfarm.androidsdk.models.res.Response;
import dev.bti.achieverfarm.androidsdk.services.AuthService;

public class AuthHelper {

    private final AuthService service;

    public AuthHelper() {
        this.service = ProviderApiClient.GetInstance(AuthService.class);
    }

    public CallHandler<Response<Pair<String, String>>> createWithEmailAndPassword(UserReq userReq) {
        return new CallHandler<>(service.createUser(userReq));
    }

    public CallHandler<Response<Pair<String, String>>> loginWithEmailAndPassword(String email, String password) {
        return new CallHandler<>(service.loginUser(new LoginReq(email, password)));
    }

    public CallHandler<Response<Void>> invalidateToken() {
        return new CallHandler<>(service.invalidate());
    }
}
